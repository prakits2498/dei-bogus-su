package su.server.ws.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import su.server.ws.model.Login;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	

	public MySQLAccess() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB ---feedback � o nome da tabela
			connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?" + "user=sqluser&password=sqluserpw");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean verifyLogin(Login login) throws Exception {
		try {
//			// Statements allow to issue SQL queries to the database
//			statement = connect.createStatement();
//			// Result set get the result of the SQL query
//			resultSet = statement.executeQuery("select * from FEEDBACK.COMMENTS");
//			writeResultSet(resultSet);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT * FROM utilizadores WHERE email=" + login.getEmail() + " AND password=" + login.getPass());
			// Parameters start with 1
//			preparedStatement.setString(1, login.getEmail());
//			preparedStatement.setString(2, login.getPass());
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				System.out.println("Login bem sucedido!!");
				return true;
			}
			else{
				System.out.println("Login mal sucedido!!");
				return false;
			}
//			preparedStatement = connect.prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
//			resultSet = preparedStatement.executeQuery();
//			writeResultSet(resultSet);
//
//			// Remove again the insert comment
//			preparedStatement = connect
//					.prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
//			preparedStatement.setString(1, "Test");
//			preparedStatement.executeUpdate();
//
//			resultSet = statement
//					.executeQuery("select * from FEEDBACK.COMMENTS");
//			writeMetaData(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}
	
	

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		//   Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summery = resultSet.getString("summery");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summery: " + summery);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

} 