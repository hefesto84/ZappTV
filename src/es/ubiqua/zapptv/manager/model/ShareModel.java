package es.ubiqua.zapptv.manager.model;

public class ShareModel {
	
	private int id;
	private String name;
	private String url;
	private String image;
	private String description;
	private String text;
	
	public ShareModel(int id, String name, String url, String image, String description, String text){
		this.setId(id);
		this.name = name;
		this.url = url;
		this.image = image;
		this.description = description;
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}

	public String getText() {
		return text;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
