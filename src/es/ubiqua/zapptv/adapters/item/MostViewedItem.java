package es.ubiqua.zapptv.adapters.item;

import es.ubiqua.zapptv.manager.model.Program;

public class MostViewedItem {

	private Program program;
	
	public MostViewedItem(Program program){
		this.program = program;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

}
