package ar.edu.itba.it.paw.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.it.paw.User;

public class UserDAO_JDBC extends GenericDAO_JDBC implements UserDAO {

	private static UserDAO_JDBC instance = new UserDAO_JDBC();

	private UserDAO_JDBC() {

	}

	public static UserDAO_JDBC getInstance() {
		return instance;
	}

	private User constructUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String password = rs.getString("password");
		boolean admin = rs.getBoolean("isadmin");
		boolean valid = rs.getBoolean("valid");
		String fullname = rs.getString("fullname");
		User user = new User(admin, valid, name, password, fullname);
		user.setId(id);
		return user;
	}

	public List<User> load() {
		try {
			List<User> users = new ArrayList<User>();
			Connection conn = openConnection();
			String query = "SELECT * FROM systemuser";
			ResultSet rs = sendQueryResultSet(conn, query);
			while (rs.next())
				users.add(constructUser(rs));
			closeConnection(conn);
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User load(int id) {
		User user = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM systemuser WHERE id='" + id + "';";
			ResultSet rs = sendQueryResultSet(conn, query);
			if (rs.next())
				user = constructUser(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User load(String name) {
		User user = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM systemuser WHERE name='" + name
					+ "';";
			ResultSet rs = sendQueryResultSet(conn, query);
			if (rs.next())
				user = constructUser(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public void save(User user) {
		if (user.getId() > 0)
			update(user);
		else
			create(user);
	}

	private void update(User user) {
		try {
			Connection c = openConnection();
			String query = "update systemuser set";
			query += " name='" + user.getName() + "', ";
			query += " password='" + user.getPassword() + "', ";
			query += " isadmin=" + user.getAdmin() + ", ";
			query += " valid=" + user.getValid() + ", ";
			query += " fullname='" + user.getFullname() + "' ";
			query += " where id=" + user.getId() + ";";
			sendQuery(c, query);
			closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void create(User user) {
		try {
			Connection c = openConnection();
			String query = "insert into systemuser(name,password,isadmin,valid,fullname) VALUES(";
			query += "'" + user.getName() + "',";
			query += "'" + user.getPassword() + "',";
			query += user.getAdmin() + ",";
			query += user.getValid() + ",";
			query += "'" + user.getFullname() + "');";
			sendQuery(c, query);
			closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
