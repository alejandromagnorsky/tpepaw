package ar.edu.itba.it.paw.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.model.Access;
import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;

@Repository
public class IssueDAO_JDBC extends GenericDAO_JDBC implements IssueDAO {

	private ProjectDAO projectDAO;
	private UserDAO userDAO;

	@Autowired
	private IssueDAO_JDBC(ProjectDAO projectDAO, UserDAO userDAO) {
		this.userDAO = userDAO;
		this.projectDAO = projectDAO;
	}

	private Issue constructIssue(ResultSet rs) throws SQLException {
		int issueId = rs.getInt("id");

		String title = rs.getString("title");

		String description = rs.getString("description");

		int projectId = rs.getInt("projectid");
		Project project = projectDAO.load(projectId);

		int assignedUserId = rs.getInt("assigneduser");
		User assignedUser = userDAO.load(assignedUserId);

		int reportedUserId = rs.getInt("reporteduser");
		User reportedUser = userDAO.load(reportedUserId);

		DateTime creationDate = new DateTime(rs.getTimestamp("creationDate"));

		Time estimatedTime = null;
		if (rs.getInt("estimatedtime") > 0)
			estimatedTime = new Time(rs.getInt("estimatedtime"));

		State state = State.valueOf(rs.getString("state"));

		Priority priority = null;
		if (rs.getString("priority") != null)
			priority = Priority.valueOf(rs.getString("priority"));

		Resolution resolution = null;
		if (rs.getString("resolution") != null)
			resolution = Resolution.valueOf(rs.getString("resolution"));
		
		Issue issue = new Issue(project, title, description, creationDate,
				estimatedTime, assignedUser, reportedUser, state, priority);
		
		issue.setId(issueId);
		
		if (resolution != null)
			issue.setResolution(resolution);

		return issue;
	}

