package es.ubiqua.zapptv.adapters.item;

import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.Program;

public class RightNowItem {
	private long id;
	private Program program;
	private Channel channel;
	
	public RightNowItem(Program program){
		this.program = program;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
}
