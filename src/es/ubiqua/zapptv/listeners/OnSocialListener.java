package es.ubiqua.zapptv.listeners;

public interface OnSocialListener {
	public void onAlarm();
	public void onRated(boolean status, int rating);
	public void onFavourite(boolean status);
	public void onCheckin(boolean status);
	public void onShared();
	public void onAlarmDeleted(int id);
	public void onSharedBuilt(String url);
}
