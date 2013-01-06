package rs.deibogus.server.socialmanager;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.Size;
import com.aetrion.flickr.photosets.Photoset;
import com.aetrion.flickr.photosets.Photosets;

import rs.deibogus.shared.Album;
import rs.deibogus.shared.Foto;
import rs.deibogus.shared.SessionData;


/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class FlickrManager implements ISocialManagerImplementor {

	private final Auth auth;
	private Flickr f;
	private static String apiKey = "32eff8810bfb81ddea86b6d50e6e5fe8";
	private static String secret = "36ae9ccecbf2b1b3";
	private RequestContext requestContext;
	
	public FlickrManager(Auth auth) throws ParserConfigurationException {
		this.auth = auth;
		f = new Flickr(apiKey,secret,new REST());
		requestContext = RequestContext.getRequestContext();
		requestContext.setAuth(auth);
	}
	
	
	@Override
	public Foto getPhoto(String id) {
		// TODO Auto-generated method stub
		return null;
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
		
		
	}
	
	@Override
	public ArrayList<Foto> getAllPhotos(SessionData session){
		// TODO Auto-generated method stub
		ArrayList<Foto> result = new ArrayList<Foto>();

		
		Photosets albuns;
		try {
			albuns = f.getPhotosetsInterface().getList(auth.getUser().getId());
			System.out.println("nº albuns = " + albuns.getPhotosets().size());
			
			for (Object a : albuns.getPhotosets()){
				Photoset album = (Photoset) a;
				PhotoList lista = f.getPhotosetsInterface().getPhotos(album.getId(), 10, 0); //metadata das fotos. Para obter foto PhotosInterface.getImage(Photo, int)
				for(Object b : lista){
					Photo foto = (Photo) b;
					result.add(new Foto("Flickr",foto.getId(),foto.getUrl(),foto.getThumbnailUrl(),album.getId(), foto.getTitle(), foto.getOriginalWidth(),foto.getOriginalHeight()));//se nao bater certo, testar get original URL
					//f.getPhotosInterface().getImage(foto, Size.ORIGINAL);//Há varios tamanhos possivels, ver doc
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}

}
