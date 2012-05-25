package su.android.server.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import su.android.model.POI;
import su.android.model.POIDetails;
import su.android.model.POIList;
import android.util.Log;

import com.google.gson.Gson;

public class ServerConnection 
{	
	private static final String NAMESPACE = "http://iPois/";
	private static final String URL = "http://ipois.dei.uc.pt:8080/su-server/ws?wsdl";
	
	private SoapSerializationEnvelope soapEnvelope;
	private AndroidHttpTransport httpTransport;
	private Gson gson;
	
	public ServerConnection()
	{
		httpTransport = new AndroidHttpTransport(URL);
		soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		gson = new Gson();
	}
	
	public List<POI> getPOIRecommendations(double lat, double lng, double maxDistance, int limit)
	{
		POIList poiList = null;
		String method = "getPOIRecommendations";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		RecommendationRequest req = new RecommendationRequest();
		req.setLat(lat);
		req.setLng(lng);
		req.setMaxDistance(maxDistance);
		req.setLimit(limit);
		String request = gson.toJson(req);
		PropertyInfo param1 = new PropertyInfo();
		param1.setName("arg0");
		param1.setValue(request);
		param1.setType(PropertyInfo.STRING_CLASS);
		soapRequest.addProperty(param1);
		soapEnvelope.setOutputSoapObject(soapRequest);
		try 
		{
			httpTransport.call(NAMESPACE+method, soapEnvelope);
			String result = soapEnvelope.getResponse().toString();
			poiList = (POIList)gson.fromJson(result, POIList.class);
		} 
		catch (IOException e) 
		{
			Log.e("error", "IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}
		if(poiList != null)
		{
			for(POI poi: poiList.getPoiList())
			{
				Log.i("POI", poi.toString());
			}
			return poiList.getPoiList();
		}
		else
		{
			return new ArrayList<POI>();
		}
	}
	
	public POIDetails getPOIDetails(String poiId)
	{
		String method = "getPOIDetails";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);		
		PropertyInfo param1 = new PropertyInfo();
		param1.setName("arg0");
		param1.setValue(poiId);
		param1.setType(PropertyInfo.STRING_CLASS);
		soapRequest.addProperty(param1);
		soapEnvelope.setOutputSoapObject(soapRequest);
		POIDetails poiDetails = null;
		try 
		{
			httpTransport.call(NAMESPACE+method, soapEnvelope);
			String result = soapEnvelope.getResponse().toString();
			poiDetails = (POIDetails)gson.fromJson(result, POIDetails.class);
		} 
		catch (IOException e) 
		{
			Log.e("error", "IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}
		return poiDetails;
	}
	
}
