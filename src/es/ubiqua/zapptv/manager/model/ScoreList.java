package es.ubiqua.zapptv.manager.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreList {
	
	private List<Score> scores;
	
	public ScoreList(){
		this.scores = new ArrayList<Score>();
	}

	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
}
