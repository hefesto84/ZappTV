package es.ubiqua.zapptv.manager.model;

import java.util.ArrayList;
import java.util.List;

public class ChannelList {
	
	private List<Channel> channels;
	
	public ChannelList(){
		setChannels(new ArrayList<Channel>());
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}
