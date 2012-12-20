package rs.deibogus.shared;

public class Photo {

	private String id;
	private String url;
	private String albumId;
	private String title;
	private long width;
	private long height;
	private long size;
	
	public Photo(String id, String url, String albumId, String title, long width, long height, long size) {
		this.id = id;
		this.url = url;
		this.albumId = albumId;
		this.title = title;
		this.width = width;
		this.height = height;
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

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
