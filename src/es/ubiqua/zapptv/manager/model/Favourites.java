package es.ubiqua.zapptv.manager.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Favourites {
	
	private List<Program> programs;

	public Favourites(){
		setPrograms(new ArrayList<Program>());
	}

	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}

}
