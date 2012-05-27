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
import su.android.model.Products;
import su.android.model.Promotions;
import android.util.Log;

import com.google.gson.Gson;

public class ServerConnection 
{	
	private static final String NAMESPACE = "http://iPois/";
	private static final String URL = "http://ipois.dei.uc.pt:8080/su-server/ws?wsdl";
	
	private SoapSerializationEnvelope soapEnvelope;
	private AndroidHttpTransport httpTransport;
	private Gson gson;
	
	private static ServerConnection myInstance = null;
	
	public static ServerConnection getInstance() 
	{
		if(myInstance == null)
		{
			myInstance = new ServerConnection();			
		}
		return myInstance;
	}
	
	public ServerConnection()
	{
		httpTransport = new AndroidHttpTransport(URL);
		soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		gson = new Gson();
	}
	
	public List<POI> getPOIRecommendations(double lat, double lng, String dayOfWeek, int hour, double maxDistance, int limit)
	{
		POIList poiList = null;
		String method = "getPOIRecommendations";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		RecommendationRequest req = new RecommendationRequest();
		req.setLat(lat);
		req.setLng(lng);
		req.setMaxDistance(maxDistance);
		req.setLimit(limit);
		req.setDayOfWeek(dayOfWeek);
		req.setHour(hour);
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
			Log.i("SERVER REQUEST(RECOMMENDATION)", "[POIS: "+poiList.getPoiList().size()+"]");
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
			return poiList.getPoiList();
		}
		else
		{
			return new ArrayList<POI>();
		}
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
			Log.i("SERVER REQUEST(RECOMMENDATION)", "[POIS: "+poiList.getPoiList().size()+"]");
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
		
		if(poiDetails.getPromotionList() != null && poiDetails.getProductList() != null)
		{
			return poiDetails;
		}
		else
		{
			poiDetails = new POIDetails();
			
			List<Products> products = new ArrayList<Products>();
			Products p1 = new Products();
			p1.setId("1");
			p1.setName("Gelado Morango");
			p1.setPrice("2€");
			p1.setID_Catgory("1");
			p1.setID_SubCatgory("1");
			p1.setID_Merchant("1");
			p1.setDescription("Gelado muita bom com sabor a morango");
			p1.setImage("http://www.haagen-dazs.com/img_db/pro/pro_sti_101.jpg");
			products.add(p1);
			
			Products p2 = new Products();
			p2.setId("2");
			p2.setName("Pastel de Nata");
			p2.setPrice("0.5€");
			p2.setID_Catgory("1");
			p2.setID_SubCatgory("1");
			p2.setID_Merchant("1");
			p2.setDescription("Pastel de Nata espectacular com canela");
			p2.setImage("http://2.bp.blogspot.com/_oBsXNzbpvUM/TSpCQI7deVI/AAAAAAAAF2A/jrWotSklRnY/s1600/p.nata.jpg");
			products.add(p2);
			
			poiDetails.setProductList(products);
			
			List<Promotions> promotions = new ArrayList<Promotions>();
			Promotions promo = new Promotions();
			promo.setDescription("Finos a 0.50€ a partir das 2h!!!");
			promotions.add(promo);
			
			poiDetails.setPromotionList(promotions);
			
			return poiDetails;
		}
	}
	
	public List<POI> searchPOIS(String query)
	{
		POIList poiList = null;
		String method = "searchPOIS";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);		
		PropertyInfo param1 = new PropertyInfo();
		param1.setName("arg0");
		param1.setValue(query);
		param1.setType(PropertyInfo.STRING_CLASS);
		soapRequest.addProperty(param1);
		soapEnvelope.setOutputSoapObject(soapRequest);
		try 
		{
			httpTransport.call(NAMESPACE+method, soapEnvelope);
			String result = soapEnvelope.getResponse().toString();
			poiList = (POIList)gson.fromJson(result, POIList.class);
			Log.i("SERVER REQUEST(SEARCH)", "[POIS: "+poiList.getPoiList().size()+"]");
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
	
}
