package rs.deibogus.server.socialmanager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rs.deibogus.server.login.PicasaLogin;
import rs.deibogus.shared.Album;
import rs.deibogus.shared.Photo;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Link;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ServiceException;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class PicasaManager implements ISocialManagerImplementor {

	private static final String API_PREFIX = "https://picasaweb.google.com/data/feed/api/user/";
	private final PicasawebService service;
	
	public PicasaManager() {
		this.service = PicasaLogin.getInstance().getPicasaService();
	}

	@Override
	public Photo getPhoto(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Photo> getPhotos(AlbumEntry album) throws IOException, ServiceException {

		String feedHref = getLinkByRel(album.getLinks(), Link.Rel.FEED);
		AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

		List<GphotoEntry> entries = albumFeed.getEntries();
		List<Photo> photos = new ArrayList<Photo>();
		for (GphotoEntry entry : entries) {
			GphotoEntry adapted = entry.getAdaptedEntry();
			if (adapted instanceof PhotoEntry) {
				PhotoEntry p = (PhotoEntry) adapted;
				Photo photo = new Photo(p.getId(), p.getMediaContents().get(0).getUrl(), p.getAlbumId(), p.getTitle().getPlainText(), p.getWidth(), p.getHeight(), p.getSize());
				photos.add(photo);
			}
		}
		return photos;
	}

	/**
	 * Helper function to get a link by a rel value.
	 */
	private String getLinkByRel(List<Link> links, String relValue) {
		for (Link link : links) {
			if (relValue.equals(link.getRel())) {
				return link.getHref();
			}
		}
		throw new IllegalArgumentException("Missing " + relValue + " link.");
	}

	/**
	 * Helper function to allow retrieval of a feed by string url, which will
	 * create the URL object for you.  Most of the Link objects have a string
	 * href which must be converted into a URL by hand, this does the conversion.
	 */
	private <T extends GphotoFeed> T getFeed(String feedHref, Class<T> feedClass) throws IOException, ServiceException {
		System.out.println("Get Feed URL: " + feedHref);
		return service.getFeed(new URL(feedHref), feedClass);
	}

	@Override
	public Album getAlbum(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removePhoto(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAlbum(String id) {
		// TODO Auto-generated method stub

	}

}
