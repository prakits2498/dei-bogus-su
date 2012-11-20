package testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import picasa.PicasaClient;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ServiceException;

import flickr.FlickrClient;

public class Main {

	private static final BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));
	private static Main m;

	public static void main(String[] args) {
		m = new Main();
		m.mainMenu();
	}

	public void mainMenu() {
		while (true) {
			System.out.println("0 - Exit");
			System.out.println("1 - Login to Picasa");
			System.out.println("2 - Login to Flickr");
			System.out.print("Option: ");
			try {
				int op = Integer.parseInt(IN.readLine());

				switch(op) {
				case 0: return;
				case 1: m.loginPicasa(); break;
				case 2: m.loginFlickr(); break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loginPicasa() {
		try {
			System.out.println("Username: ");
			String username = IN.readLine();
			System.out.println("Password: ");
			String password = IN.readLine();

			PicasawebService service = new PicasawebService("exampleClient");
			PicasaClient picasa = new PicasaClient(service, username, password);

			showAlbums(picasa);

		} catch(IOException e) {
			e.printStackTrace();
		} 
	}

	public void showAlbums(PicasaClient picasa) {
		try {
			List<AlbumEntry> albums = picasa.getAlbums();
			if(albums.isEmpty())
				System.out.println("No albums found.");
			else {
				System.out.println("Found "+albums.size()+" albums.");
				for (AlbumEntry album : albums) {
					System.out.println("Album title: "+album.getTitle().getPlainText());

					showAlbumPhotos(picasa, album);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showAlbumPhotos(PicasaClient picasa, AlbumEntry album) {
		try {
			List<PhotoEntry> photos = picasa.getPhotos(album);
			if (photos.size() == 0) {
				System.out.println("\tNo photos found.");
			} else {
				System.out.println("\tFound "+photos.size()+" photos.");
				for (PhotoEntry photo : photos) {
					System.out.println("\tPhoto title: "+photo.getTitle().getPlainText());
					System.out.println("\tPhoto url: "+ photo.getMediaContents().get(0).getUrl());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void loginFlickr() {
		try {
			System.out.println("Username: ");
			String username = IN.readLine();
			System.out.println("Password: ");
			String password = IN.readLine();

			FlickrClient flickr = new FlickrClient();
			flickr.getPhotos(flickr.getIdUser(username));

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
