package rs.deibogus.server.socialmanager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rs.deibogus.server.login.PicasaLogin;
import rs.deibogus.shared.Album;
import rs.deibogus.shared.Foto;
import rs.deibogus.shared.Photo;
import rs.deibogus.shared.SessionData;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Link;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.ServiceException;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class PicasaManager implements ISocialManagerImplementor {

	private static final String API_PREFIX = "https://picasaweb.google.com/data/feed/api/user/";
	private final PicasawebService service;

	public PicasaManager(PicasawebService service) {
		this.service = service;
	}

	@Override
	public Foto getPhoto(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Foto> getAllPhotos(SessionData session) {

		ArrayList<Foto> photos = new ArrayList<Foto>();
		URL albumFeedUrl;
		try {
			albumFeedUrl = new URL(API_PREFIX + session.getPicasaUsername() + "?kind=album");
			UserFeed myUserFeed = service.getFeed(albumFeedUrl, UserFeed.class);
			System.out.println(myUserFeed.getAlbumEntries().size());

			for (AlbumEntry myAlbum : myUserFeed.getAlbumEntries()) {

				System.out.println(myAlbum.getId());
				String temp[] = myAlbum.getId().split("/");
				System.out.println(temp[0] + temp[1] + temp[2] + temp[7]);
				URL photosFeedUrl = new URL(API_PREFIX + session.getPicasaUsername() + "/albumid/" + temp[8]);
				

				AlbumFeed feed = service.getFeed(photosFeedUrl, AlbumFeed.class);

				for(PhotoEntry p : feed.getPhotoEntries()) {
					Foto photo = new Foto("Picasa",p.getId(), p.getMediaContents().get(0).getUrl(), p.getMediaThumbnails().get(0).getUrl(), p.getAlbumId(), p.getTitle().getPlainText(), p.getWidth(), p.getHeight());
					System.out.println(photo.getTitle());
					photos.add(photo);
				}

				System.out.println(myAlbum.getTitle().getPlainText());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return photos;

		//		String feedHref = getLinkByRel(album.getLinks(), Link.Rel.FEED);
		//		AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);
		//
		//		List<GphotoEntry> entries = albumFeed.getEntries();
		//		List<Foto> photos = new ArrayList<Foto>();
		//		for (GphotoEntry entry : entries) {
		//			GphotoEntry adapted = entry.getAdaptedEntry();
		//			if (adapted instanceof PhotoEntry) {
		//				PhotoEntry p = (PhotoEntry) adapted;
		//				Foto photo = new Foto(p.getId(), p.getMediaContents().get(0).getUrl(), p.getAlbumId(), p.getTitle().getPlainText(), p.getWidth(), p.getHeight(), p.getSize());
		//				photos.add(photo);
		//			}
		//		}
		//		return photos;
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
