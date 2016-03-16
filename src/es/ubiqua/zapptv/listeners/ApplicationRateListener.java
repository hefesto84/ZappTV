package es.ubiqua.zapptv.listeners;

public interface ApplicationRateListener {
	
	public static int BTN_LIKE = 0;
	public static int BTN_NEED_TO_IMPROVE = 1;
	public static int BTN_RATE = 2;
	public static int BTN_SEND_IMPROVEMENTS = 3;
	public static int BTN_CLOSE = 4;
	public static int BTN_DONT_SHOW = 5;
	
	public void onButtonClicked(int button);
}
