package rs.deibogus.server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private final String DIRECTORY = System.getProperty("user.dir") + "/data/db.sqlite";
	private Connection conn;
	private Statement stat;
	private static DBManager instance;

	private DBManager() {
		if (!checkSQLITEfile()) {
			createBD();
		} else {
			connection();
		}
	}

	public static DBManager getInstance() {
		if (instance==null)
			instance = new DBManager();
		return instance;
	}

	/**
	 * Connection with the DB
	 */
	private void connection() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn = DriverManager.getConnection("jdbc:sqlite:" + DIRECTORY);
			this.stat = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void destroyConnection() {
		try {
			conn.close();
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean addClient(String username, String password) {
		connection();
		try {
			PreparedStatement prep = null;
			//table client: {id, username, password}
			String sql = "INSERT INTO client(username, password) values (?,?);";
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			prep.setString(2, password);
			//sending information to DB
			prep.addBatch();
			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);

			destroyConnection();
		} catch (SQLException ex) {
			return false;
		}
		
		return true;
	}

	public boolean clientLogin(String username, String password) {
		connection();
		try {
			String sql = "SELECT * FROM client WHERE username = '"+username+"' AND password = '"+password+"'";
			ResultSet result = stat.executeQuery(sql);
			if(result.next())
				return true;

			result.close();
			destroyConnection();
			return false;
		} catch (SQLException ex) {
			destroyConnection();
			return false;
		}
	}

	/**
	 * Creates a new DB, table "client"
	 */
	private void createBD() {
		try {
			connection();
			stat.executeUpdate("CREATE TABLE client (id INTEGER PRIMARY KEY, username TEXT, password TEXT);");
			destroyConnection();
		} catch (SQLException ex) {
			System.err.println("Database error: " + ex);
		}

	}//end createBD

	/**
	 * Verify if the DB file name exists
	 * @return - true or false
	 */
	private boolean checkSQLITEfile() {
		File f = new File(DIRECTORY);
		if (f.exists()) {
			return true;
		} else {
			try {
				System.err.println("Database not found: " + DIRECTORY);
				f.createNewFile();
				return false;
			} catch (IOException ex) {
				return false;
			}
		}
	}


}
