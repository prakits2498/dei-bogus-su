package su.server.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name="IPoisWebService", targetNamespace="http://iPois/")
public interface IPoisWebService {

	@WebMethod(operationName="getPOIRecommendations")
	public String getPOIRecommendations(String request);
	
	@WebMethod(operationName="checkEvents")
	public String checkEvents(String request);

	@WebMethod(operationName="verifyLogin")
	public String verifyLogin(String request);
	
	@WebMethod(operationName="getMenuDetails")
	public String getMenuDetails(String poiId);
	
	@WebMethod(operationName="getMenuFromReservations")
	public String getMenuFromReservations(String request);
	
	@WebMethod(operationName="getCredits")
	public String getCredits(String request);
	
	@WebMethod(operationName="getSlots")
	public String getSlots(String request);
	
	@WebMethod(operationName="makeReservation")
	public String makeReservation(String request);
	
	@WebMethod(operationName="actualizaCreditos")
	public String actualizaCreditos(String userID, String credits);
	
}
