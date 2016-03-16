package es.ubiqua.zapptv.adapters.item;

import es.ubiqua.zapptv.manager.model.Channel;

public class ChannelItem {
	
	private Channel channel;
	private String name;
	private long mId;
	
	public ChannelItem(Channel channel){
		this.channel = channel;
		this.name = channel.getName();
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
