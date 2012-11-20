package flickr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class FlickrClient {
	private static final boolean DEBUG = false;
	private static final String apiKey = "32eff8810bfb81ddea86b6d50e6e5fe8";
	
	public static void main(String[] args) {
		FlickrClient f = new FlickrClient();
		String id = f.getIdUser("sly-cooper");
		f.getPhotos(id);
	}

	public void getPhotos(String idUser){
		URL u;
		HttpURLConnection uc;
		
		try {
			u = new URL("http://api.flickr.com/services/rest/?method=flickr.people.getPublicPhotos&api_key=" + apiKey + "&user_id=" + idUser + "&format=json&extras=url_m");
			uc = (HttpURLConnection) u.openConnection();

			BufferedReader rd = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			
			if(DEBUG)
				System.out.println(sb.toString());

			sb = sb.delete(0, 14);
			sb = sb.deleteCharAt(sb.length() - 1);
			
			JSONObject json = (JSONObject) new JSONParser().parse(sb.toString());
			JSONObject photos = (JSONObject) json.get("photos");
			JSONArray menuitemArray = (JSONArray)photos.get("photo");
			
			for(int i=0;i<menuitemArray.size();i++){
				JSONObject temp = (JSONObject) menuitemArray.get(i);
				System.out.println(temp.get("url_m"));
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		


	}

	public String getIdUser(String username){
		URL u;
		String idUser = "";

		try {
			u = new URL("http://api.flickr.com/services/rest/?method=flickr.people.findByUsername&api_key=32eff8810bfb81ddea86b6d50e6e5fe8&format=json&username=" + username);
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(uc.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			
			if(DEBUG)
				System.out.println(sb.toString());
			
			sb = sb.delete(0, 14);
			sb = sb.deleteCharAt(sb.length() - 1);
			
			JSONObject json = (JSONObject) new JSONParser().parse(sb.toString());
			JSONObject user = (JSONObject) json.get("user");
			idUser = (String) user.get("nsid");
			
			//System.out.println("idUser=" + idUser);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}


		return idUser;
	}


}