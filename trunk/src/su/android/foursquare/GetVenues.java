package su.android.foursquare;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

public class GetVenues {
	private String client_id = "GWFTFZVMNE5PS23HUCTWK40F302J1RJGARFPDYARD5U3HBDY";
	private String client_secret = "CYQDKEKPLWF4PVEYSRYKO2QA4WSIGHZH3RPDZEKTQYXTS4WH";
	private String callback_url = "http://localhost:8080/foursquare/";
	
	private FoursquareApi foursquareApi;
	
	private HashMap<String, CompactVenue> venues = new HashMap<String, CompactVenue>();
	
	private static GetVenues myInstance = null;
	
	public static GetVenues getInstance()
	{
		if(myInstance == null)
		{
			myInstance = new GetVenues();			
		}
		return myInstance;
	}
	
	public GetVenues() {
		// Initialize FoursquareApi
		foursquareApi = new FoursquareApi(client_id, client_secret, callback_url);
	}

	public void getAllVenues() throws FoursquareApiException {
		ArrayList<String> locations = this.getLocations();

		for(String ll : locations) {
			this.getVenues(ll);
		}
		
		//TODO guardar venues no mongo -> ID, nome, stats
		for(CompactVenue poi : venues.values()) {
			
		}
	}

	public void getVenues(String ll) throws FoursquareApiException {		
		
		
		Result<VenuesSearchResult> result = foursquareApi.venuesSearch(ll, null, null, null, null, 500, "checkin", null, null, null, null);

		
		
		if (result.getMeta().getCode() == 200) {		
			// if query was ok we can finally we do something with the data
			for (CompactVenue venue : result.getResult().getVenues()) {
				System.out.println(venue.getName());
				
				for(int i=0; i<venue.getCategories().length; i++) {
					System.out.println("Cat: "+venue.getCategories()[0].getIcon());
				}

				venues.put(venue.getId(), venue);
			}
			System.out.println("Total Dif: "+venues.size());
		} else {
			System.out.println("Error occured: ");
			System.out.println("  code: " + result.getMeta().getCode());
			System.out.println("  type: " + result.getMeta().getErrorType());
			System.out.println("  detail: " + result.getMeta().getErrorDetail()); 
		}
	}

	private ArrayList<String> getLocations() {
		ArrayList<String> locations = new ArrayList<String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("locations"));
			
			String line;
			while((line = in.readLine()) != null) {
				locations.add(line);
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return locations;
	}

	public static void main(String[] args) {
		GetVenues v = new GetVenues();
		try {
			String ll = "40.2072,-8.426428";
			v.getVenues(ll);
		} catch (FoursquareApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
