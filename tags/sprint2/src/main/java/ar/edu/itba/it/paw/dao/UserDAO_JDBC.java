package ar.edu.itba.it.paw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.model.User;

@Repository
public class UserDAO_JDBC extends GenericDAO_JDBC implements UserDAO {

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
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	public User load(int id) {
		User user = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM systemuser WHERE id= ?;";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				user = constructUser(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return user;
	}

	public User load(String name) {
		User user = null;
		try {
			Connection conn = openConnection();
			String query = "SELECT * FROM systemuser WHERE name= ?;";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				user = constructUser(rs);
			closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
		return user;
	}

	public void save(User user) {
		if (!user.isNew())
			update(user);
		else
			create(user);
	}

	private void update(User user) {
		try {
			Connection c = openConnection();
			String query =	"UPDATE	systemuser " +
							"SET	name = ?, password = ?, isadmin = ?, " + 
							"		valid = ?, fullname = ? " +
							"WHERE	id = ?;";
			
			PreparedStatement stmt = c.prepareStatement(query);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getPassword());
			stmt.setBoolean(3, user.getAdmin());
			stmt.setBoolean(4, user.getValid());
			stmt.setString(5, user.getFullname());
			stmt.setInt(6, user.getId());
			stmt.executeUpdate();
			
			c.commit();
			closeConnection(c);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

	private void create(User user) {
		try {
			Connection c = openConnection();
			String query =	"INSERT INTO systemuser (name, password, isadmin, valid, fullname) " +
							"VALUES (?, ?, ?, ?, ?);";
			
			PreparedStatement stmt = c.prepareStatement(query);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getPassword());
			stmt.setBoolean(3, user.getAdmin());
			stmt.setBoolean(4, user.getValid());
			stmt.setString(5, user.getFullname());			
			stmt.executeUpdate();
			
			c.commit();
			closeConnection(c);
		} catch (SQLException e) {
			throw new RuntimeException("Error en la base de datos", e);
		}
	}

}
