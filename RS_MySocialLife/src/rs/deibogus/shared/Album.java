package rs.deibogus.shared;

import java.util.HashMap;

public class Album {

	private String id;
	private HashMap<String, Photo> photos;
	
	public Album(String id, HashMap<String, Photo> photos) {
		this.id = id;
		this.photos = photos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HashMap<String, Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(HashMap<String, Photo> photos) {
		this.photos = photos;
	}
	
	public void addPhoto(Photo photo) {
		this.photos.put(photo.getId(), photo);
	}
	
	public void removePhoto(Photo photo) {
		this.photos.remove(photo.getId());
	}
}
