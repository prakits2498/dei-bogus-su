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

import su.android.model.ConfirmationData;
import su.android.model.DayEventsRequest;
import su.android.model.Login;
import su.android.model.MenuDetails;
import su.android.model.MonthlyEventsRequest;
import su.android.model.POI;
import su.android.model.POIList;
import su.android.model.Reserva;
import android.util.Log;

import com.google.gson.Gson;

public class ServerConnection 
{	
	private static final String NAMESPACE = "http://iPois/";
	//private static final String URL = "http://ipois.dei.uc.pt:8080/su-server/ws?wsdl";
	//private static final String URL = "http://localhost:80/su-server/ws?wsdl";
	//private static final String URL = "http://ipois.dei.uc.pt:8080/su-server/ws?wsdl";
	//private static final String URL = "http://localhost:8083/su-server/ws?wsdl";
	//private static final String URL = "http://193.136.207.29:8080/su-server/ws?wsdl";
	private static final String URL = "http://10.16.0.3:8080/su-server/ws?wsdl"; //BB
	//private static final String URL = "http://10.16.0.196:8080/su-server/ws?wsdl"; //DURVAS


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

	public List<POI> getPOIRecommendations(String dayOfWeek, int hour, int limit)
	{
		POIList poiList = null;
		String method = "getPOIRecommendations";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		RecommendationRequest req = new RecommendationRequest();
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
			Log.e("error", ">>>> IOException!!"+e.getMessage());
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

	public MenuDetails getMenuDetails(String poiId)
	{
		String method = "getMenuDetails";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);		
		PropertyInfo param1 = new PropertyInfo();
		param1.setName("arg0");
		param1.setValue(poiId);
		param1.setType(PropertyInfo.STRING_CLASS);
		soapRequest.addProperty(param1);
		soapEnvelope.setOutputSoapObject(soapRequest);
		MenuDetails menuDetails = null;
		try 
		{
			httpTransport.call(NAMESPACE+method, soapEnvelope);
			String result = soapEnvelope.getResponse().toString();
			menuDetails = (MenuDetails) gson.fromJson(result, MenuDetails.class);
		} 
		catch (IOException e) 
		{
			Log.e("error", "IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return menuDetails;		
	}

	public Reserva getSlots(Reserva reserva) {
		String method = "getSlots";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		String request = gson.toJson(reserva);
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
			reserva = (Reserva) gson.fromJson(result, Reserva.class);
		} 
		catch (IOException e) 
		{
			Log.e("error", "IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return reserva;		
	}
	
	public ConfirmationData getConfirmationData(ConfirmationData data) {
		ConfirmationData resultado=null;
		String method = "getConfirmationData";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		String request = gson.toJson(data);
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
			resultado = (ConfirmationData) gson.fromJson(result, ConfirmationData.class);
		} 
		catch (IOException e) 
		{
			Log.e("error", "IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return resultado;		
	}
	
	public boolean makeReservationSlots(Reserva reserva) {
		boolean resultado=true;
		String method = "makeReservation";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		String request = gson.toJson(reserva);
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
			resultado = (boolean) gson.fromJson(result, boolean.class);
		} 
		catch (IOException e) 
		{
			Log.e("error", "IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return resultado;		
	}
	
	public boolean actualizaCreditos(String userID, String credits) {
		boolean resultado=true;
		String method = "actualizaCreditos";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		
		PropertyInfo param1 = new PropertyInfo();
		param1.setName("arg0");
		param1.setValue(userID);
		param1.setType(PropertyInfo.STRING_CLASS);
		
		PropertyInfo param2 = new PropertyInfo();
		param2.setName("arg1");
		param2.setValue(credits);
		param2.setType(PropertyInfo.STRING_CLASS);
		
		soapRequest.addProperty(param1);
		soapRequest.addProperty(param2);
		
		soapEnvelope.setOutputSoapObject(soapRequest);
		
		try 
		{
			httpTransport.call(NAMESPACE+method, soapEnvelope);
			String result = soapEnvelope.getResponse().toString();
			resultado = (boolean) gson.fromJson(result, boolean.class);
		} 
		catch (IOException e) 
		{
			Log.e("error", "IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return resultado;	
	}
	
	public int verifyLogin (Login login){
		int res=-1;
		String method = "verifyLogin";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		String request = gson.toJson(login);
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
			res = (Integer)gson.fromJson(result, Integer.class);
			Log.i("SERVER LOGIN VERIFICATION", "[Result: " + res + "]");
		} 
		catch (IOException e) 
		{
			Log.e("error", ">>>> IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return res;
	}
	
	public double getCredits(int idUser){
		double res=-1;
		String method = "getCredits";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		String request = gson.toJson(idUser);
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
			res = (Double)gson.fromJson(result, Double.class);
			Log.i("USER CREDITS", "[Result: " + res + " do user: " + idUser + "]");
		} 
		catch (IOException e) 
		{
			Log.e("error", ">>>> IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return res;
	}

	public DayEventsRequest getMenuFromReservations (DayEventsRequest context){
		DayEventsRequest events = null;

		String method = "getMenuFromReservations";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		String request = gson.toJson(context);
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
			events = (DayEventsRequest)gson.fromJson(result, DayEventsRequest.class);
			if(events.getLunchEvents() != null)
				Log.i("MONTHLY EVENTS VERIFICATION", "[Result do day one: " + events.getLunchEvents() + "]");
		} 
		catch (IOException e) 
		{
			Log.e("error", ">>>> IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return events;

	}

	public MonthlyEventsRequest checkEvents(MonthlyEventsRequest context){
		//HashMap<String,Integer> lista_eventos = new HashMap<String, Integer>();
		MonthlyEventsRequest events = null;

		String method = "checkEvents";
		SoapObject soapRequest = new SoapObject(NAMESPACE, method);
		String request = gson.toJson(context);
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
			events = (MonthlyEventsRequest)gson.fromJson(result, MonthlyEventsRequest.class);
			if(events.getListEvents() != null)
				Log.i("MONTHLY EVENTS VERIFICATION", "[Result do day one: " + events.getListEvents() + "]");
		} 
		catch (IOException e) 
		{
			Log.e("error", ">>>> IOException!!"+e.getMessage());
		} 
		catch (XmlPullParserException e) 
		{
			Log.e("error", "XMLPullParserException!!"+e.getMessage());
		}

		return events;
	}

}