	public List<Issue> load() {
		try {
			Connection c = openConnection();
			String query = "SELECT * FROM issue;";
			ResultSet rs = sendQueryResultSet(c, query);

			List<Issue> out = new ArrayList<Issue>();
			while (rs.next())
				out.add(constructIssue(rs));

			closeConnection(c);
			return out;
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	public Issue load(int id) {
		try {
			Connection c = openConnection();
			String query = "SELECT * FROM issue WHERE issue.id = ?;";
			
			PreparedStatement stmt = c.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			Issue out = null;
			if (rs.next())
				out = constructIssue(rs);
			closeConnection(c);
			return out;
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	private void create(Issue i) {
		try {
			Connection c = openConnection();
			String query = "INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

			PreparedStatement stmt = c.prepareStatement(query);
			stmt.setString(1, i.getTitle());
			stmt.setString(2, i.getDescription());
			if(i.getAssignedUser() == null)
				stmt.setObject(3, null);
			else
				stmt.setInt(3, i.getAssignedUser().getId());
			
			stmt.setInt(4, i.getReportedUser().getId());
			stmt.setString(5, i.getState().name());
			stmt.setInt(6, i.getProject().getId());
			stmt.setTimestamp(7, new Timestamp(i.getCreationDate().getMillis()));
			if(i.getEstimatedTime() != null)
				stmt.setInt(8, i.getEstimatedTime().getMinutes());
			else
				stmt.setObject(8, null);
			stmt.setString(9, i.getPriority().name());
			ResultSet rs = stmt.executeQuery();

			int id = 0;
			if (rs.next())
				id = rs.getInt("id");
			i.setId(id);

			c.commit();
			closeConnection(c);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	private void update(Issue i) {
		try {
			Connection c = openConnection();
			String query = "UPDATE issue SET"
						 + " title = ?, description = ?, assigneduser = ?, reporteduser = ?, state = ?, projectid = ?, creationdate = ?, estimatedtime = ?, priority = ?, resolution = ?"
						 + "WHERE id = ?;";

			PreparedStatement stmt = c.prepareStatement(query);
			stmt.setString(1, i.getTitle());
			stmt.setString(2, i.getDescription());
			if(i.getAssignedUser() == null)
				stmt.setObject(3, null);
			else
				stmt.setInt(3, i.getAssignedUser().getId());
			stmt.setInt(4, i.getReportedUser().getId());
			stmt.setString(5, i.getState().name());
			stmt.setInt(6, i.getProject().getId());
			stmt.setTimestamp(7, new Timestamp(i.getCreationDate().getMillis()));
			if(i.getEstimatedTime() != null)
				stmt.setInt(8, i.getEstimatedTime().getMinutes());
			else
				stmt.setObject(8, null);
			stmt.setString(9, i.getPriority().name());
			stmt.setString(10, (i.getResolution() == null) ? null : i.getResolution().name());
			stmt.setInt(11, i.getId());
			stmt.executeUpdate();

			c.commit();
			closeConnection(c);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	public void save(Issue i) {
		if (!i.isNew()) {
			update(i);
		} else
			create(i);
	}

	private Comment constructComment(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		User user = userDAO.load(rs.getInt("userid"));
		DateTime date = new DateTime(rs.getTimestamp("date"));
		String description = rs.getString("description");
		Issue issue = load(rs.getInt("issueid"));

		Comment c = new Comment(user, date, description, issue);
		c.setId(id);
		return c;
	}

	public SortedSet<Comment> loadComments() {
		SortedSet<Comment> comments = new TreeSet<Comment>();
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM comment";
			ResultSet rs = sendQueryResultSet(conn, query);
			while (rs.next())
				comments.add(constructComment(rs));
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return comments;
	}

	public Comment loadComment(int id) {
		Comment comment = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM comment WHERE comment.id = ?;";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				comment = constructComment(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return comment;
	}

	public void saveComment(Comment comment) {
		try {
			Connection c = openConnection();
			String query = "INSERT INTO comment (userid, date, description, issueid) "
						 + "VALUES (?, ?, ?, ?) RETURNING id;";
			
			PreparedStatement stmt;
			stmt = c.prepareStatement(query);
			stmt.setInt(1, comment.getUser().getId());
			stmt.setTimestamp(2, new Timestamp(comment.getDate().getMillis()));
			stmt.setString(3, comment.getDescription());
			stmt.setInt(4, comment.getIssue().getId());
			ResultSet rs = stmt.executeQuery();
			
			int id = 0;
			if (rs.next())
				id = rs.getInt("id");
			comment.setId(id);

			c.commit();
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	private Work constructWork(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		User user = userDAO.load(rs.getInt("userid"));
		DateTime date = new DateTime(rs.getTimestamp("date"));
		String description = rs.getString("description");
		Time dedicatedTime = new Time(rs.getInt("dedicatedtime"));
		Issue issue = load(rs.getInt("issueid"));

		Work w = new Work(user, date, description, dedicatedTime, issue);
		w.setId(id);
		return w;
	}

	public SortedSet<Work> loadWorks() {
		SortedSet<Work> works = new TreeSet<Work>();
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM work";
			ResultSet rs = sendQueryResultSet(conn, query);
			while (rs.next())
				works.add(constructWork(rs));
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return works;
	}

	public Work loadWork(int id) {
		Work work = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM work WHERE work.id = ?;";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				work = constructWork(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return work;
	}

	public void saveWork(Work w) {
		if (!w.isNew())
			update(w);
		else
			create(w);
	}

	private void create(Work w) {
		try {
			Connection c = openConnection();
			String query;
			query = "INSERT	INTO work (dedicatedtime, userid, date, description, issueid) "
				  + "VALUES (?, ?, ?, ?, ?) RETURNING id;";			

			PreparedStatement stmt;
			stmt = c.prepareStatement(query);
			stmt.setInt(1, w.getDedicatedTime().getMinutes());
			stmt.setInt(2, w.getUser().getId());
			stmt.setTimestamp(3, new Timestamp(w.getDate().getMillis()));
			stmt.setString(4, w.getDescription());
			stmt.setInt(5, w.getIssue().getId());
			ResultSet rs = stmt.executeQuery();
			
			int id = 0;
			if (rs.next())
				id = rs.getInt("id");
			w.setId(id);

			c.commit();
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	private void update(Work w) {
		try {
			Connection c = openConnection();
			String query = "UPDATE work " 
						 + "SET dedicatedtime = ?, userid = ?, date = ?, description = ?, issueid = ?"
						 + "WHERE id = ?;";
			
			PreparedStatement stmt;
			stmt = c.prepareStatement(query);
			stmt.setInt(1, w.getDedicatedTime().getMinutes());
			stmt.setInt(2, w.getUser().getId());
			stmt.setTimestamp(3, new Timestamp(w.getDate().getMillis()));
			stmt.setString(4, w.getDescription());
			stmt.setInt(5, w.getIssue().getId());
			stmt.setInt(6, w.getId());
			stmt.executeUpdate();
			
			c.commit();
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}
	
	
	private Access constructAccess(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		LocalDate date = new LocalDate(rs.getTimestamp("date"));
		Issue issue = load(rs.getInt("issueid"));

		Access access = new Access(date, issue);
		access.setId(id);
		return access;
	}

	public List<Access> loadAccess(Issue issue){
		try {
			Connection c = openConnection();
			String query = "SELECT * FROM access WHERE issueid = ?;";
			
			PreparedStatement stmt = c.prepareStatement(query);
			stmt.setInt(1, issue.getId());
			ResultSet rs = stmt.executeQuery();

			List<Access> out = new ArrayList<Access>();
			while (rs.next())
				out.add(constructAccess(rs));

			closeConnection(c);
			return out;
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}
	
	public List<Access> loadAccess(){
		try {
			Connection c = openConnection();
			String query = "SELECT * FROM access;";
			ResultSet rs = sendQueryResultSet(c, query);

			List<Access> out = new ArrayList<Access>();
			while (rs.next())
				out.add(constructAccess(rs));

			closeConnection(c);
			return out;
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}
	
	
	public void saveAccess(Access access){
		try {
			Connection c = openConnection();
			String query;
			query = "INSERT	INTO access (date, issueid) "
				  + "VALUES (?, ?);";			

			PreparedStatement stmt;
			stmt = c.prepareStatement(query);
			stmt.setDate(1, new Date(access.getDate().toDateTimeAtStartOfDay().getMillis()));
			stmt.setInt(2, access.getIssue().getId());
			stmt.executeUpdate();
			
			c.commit();
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}
}
