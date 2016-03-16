package es.ubiqua.zapptv.manager.model;

public class AppItem {
	
	private String id;
	private String nombre;
	private String thumbnail;
	private String shortDescription;
	private String image;
	private String longDescription;
	private String urlclick;
	
	public AppItem(){
		
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getImage() {
		return image;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getUrlclick() {
		return urlclick;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public void setUrlclick(String urlclick) {
		this.urlclick = urlclick;
	}
}
