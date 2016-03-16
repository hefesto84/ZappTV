package es.ubiqua.zapptv.manager;

import es.ubiqua.zapptv.manager.model.Program;


public interface EventsManagerInterface {
	public void show(String str);
	public void reminder(long timestamp, Program p);
	public void sendEvent(int page, int action, String label);
}
