/**
 * SDiC (Software Development in Context)
 *
 * Knowledge and Intelligent Systems Laboratory
 * Cognitive and Media Systems Group
 * Centre for Informatics and Systems of the University of Coimbra
 *
 * Copyright (c) 2010-2011 University of Coimbra
 * All rights reserved.
 * 
 */

package su.server.ws;

import javax.jws.WebService;

import su.server.ws.DB.MySQLAccess;
import su.server.ws.model.Login;
import su.server.ws.model.POIList;

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
	
	public String verifyLogin(String request) {
		Gson gson = new Gson();
		Login login = (Login)gson.fromJson(request, Login.class);
		boolean result = false;
		
		try {
			result = Db.verifyLogin(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(result);
	}
	
	/*public String getPOIDetails(String poiId)
	{
		Gson gson = new Gson();
		POIDetails poiDetails = this.server.getPOIDetails(poiId);
		String response = gson.toJson(poiDetails);
		return response;
	}

	@Override
	public String searchPois(String query) {
		Gson gson = new Gson();
		POIList poiList = this.server.search(query);				
		return gson.toJson(poiList);
	}

	@Override
	public String getPois(String request) {
		Gson gson = new Gson();
		RecommendationRequest recommendationReq = (RecommendationRequest)gson.fromJson(request, RecommendationRequest.class);
		int hour = recommendationReq.getHour();
		String dayOfWeek = recommendationReq.getDayOfWeek();
		int limit = recommendationReq.getLimit();
		POIList poiList = this.server.getPOIList(hour, dayOfWeek, limit);				
		return gson.toJson(poiList);
	}*/
}
