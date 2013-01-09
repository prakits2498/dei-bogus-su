package rs.deibogus.server.socialmanager;

import java.util.ArrayList;

import rs.deibogus.shared.Album;
import rs.deibogus.shared.Foto;
import rs.deibogus.shared.Photo;
import rs.deibogus.shared.SessionData;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class SocialManager implements ISocialManager {

	private ISocialManagerImplementor implementor = null;
	
	public SocialManager(ISocialManagerImplementor imp) {
		this.implementor = imp;
	}

	@Override
	public Foto getPhoto(String id) {
		return this.implementor.getPhoto(id);
	}

	@Override
	public Album getAlbum(String id) {
		return this.implementor.getAlbum(id);
	}

	@Override
	public void removePhoto(Foto foto) {
		this.implementor.removePhoto(foto);
	}

	@Override
	public void removeAlbum(String id) {
		this.implementor.removeAlbum(id);
	}
	
	@Override
	public ArrayList<Foto> getAllPhotos(SessionData session){
		return this.implementor.getAllPhotos(session);
	}

	@Override
	public void uploadPhoto(Foto foto) {
		// TODO Auto-generated method stub
		this.implementor.uploadPhoto(foto);
	}
	
	
}
