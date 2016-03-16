package es.ubiqua.zapptv.manager.model;

public class Criteria {

	public enum BY { 
		ACTOR, 
		GUEST, 
		PRESENTER, 
		COUNTRY, 
		YEAR, 
		TITLE, 
		CHANNEL 
	}

	private String query;
	private BY criteria;
	
	public Criteria(BY criteria){
		this.setCriteria(criteria);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public BY getCriteria() {
		return criteria;
	}

	public void setCriteria(BY criteria) {
		this.criteria = criteria;
	}
	
}
