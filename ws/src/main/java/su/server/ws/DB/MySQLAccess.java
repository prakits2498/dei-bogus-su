package su.server.ws.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import su.server.ws.model.ConfirmationData;
import su.server.ws.model.DayEventsRequest;
import su.server.ws.model.Login;
import su.server.ws.model.Meal;
import su.server.ws.model.MenuDetails;
import su.server.ws.model.MonthlyEventsRequest;
import su.server.ws.model.POI;
import su.server.ws.model.POIList;
import su.server.ws.model.Reserva;
import su.server.ws.model.Slot;


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

	public int verifyLogin(Login login) throws Exception {
		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT id FROM utilizadores WHERE email='" + login.getEmail() + "' AND password='" + login.getPass() +"'");
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()){
				return resultSet.getInt(1);
			}
			else{
				return -1;
			}

		} catch (Exception e) {
			throw e;
		}

	}
	
	public ConfirmationData getConfirmationData (ConfirmationData data) throws Exception{
		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT email,telemovel,name FROM utilizadores WHERE id=" + data.getIdUser());
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()){
				data.setEmail(resultSet.getString(1));
				data.setNumTlm(resultSet.getString(2));
				data.setNomeUtilizador(resultSet.getString(3));
				return data;
			}
			else{
				return null;
			}

		} catch (Exception e) {
			throw e;
		}
	}
	

	public boolean actualizaCreditos(String userID, String credits) throws Exception {

		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("UPDATE utilizadores SET credits = "+credits+" WHERE id = "+userID);
			int res = preparedStatement.executeUpdate();
			if(res == 1)
				return true;
			else
				return false;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	public boolean makeReservation(Reserva reserva) throws Exception{
		try {
			if(reserva.isCreditos())
				actualizaCreditos(reserva.getUserID(), reserva.getUserCredits());
			
			actualizaReservados(reserva.getSlotID());
		
			preparedStatement = connect.prepareStatement("INSERT INTO reservas (id_utilizador, id_cantina, date, id_slot, price) VALUES('" + reserva.getUserID() + "', '" + reserva.getPoiID() + "', NOW(), '" + reserva.getSlotID() + "', '" + reserva.getPriceMeal() + "')");
			int num = preparedStatement.executeUpdate();

			if(num==1){
				return true;
			}
			else{
				return false;
			}

		} catch (Exception e) {
			throw e;
		}
	}
	
	public int  actualizaReservados(String slotID) throws Exception {
		int res=0;
		try {
			int n_reservados = getNumReservados(slotID);
			
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("UPDATE slots SET n_reservados = "+(n_reservados++)+" WHERE id = "+slotID);
			res = preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw e;
		}
		
		return res;
	}
	
	public int getNumReservados(String slotID) throws Exception {
		try {
			preparedStatement = connect.prepareStatement("SELECT n_reservados FROM slots WHERE id=" + slotID);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()){
				return resultSet.getInt(1);
			}
			else{
				return -1;
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public double getCredits(int idUser) throws Exception {
		try {
			preparedStatement = connect.prepareStatement("SELECT credits FROM utilizadores WHERE id=" + idUser);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()){
				return resultSet.getDouble(1);
			}
			else{
				return -1;
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public MonthlyEventsRequest checkEvents(MonthlyEventsRequest req) throws Exception{
		//List<String> lista_eventos = new ArrayList<String>();
		HashMap<String, String> lista_eventos = new HashMap<String, String>();

		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT COUNT(r.id), DAYOFMONTH(s.horario) FROM reservas r, utilizadores u, slots s WHERE r.id_utilizador = u.id AND r.id_slot = s.id AND u.id = "+req.getIdUser()+" AND MONTH(s.horario)="+req.getMonth()+" GROUP BY DAYOFMONTH(s.horario)");
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()){
				//lista_eventos.add(resultSet.getString(1)+"|"+resultSet.getString(2));
				lista_eventos.put(resultSet.getString(2), resultSet.getString(1));
			}
			req.setListEvents(lista_eventos);

		} catch (Exception e) {
			throw e;
		}

		return req;
	}

	public DayEventsRequest getMenuFromReservations(DayEventsRequest req) throws Exception{
		HashMap<String, String> eventos_almoco = new HashMap<String, String>();
		HashMap<String, String> eventos_jantar = new HashMap<String, String>();

		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT m.sopa, m.carne, m.peixe, m.price, c.name, hour(s.horario),minute(s.horario) FROM utilizadores u, reservas r, cantinas c, slots s, menu m  WHERE u.id = r.id_utilizador AND r.id_cantina  = c.id AND r.id_slot = s.id AND s.id_menu = m.id AND u.id = " + req.getIdUser() + " AND MONTH(s.horario) = " + req.getMonth() + " AND DAYOFMONTH(s.horario) = " + req.getDay());
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()){
				if(resultSet.getInt(6) >= 19){
					System.out.println(resultSet.toString());
					eventos_almoco.put("sopa", resultSet.getString(1));
					eventos_almoco.put("carne", resultSet.getString(2));
					eventos_almoco.put("peixe", resultSet.getString(3));
					eventos_almoco.put("preco", Integer.toString(resultSet.getInt(4)));
					eventos_almoco.put("cantina", resultSet.getString(5));
					String slot = resultSet.getString(6).concat(":").concat(resultSet.getString(7));
					eventos_almoco.put("slot", slot);
				}
				else{
					eventos_jantar.put("sopa", resultSet.getString(1));
					eventos_jantar.put("carne", resultSet.getString(2));
					eventos_jantar.put("peixe", resultSet.getString(3));
					eventos_jantar.put("preco", Integer.toString(resultSet.getInt(4)));
					eventos_jantar.put("cantina", resultSet.getString(5));
					String slot = resultSet.getString(6).concat(":").concat(resultSet.getString(7));
					eventos_jantar.put("slot", slot);
				}
				//lista_eventos.add(resultSet.getString(1)+"|"+resultSet.getString(2));
				//lista_eventos.put(resultSet.getString(2), resultSet.getString(1));
			}
			req.setLunchEvents(eventos_almoco);
			req.setDinnerEvents(eventos_jantar);

		} catch (Exception e) {
			throw e;
		}

		return req;
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
			preparedStatement = connect.prepareStatement("SELECT m.id, m.sopa, m.carne, m.peixe, m.price, DAYOFMONTH(s.horario), MONTH(s.horario) FROM menu m, cantinas c, slots s WHERE c.id = s.id_cantina AND m.id = s.id_menu AND c.id = "+poiID+" GROUP BY m.id");
			resultSet = preparedStatement.executeQuery();

			menuDetails = new MenuDetails();
			List<Meal> lunch = new ArrayList<Meal>();
			List<Meal> dinner = new ArrayList<Meal>();

			String day="", month="";
			
			int i=0;
			while(resultSet.next()) {
				i++;

				Meal meal = new Meal();
				meal.setId(resultSet.getString(1));
				meal.setSopa(resultSet.getString(2));
				meal.setCarne(resultSet.getString(3));
				meal.setPeixe(resultSet.getString(4));
				meal.setPrice(resultSet.getString(5));
				
				day = resultSet.getString(6);
				month = resultSet.getString(7);
				
				meal.setDay(day);
				meal.setMonth(month);

				if(i%2 == 1) {
					lunch.add(meal);
				} else {
					dinner.add(meal);
				}
			}

			menuDetails.setPOI(poiID);
			menuDetails.setMenuLunch(lunch);
			menuDetails.setMenuDinner(dinner);

		} catch(Exception e) {
			throw e;
		}

		return menuDetails;
	}
	
	public Reserva getSlots(Reserva reserva) throws Exception {
		try {
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("SELECT s.id, DAYOFMONTH(s.horario), MONTH(s.horario), HOUR(s.horario), MINUTE(s.horario), s.n_reservados, c.capacity FROM slots s, cantinas c, menu m WHERE s.id_cantina = c.id AND s.id_menu = m.id AND DAYOFMONTH(s.horario) = "+reserva.getDay()+" AND MONTH(s.horario) = "+reserva.getMonth()+"  AND c.id = "+reserva.getPoiID()+" AND m.id = "+reserva.getMeal().getId());
			resultSet = preparedStatement.executeQuery();

			List<Slot> slots = new ArrayList<Slot>();
			
			while(resultSet.next()) {
				Slot slot = new Slot();
				
				slot.setIdSlot(resultSet.getString(1));
				slot.setDay(resultSet.getInt(2));
				slot.setMonth(resultSet.getInt(3));
				slot.setHour(resultSet.getInt(4));
				slot.setMinute(resultSet.getInt(5));
				
				int reservados = resultSet.getInt(6);
				int capacidade = resultSet.getInt(7);
				int available = capacidade - reservados;
				
				slot.setReservados(reservados);
				slot.setAvailability(available);
				
				if(available > 0)
					slot.setAvailable(true);
				else
					slot.setAvailable(false);
				
				slots.add(slot);
			}
			
			reserva.setSlots(slots);

		} catch(Exception e) {
			throw e;
		}

		return reserva;
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