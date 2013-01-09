package rs.deibogus.server.socialmanager;

import java.util.ArrayList;

import rs.deibogus.shared.Album;
import rs.deibogus.shared.Foto;
import rs.deibogus.shared.SessionData;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public interface ISocialManagerImplementor {
	public Foto getPhoto(String id);
	public Album getAlbum(String id);
	public void removePhoto(Foto foto);
	public void removeAlbum(String id);
	public ArrayList<Foto> getAllPhotos(SessionData session);
	public void uploadPhoto(Foto foto);
}
