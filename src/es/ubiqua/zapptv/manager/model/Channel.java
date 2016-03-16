package es.ubiqua.zapptv.manager.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import es.ubiqua.zapptv.BaseApplication;

public class Channel {
	
	private int id;
	private int channelId;
	@SerializedName("lang")
	private String language;
	private String name;
	private String icon_path;
	private String full_path;
	
	public Channel(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon_path() {
		return icon_path;
	}

	public void setIcon_path(String icon_path) {
		this.setFull_path(icon_path);
		String icon = icon_path.substring(icon_path.lastIndexOf("/")+1);
		
		Pattern pat = Pattern.compile("^[^0-9].*");
	    Matcher mat = pat.matcher(icon);
	    
	    if(!mat.matches()){
	    	icon = "x"+icon;
	    }
	    try{
	    	String s =  icon.substring(0,icon.lastIndexOf("."));
	    	this.icon_path = s;
	    }catch(Exception e){
	    	this.icon_path = icon_path;
	    }
		
	}

	public String getFull_path() {
		return full_path;
	}

	public void setFull_path(String full_path) {
		this.full_path = full_path;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
}
