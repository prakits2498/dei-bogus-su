package rs.deibogus.shared;

import java.io.Serializable;

public class Foto implements Serializable{

	private String id;
	private String url;
	private String thumbnailUrl;
	private String albumId;
	private String title;
	private long width;
	private long height;
	//private long size;
	private String network;
	private String path;

	public Foto(String network, String id, String url, String thumbnailUrl, String albumId, String title, long width, long height) {
		this.network = network;
		this.id = id;
		this.url = url;
		this.albumId = albumId;
		this.title = title;
		this.width = width;
		this.height = height;
		this.thumbnailUrl = thumbnailUrl;
	}

	public Foto(String network, String title){
		this.network = network;
		this.title = title;
	}

	public Foto(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	//	public long getSize() {
	//		return size;
	//	}
	//
	//	public void setSize(long size) {
	//		this.size = size;
	//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


}
