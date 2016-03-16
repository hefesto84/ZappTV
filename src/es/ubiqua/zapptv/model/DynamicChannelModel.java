package es.ubiqua.zapptv.model;

public class DynamicChannelModel {
	private int id;
	private String name;
	private int channelId;
	private String icon_path;
	
	public DynamicChannelModel(){
		
	}
	
	public DynamicChannelModel(int id, String name, int channelId, String icon_path){
		this.id = id;
		this.name = name;
		this.channelId = channelId;
		this.icon_path = icon_path;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getChannelId() {
		return channelId;
	}

	public String getIcon_path() {
		return icon_path;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public void setIcon_path(String icon_path) {
		this.icon_path = icon_path;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
