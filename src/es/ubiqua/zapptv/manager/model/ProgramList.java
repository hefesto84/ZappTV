package es.ubiqua.zapptv.manager.model;

import java.util.ArrayList;
import java.util.List;

public class ProgramList {
	
	private List<Program> programmes;
	
	public ProgramList(){
		this.programmes = new ArrayList<Program>();
	}

	public List<Program> getProgrammes() {
		return programmes;
	}

	public void setProgrammes(List<Program> programmes) {
		this.programmes = programmes;
	}

}
