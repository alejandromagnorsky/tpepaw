package ar.edu.itba.it.paw.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.Issue.Priority;
import ar.edu.itba.it.paw.Issue.Resolution;
import ar.edu.itba.it.paw.Issue.State;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;

public class IssueDAO_JDBC extends GenericDAO_JDBC implements IssueDAO {

	private static IssueDAO_JDBC instance = new IssueDAO_JDBC();

	public static IssueDAO_JDBC getInstance() {
		return instance;
	}

	private IssueDAO_JDBC() {
	}

	private Issue constructIssue(ResultSet rs) throws SQLException {
		int issueId = rs.getInt("id");

		String title = rs.getString("title");

		String description = rs.getString("description");

		int projectId = rs.getInt("projectid");
		Project project = ProjectDAO_JDBC.getInstance().load(projectId);

		int assignedUserId = rs.getInt("assigneduser");
		User assignedUser = UserDAO_JDBC.getInstance().load(assignedUserId);

		int reportedUserId = rs.getInt("reporteduser");
		User reportedUser = UserDAO_JDBC.getInstance().load(reportedUserId);

		DateTime creationDate = new DateTime(rs.getTimestamp("creationDate"));
		DateTime completionDate = null;
		if (rs.getDate("completionDate") != null)
			completionDate = new DateTime(rs.getDate("completionDate"));

		float estimatedTime = rs.getFloat("estimatedtime");

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
		issue.setCompletionDate(completionDate);

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
			e.printStackTrace();
			return null;
		}
	}

	public Issue load(int id) {
		try {
			Connection c = openConnection();
			String query = "SELECT * FROM issue WHERE issue.id =" + id + ";";
			ResultSet rs = sendQueryResultSet(c, query);
			Issue out = null;
			if (rs.next())
				out = constructIssue(rs);
			closeConnection(c);
			return out;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void create(Issue i) {
		try {
			Connection c = openConnection();
			String query = "insert into issue (title, description, assigneduser, reporteduser, "
					+ "state, projectid, creationdate, estimatedtime, priority) VALUES (";
			query += " '" + i.getTitle() + "', ";
			query += " '" + i.getDescription() + "', ";
			query += " " + ( (i.getAssignedUser() == null) ? null : i.getAssignedUser().getId() ) + ", ";
			query += " " + i.getReportedUser().getId() + ", ";
			query += " '" + i.getState().name() + "', ";
			query += " " + i.getProject().getId() + ", ";
			query += " '" + new Timestamp(i.getCreationDate().getMillis())
					+ "', ";
			query += " " + i.getEstimatedTime() + ", ";
			query += " '" + i.getPriority().name() + "'";
			query += ") returning id;"; // OMFG THIS IS SO GREAT!
			ResultSet rs = sendQueryResultSet(c, query);

			if (rs.next())
				i.setId(rs.getInt("id"));

			closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void update(Issue i) {
		try {
			Connection c = openConnection();
			String query = "update issue set";
			query += " title='" + i.getTitle() + "', ";
			query += " description='" + i.getDescription() + "', ";
			query += " assigneduser=" +( (i.getAssignedUser() == null) ? null : i.getAssignedUser().getId() ) + ", ";
			query += " reporteduser='" + i.getReportedUser().getId() + "', ";
			query += " state='" + i.getState().name() + "', ";
			query += " projectid='" + i.getProject().getId() + "', ";
			query += " creationdate='"
					+ new Timestamp(i.getCreationDate().getMillis()) + "', ";

			if (i.getCompletionDate() != null)
				query += " completiondate='"
						+ new Timestamp(i.getCompletionDate().getMillis())
						+ "', ";
			query += " estimatedtime='" + i.getEstimatedTime() + "' ";

			if (i.getPriority() != null)
				query += ", priority='" + i.getPriority().name() + "' ";

			if (i.getResolution() != null)
				query += ", resolution='" + i.getResolution().name() + "' ";
			query += " where id='" + i.getId() + "';";
			sendQuery(c, query);
			closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save(Issue i) {
		if (i.getId() > 0) {
			update(i);
		} else
			create(i);
	}

}
