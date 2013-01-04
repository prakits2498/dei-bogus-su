package rs.deibogus.server.socialmanager;

import rs.deibogus.shared.Album;
import rs.deibogus.shared.Photo;

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
	public Photo getPhoto(String id) {
		return this.implementor.getPhoto(id);
	}

	@Override
	public Album getAlbum(String id) {
		return this.implementor.getAlbum(id);
	}

	@Override
	public void removePhoto(String id) {
		this.implementor.removePhoto(id);
	}

	@Override
	public void removeAlbum(String id) {
		this.implementor.removeAlbum(id);
	}
	
	
}
