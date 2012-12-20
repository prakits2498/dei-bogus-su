package rs.deibogus.server.socialmanager;

import rs.deibogus.shared.Album;
import rs.deibogus.shared.Photo;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public interface ISocialManager {
	public Photo getPhoto(String id);
	public Album getAlbum(String id);
	public void removePhoto(String id);
	public void removeAlbum(String id);
}
