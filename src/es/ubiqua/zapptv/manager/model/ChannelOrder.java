package es.ubiqua.zapptv.manager.model;

import java.util.ArrayList;

import es.ubiqua.zapptv.model.DynamicChannelModel;

public class ChannelOrder {
	
	private ArrayList<DynamicChannelModel> items;
	
	public ChannelOrder(){
		setItems(new ArrayList<DynamicChannelModel>());
	}
	/*
	private int[] id;
	private boolean[] enabled;
	
	public ChannelOrder(int numChannels){
		id = new int[numChannels];
		enabled = new boolean[numChannels];
	}

	public int[] getId() {
		return id;
	}

	public boolean[] getEnabled() {
		return enabled;
	}

	public void setId(int[] id) {
		this.id = id;
	}

	public void setEnabled(boolean[] enabled) {
		this.enabled = enabled;
	}

	*/

	public ArrayList<DynamicChannelModel> getItems() {
		return items;
	}

	public void setItems(ArrayList<DynamicChannelModel> items) {
		this.items = items;
	}
}
