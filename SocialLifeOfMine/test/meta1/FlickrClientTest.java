package meta1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import flickr.FlickrClient;

public class FlickrClientTest {

	@Test
	public void testGetPhotos() {
		FlickrClient f = new FlickrClient();
		String userId="89988741@N07";
		assertEquals(2, f.getPhotos(userId)); //sei que este user tem 2, doutra forma é impossivel testar
		//fail("Not yet implemented");
	}

	@Test
	public void testGetIdUser() {
		FlickrClient f = new FlickrClient();
		String user="sly-cooper";
		assertEquals("89988741@N07",f.getIdUser(user));
		//fail("Not yet implemented");
	}

}
