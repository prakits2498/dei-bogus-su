package su.server.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name="IPoisWebService", targetNamespace="http://iPois/")
public interface IPoisWebService {

	@WebMethod(operationName="getPOIRecommendations")
	public String getPOIRecommendations(String request);
	
	/*@WebMethod(operationName="getPOIDetails")
	public String getPOIDetails(String poiId);
	
	@WebMethod(operationName="searchPOIS")
	public String searchPois(String poiId);
	
	@WebMethod(operationName="getPOIS")
	public String getPois(String request);*/
	
}