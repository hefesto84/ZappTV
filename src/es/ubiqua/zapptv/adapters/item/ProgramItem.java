package es.ubiqua.zapptv.adapters.item;

import es.ubiqua.zapptv.manager.model.Program;

public class ProgramItem {

	private Program program;
	
	public ProgramItem(Program program){
		this.program = program;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

}
