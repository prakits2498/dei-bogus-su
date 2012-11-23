package meta1;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import picasa.PicasaClient;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.util.ServiceException;

public class PicasaClientTest {

	@Test
	public void testGetAlbumsString() {
		String username = "thethis";
		String password = "";
		PicasawebService service = new PicasawebService("exampleClient");
		PicasaClient picasa = new PicasaClient(service, username, password);

		try {
			assertEquals(4,picasa.getAlbums(username).size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//fail("Not yet implemented");
	}

	//	@Test
	//	public void testGetAlbums() {
	//		
	//		fail("Not yet implemented");
	//	}

	//	@Test
	//	public void testGetTagsString() {
	//		
	//		fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	public void testGetTags() {
	//		fail("Not yet implemented");
	//	}

	@Test
	public void testGetPhotos() {
		String username = "thethis";
		String password = "";
		PicasawebService service = new PicasawebService("exampleClient");
		PicasaClient picasa = new PicasaClient(service, username, password);


		try {
			List<AlbumEntry> albums = picasa.getAlbums();
			for (AlbumEntry album : albums) {
				assertNotSame(null, picasa.getPhotos(album).size());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//fail("Not yet implemented");
	}

	

//	@Test
//	public void testGetComments() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetTagsGphotoEntryOfQ() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testInsertAlbum() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testInsert() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFeed() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddKindParameter() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetLinkByRel() {
//		fail("Not yet implemented");
//	}

}
