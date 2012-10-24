package su.server.ws.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import su.server.ws.model.Login;
import su.server.ws.model.MonthlyEventsRequest;
import su.server.ws.model.Meal;
import su.server.ws.model.MenuDetails;
import su.server.ws.model.POI;
import su.server.ws.model.POIList;


public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public MySQLAccess() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://localhost/aLaCarte?" + "user=root");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean verifyLogin(Login login) throws Exception {
		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT * FROM utilizadores WHERE email='" + login.getEmail() + "' AND password='" + login.getPass() +"'");
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()){
				return true;
			}
			else{
				return false;
			}

		} catch (Exception e) {
			throw e;
		}

	}

	public HashMap<String, Integer> checkEvents(MonthlyEventsRequest req) throws Exception{
		HashMap<String, Integer> lista_eventos = null;
		int counter=0;
		
		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT COUNT(r.id), DAYOFMONTH(s.horario) FROM reservas r, utilizadores u, slots s WHERE r.id_utilizador = u.id AND r.id_slot = s.id AND u.id = "+req.getId_utilizador()+" GROUP BY DAYOFMONTH(s.horario)");
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()){
				//return true;
				lista_eventos.put(resultSet.getString(1), resultSet.getInt(2));
			}

		} catch (Exception e) {
			throw e;
		}
		
		return lista_eventos;
	}
	
	public POIList getPOIs() throws Exception {
		POIList poiList = null;

		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT id, lat, lng, name, address, category, capacity FROM cantinas");
			resultSet = preparedStatement.executeQuery();

			poiList = new POIList();
			List<POI> list = new ArrayList<POI>();

			while(resultSet.next()) {
				POI poi = new POI();
				poi.setId(resultSet.getString(1));
				poi.setLocation(resultSet.getDouble(2), resultSet.getDouble(3));
				poi.setName(resultSet.getString(4));
				poi.setAddress(resultSet.getString(5));
				poi.setCategory(resultSet.getString(6));
				poi.setCapacity(resultSet.getInt(7));

				list.add(poi);
			}

			poiList.setPoiList(list);

		} catch (Exception e) {
			throw e;
		}

		return poiList;
	}

	public MenuDetails getMenuDetails(String poiID) throws Exception {
		MenuDetails menuDetails = null;

		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT m.id, m.sopa, m.carne, m.peixe, m.price FROM menu m, cantinas c, slots s WHERE c.id = s.id_cantina AND m.id = s.id_menu AND c.id = "+poiID+" GROUP BY m.id");
			resultSet = preparedStatement.executeQuery();
			
			menuDetails = new MenuDetails();
			List<Meal> lunch = new ArrayList<Meal>();
			List<Meal> dinner = new ArrayList<Meal>();

			int i=0;
			while(resultSet.next()) {
				i++;
				
				Meal meal = new Meal();
				meal.setId(resultSet.getString(1));
				meal.setSopa(resultSet.getString(2));
				meal.setCarne(resultSet.getString(3));
				meal.setPeixe(resultSet.getString(4));
				meal.setPrice(resultSet.getString(5));
				
				if(i%2 == 1) {
					lunch.add(meal);
				} else {
					dinner.add(meal);
				}
			}
			
			menuDetails.setMenuLunch(lunch);
			menuDetails.setMenuDinner(dinner);

		} catch(Exception e) {
			throw e;
		}
		
		return menuDetails;
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