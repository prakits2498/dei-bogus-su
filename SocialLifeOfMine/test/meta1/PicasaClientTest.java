package meta1;

import static org.junit.Assert.*;

import org.junit.Test;

import picasa.PicasaClient;

import com.google.gdata.client.photos.PicasawebService;

public class PicasaClientTest {

	@Test
	public void testGetAlbumsString() {
		String username = "";
		String password = "";
		PicasawebService service = new PicasawebService("exampleClient");
		PicasaClient picasa = new PicasaClient(service, username, password);
		
		
		fail("Not yet implemented");
	}

	@Test
	public void testGetAlbums() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTagsString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTags() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPhotos() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetComments() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTagsGphotoEntryOfQ() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertAlbum() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFeed() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddKindParameter() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinkByRel() {
		fail("Not yet implemented");
	}

}
