package ar.edu.itba.it.paw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class ProjectDAO_JDBC extends GenericDAO_JDBC implements ProjectDAO {
	private static ProjectDAO_JDBC instance = new ProjectDAO_JDBC();

	private ProjectDAO_JDBC() {
	}

	public static ProjectDAO_JDBC getInstance() {
		return instance;
	}

	private Project constructProject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String code = rs.getString("code");
		String name = rs.getString("name");
		String description = rs.getString("description");
		User leader = UserServiceImpl.getInstance().getUser(
				rs.getInt("leaderid"));
		boolean ispublic = rs.getBoolean("ispublic");
		Project p = new Project(code, name, description, leader, ispublic);
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
			e.printStackTrace();
		}
		return projects;
	}

	public Project load(String code) {
		Project p = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM Project WHERE code='" + code + "';";
			ResultSet rs = sendQueryResultSet(conn, query);
			if (rs.next())
				p = constructProject(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}

	public Project load(int id) {
		Project p = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM Project WHERE id='" + id + "';";
			ResultSet rs = sendQueryResultSet(conn, query);
			if (rs.next())
				p = constructProject(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public void save(Project p) {
		if (p.getId() > 0) {
			update(p);
		} else
			create(p);
	}
	
	private void create(Project p){
		try {
			Connection c = openConnection();
			PreparedStatement stmt;
			String query =	"INSERT INTO "
							+ "Project (name, description, leaderid, ispublic, code) "
							+ "VALUES (?, ?, ?, ?, ?);";
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
			e.printStackTrace();
		}
	}
	
	private void update(Project p){
		try {
			Connection c = openConnection();
			PreparedStatement stmt;
			String query =	"UPDATE Project "
							+ "SET name = ?, description = ?, leaderid = ?, ispublic = ?, code = ? "
							+ "WHERE id = '" + p.getId() + "';";
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
			e.printStackTrace();
		}
	}
}
