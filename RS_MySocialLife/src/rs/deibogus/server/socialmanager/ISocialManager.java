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
public interface ISocialManager {
	public Foto getPhoto(String id);
	public void removePhoto(Foto foto);
	public ArrayList<Foto> getAllPhotos(SessionData session);
	public void uploadPhoto(Foto foto, File ficheiro);
}
