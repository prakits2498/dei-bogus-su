package su.server.ws;

import javax.jws.WebService;

import su.server.ws.DB.MySQLAccess;
import su.server.ws.model.ConfirmationData;
import su.server.ws.model.DayEventsRequest;
import su.server.ws.model.Login;
import su.server.ws.model.MenuDetails;
import su.server.ws.model.MonthlyEventsRequest;
import su.server.ws.model.POIList;
import su.server.ws.model.Reserva;

import com.google.gson.Gson;

/**
 * Represent a web service surrogate for the Server API, based on the Proxy
 * Design Pattern.
 * 
 */
@WebService(portName = "WSPort", serviceName = "iPoisService", targetNamespace = "http://iPois/", endpointInterface = "su.server.ws.IPoisWebService")
public class PoisWebService implements IPoisWebService 
{
	//private Server server;
	private MySQLAccess Db;

	public PoisWebService() 
	{
		//this.server = new Server();
		this.Db = new MySQLAccess(); 
	}

	public String getPOIRecommendations(String request)
	{			
		Gson gson = new Gson();
		POIList poiList = null;
		try {
			poiList = Db.getPOIs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gson.toJson(poiList);
	}
	
	public String getConfirmationData(String request)
	{			
		Gson gson = new Gson();
		ConfirmationData data = (ConfirmationData)gson.fromJson(request, ConfirmationData.class);;
		try {
			data = Db.getConfirmationData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gson.toJson(data);
	}

	public String verifyLogin(String request) {
		Gson gson = new Gson();
		Login login = (Login)gson.fromJson(request, Login.class);

		int result=-1;

		try {
			result = Db.verifyLogin(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(result);
	}
	
	public String makeReservation(String request){
		Gson gson = new Gson();
		Reserva reserva = (Reserva)gson.fromJson(request, Reserva.class);

		boolean result=true;

		try {
			result = Db.makeReservation(reserva);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(result);
	}
	
	public String actualizaCreditos(String userID, String credits) {
		Gson gson = new Gson();
		boolean result=true;

		try {
			result = Db.actualizaCreditos(userID, credits);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(result);
	}
	
	public String getCredits(String request) {
		Gson gson = new Gson();
		int idUser = (int)gson.fromJson(request, Integer.class);

		double result=-1;

		try {
			result = Db.getCredits(idUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(result);
	}
	
	public String checkEvents(String request){
		Gson gson = new Gson();
		MonthlyEventsRequest req = (MonthlyEventsRequest)gson.fromJson(request, MonthlyEventsRequest.class);
		MonthlyEventsRequest response = null;
		try {
			response = Db.checkEvents(req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gson.toJson(response);
	}
	
	public String getMenuFromReservations(String request){
		Gson gson = new Gson();
		DayEventsRequest eventsReq = (DayEventsRequest)gson.fromJson(request, DayEventsRequest.class);
		
		try {
			eventsReq = Db.getMenuFromReservations(eventsReq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(eventsReq);
	}
	
	public String getMenuDetails(String poiId)
	{
		Gson gson = new Gson();
		MenuDetails menuDetails = null;
		
		try {
			menuDetails = Db.getMenuDetails(poiId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(menuDetails);
	}

	public String getSlots(String request) {
		Gson gson = new Gson();
		Reserva reserva = (Reserva) gson.fromJson(request, Reserva.class);
		
		try {
			reserva = Db.getSlots(reserva);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(reserva);
		
	}
	
}
