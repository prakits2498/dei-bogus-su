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

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import su.server.foursquare.db.model.POI;
import su.server.foursquare.db.model.POIList;

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
	
	public PoisWebService() 
	{
		//this.server = new Server();
	}

	public String getPOIRecommendations(String request)
	{				
		Gson gson = new Gson();
		//RecommendationRequest recommendationReq = (RecommendationRequest)gson.fromJson(request, RecommendationRequest.class);
		//System.out.println("GetPOIRecommendations [lat:"+recommendationReq.getLat()+", lng:"+recommendationReq.getLng()+"]");
		//double lat = recommendationReq.getLat();
		//double lng = recommendationReq.getLng();
		//int hour = recommendationReq.getHour();
		//String dayOfWeek = recommendationReq.getDayOfWeek();
		//Double maxDistance = recommendationReq.getMaxDistance();
		//int limit = recommendationReq.getLimit();
		//POIList poiList = this.server.searchNear(lat, lng, hour, dayOfWeek, maxDistance, limit);				
		
		List<POI> cantinas = new ArrayList<POI>();
		POI cantina = new POI();
		cantina.setId("1");
		cantina.setLocation(40.220200, -8.417983);
		cantina.setName("POLOII");
		cantinas.add(cantina);
		
		POI c = new POI();
		c.setId("2");
		c.setLocation(40.208572, -8.421364);
		c.setName("CASA DA PEDRA");
		cantinas.add(c);
		
		POIList poiList = new POIList();
		poiList.setPoiList(cantinas);
		
		return gson.toJson(poiList);
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
