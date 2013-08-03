package ar.edu.itba.it.paw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;

@Repository
public class ProjectDAO_JDBC extends GenericDAO_JDBC implements ProjectDAO {

	private UserDAO userDAO;

	@Autowired
	public ProjectDAO_JDBC(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private String addUserQuery(Project p, User user) {
		return "INSERT INTO projectusers(projectid,userid) VALUES ("
				+ p.getId() + "," + user.getId() + ");";
	}

	private String deleteUserQuery(Project p, User user) {
		return "DELETE FROM projectusers WHERE projectid = " + p.getId()
				+ " AND userid=" + user.getId() + ";";
	}

	private String updateUserListQuery(Project p) {
		List<User> updated = p.getUsers();
		List<User> actual = getUserList(p.getId());

		String out = "";
		for (User user : actual)
			if (!updated.contains(user))
				out += deleteUserQuery(p, user);

		for (User user : updated)
			if (!actual.contains(user))
				out += addUserQuery(p, user);
		return out;
	}

	private List<User> getUserList(int id) {
		List<User> out = new ArrayList<User>();
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM projectusers WHERE projectid= ?;";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String userID = rs.getString("userid");
				User user = userDAO.load(Integer.valueOf(userID));
				out.add(user);
			}
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return out;
	}

	private Project constructProject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String code = rs.getString("code");
		String name = rs.getString("name");
		String description = rs.getString("description");
		User leader = userDAO.load(rs.getInt("leaderid"));
		boolean ispublic = rs.getBoolean("ispublic");
		List<User> users = getUserList(id);
		Project p = new Project(code, name, description, leader, ispublic,
				users);
		p.setId(id);
		return p;
	}

	public List<Project> load() {
		List<Project> projects = new ArrayList<Project>();
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM Project";
			ResultSet rs = sendQueryResultSet(conn, query);
			while (rs.next())
				projects.add(constructProject(rs));
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return projects;
	}

	public Project load(String code) {
		Project p = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM project WHERE code= ?;";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, code);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				p = constructProject(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return p;
	}

	public Project load(int id) {
		Project p = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM project WHERE id= ?;";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				p = constructProject(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return p;
	}

	public void save(Project p) {
		if (!p.isNew()) {
			update(p);
		} else
			create(p);
	}

	private void create(Project p) {
		try {
			Connection c = openConnection();
			PreparedStatement stmt;
			String query = "INSERT INTO "
					+ "Project (name, description, leaderid, ispublic, code) "
					+ "VALUES (?, ?, ?, ?, ?);";
			query += updateUserListQuery(p);
			stmt = c.prepareStatement(query);
			stmt.setString(1, p.getName());
			stmt.setString(2, p.getDescription());
			stmt.setInt(3, p.getLeader().getId());
			stmt.setBoolean(4, p.getIsPublic());
			stmt.setString(5, p.getCode());
			stmt.executeUpdate();
			c.commit();
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	private void update(Project p) {
		try {
			Connection c = openConnection();
			PreparedStatement stmt;
			String query = "UPDATE Project "
					+ "SET name = ?, description = ?, leaderid = ?, ispublic = ?, code = ? "
					+ "WHERE id = '" + p.getId() + "';";
			query += updateUserListQuery(p);
			stmt = c.prepareStatement(query);
			stmt.setString(1, p.getName());
			stmt.setString(2, p.getDescription());
			stmt.setInt(3, p.getLeader().getId());
			stmt.setBoolean(4, p.getIsPublic());
			stmt.setString(5, p.getCode());
			stmt.executeUpdate();
			c.commit();
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}
}
