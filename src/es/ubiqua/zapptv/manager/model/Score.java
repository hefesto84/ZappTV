package es.ubiqua.zapptv.manager.model;

public class Score {
	
	private String idProgram;
	private String score;
	
	public Score(){
		idProgram = "";
		score = "";
	}

	public String getIdProgram() {
		return idProgram;
	}

	public String getScore() {
		return score;
	}

	public void setIdProgram(String idProgram) {
		this.idProgram = idProgram;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
