package es.ubiqua.zapptv.manager.impl;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.manager.EventsManagerInterface;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.receivers.AlarmManagerBroadcastReceiver;

public class EventsManager implements EventsManagerInterface {

	private Context context;
	private AlarmManagerBroadcastReceiver alarm;
	
	public EventsManager(){
		context = BaseApplication.getInstance().getApplicationContext();
		alarm = new AlarmManagerBroadcastReceiver();
	}
	
	public void show(String str) {
		Toast.makeText(BaseApplication.getInstance().getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	public void reminder(long timestamp, Program p) {
		AlarmManager alarmManager = (AlarmManager)BaseApplication.getInstance().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(BaseApplication.getInstance().getApplicationContext(),AlarmManagerBroadcastReceiver.class);
		i.putExtra("broadcast", p.getIdBroadcast());
		PendingIntent pi = PendingIntent.getBroadcast(BaseApplication.getInstance().getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp+100*10000, pi);
		BaseApplication.getWebserviceManager().saveReminder(p);
		BaseApplication.getEventsManager().show(BaseApplication.getInstance().getApplicationContext().getString(R.string.app_alarm_setted));
	}

	public void sendEvent(int page, int action, String label) {
		Tracker t = BaseApplication.getTracker();
		
		t.send(new HitBuilders.EventBuilder()
        .setCategory(BaseApplication.getInstance().getApplicationContext().getString(page))
        .setAction(BaseApplication.getInstance().getApplicationContext().getString(action))
        .setLabel(BaseApplication.getInstance().getApplicationContext().getString(page))
        .build());
	}
}
