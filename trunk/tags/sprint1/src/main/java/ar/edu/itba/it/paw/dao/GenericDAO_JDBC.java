package ar.edu.itba.it.paw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import ar.edu.itba.it.paw.DBException;

public class GenericDAO_JDBC {

	protected Connection openConnection() throws SQLException {

		Connection conn = null;
		try {
			Properties prop = new Properties();
			prop.load(getClass().getClassLoader().getResourceAsStream("connection.properties"));
			String url = prop.getProperty("url");
			conn = DriverManager.getConnection(url, prop);
		} catch (Exception e) {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost/paw2", "paw", "paw");
		}
		return conn;
	}

	protected void closeConnection(Connection c) throws SQLException {
		c.close();
	}

	protected void sendQuery(Connection c, String query) throws SQLException {
		PreparedStatement stmt = c.prepareStatement(query);
		stmt.executeUpdate();
		return;
	}

	protected ResultSet sendQueryResultSet(Connection c, String query)
			throws SQLException {
		PreparedStatement stmt = c.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		return rs;
	}

}
