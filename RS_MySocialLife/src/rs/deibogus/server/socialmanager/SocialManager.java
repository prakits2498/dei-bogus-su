package rs.deibogus.server.socialmanager;

import java.io.File;
import java.util.ArrayList;

import rs.deibogus.shared.Foto;
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
	public void removePhoto(Foto foto) {
		this.implementor.removePhoto(foto);
	}
	
	@Override
	public ArrayList<Foto> getAllPhotos(SessionData session){
		return this.implementor.getAllPhotos(session);
	}

	@Override
	public void uploadPhoto(Foto foto, File ficheiro) {
		this.implementor.uploadPhoto(foto, ficheiro);
	}
	
	
}
