package es.ubiqua.zapptv.manager.model;

public class Checkin {
	
	private int idProgram;
	private int times;
	
	public Checkin(){
		
	}

	public int getIdProgram() {
		return idProgram;
	}

	public int getTimes() {
		return times;
	}

	public void setIdProgram(int idProgram) {
		this.idProgram = idProgram;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
