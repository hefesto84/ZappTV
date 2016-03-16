package es.ubiqua.zapptv.manager.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Program {

	private String start;
	private String stop;
	@SerializedName("id")
	private int idProgramme;
	private String[] categories;
	@SerializedName("broadcast")
	private int idBroadcast;
	private int channel;
	private String title;
	private String subtitle;
	@SerializedName("lang")
	private String language;
	@SerializedName("desc")
	private String description;

	private String[] directors;
	private String[] actors;
	private String[] presenters;
	private String[] guests;
	@SerializedName("year")
	private int date;
	private String country;
	private String rating;
	private String image_path;
	
	private String category;
	private String presenter;
	private String guest;
	private String director;
	private String actor;
	private long timestamp_a;
	private long timestamp_b;
	private int score;
	
	public Program(){
		start = "";
		stop = "";
		idProgramme = 0;
		setCategories(new String[0]);
		idBroadcast = 0;
		channel = 0;
		title = "";
		subtitle = "";
		language = "";
		description = "";
		directors = new String[0];
		actors = new String[0];
		presenters = new String[0];
		guests =  new String[0];
		date = 0;
		country = "";
		rating = "";
		image_path = "";
		
		category = "";
	}
	
	public String[] getDirectors() {
		return directors;
	}

	public void setDirectors(String[] directors) {
		this.directors = directors;
	}

	public String[] getActors() {
		return actors;
	}

	public void setActors(String[] actors) {
		this.actors = actors;
	}

	public String[] getPresenters() {
		return presenters;
	}

	public void setPresenters(String[] presenters) {
		this.presenters = presenters;
	}

	public String[] getGuests() {
		return guests;
	}

	public void setGuests(String[] guests) {
		this.guests = guests;
	}
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public int getIdProgramme() {
		return idProgramme;
	}

	public void setIdProgramme(int idProgramme) {
		this.idProgramme = idProgramme;
	}

	public int getIdBroadcast() {
		return idBroadcast;
	}

	public void setIdBroadcast(int idBroadcast) {
		this.idBroadcast = idBroadcast;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getTimestamp_a() {
		return timestamp_a;
	}

	public long getTimestamp_b() {
		return timestamp_b;
	}

	public void setTimestamp_a(long timestamp_a) {
		this.timestamp_a = timestamp_a;
	}

	public void setTimestamp_b(long timestamp_b) {
		this.timestamp_b = timestamp_b;
	}

	public String getPresenter() {
		return presenter;
	}

	public String getGuest() {
		return guest;
	}

	public String getDirector() {
		return director;
	}

	public String getActor() {
		return actor;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public String toJson(){
		return new Gson().toJson(this);
	}
}
