package es.ubiqua.zapptv.manager.model;

import java.util.ArrayList;
import java.util.List;

public class ChannelStatus {
	
	private List<String> status;
	
	public ChannelStatus(){
		this.status = new ArrayList<String>();
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}
	
	public boolean contains(int id){
		for(String s : status){
			if(s.contains(String.valueOf(id))){
				return true;
			}
		}
		return false;
	}
}
