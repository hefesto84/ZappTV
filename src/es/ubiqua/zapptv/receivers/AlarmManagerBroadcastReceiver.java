package es.ubiqua.zapptv.receivers;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.manager.model.Program;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		sendNotification(intent.getExtras().getInt("broadcast"), context);
	}
	
	private void sendNotification(int id, Context context) {
		Program p = new Program();
		p.setIdBroadcast(id);	
		p = BaseApplication.getWebserviceManager().getProgram(p);
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.zapptvicon)
        .setContentTitle(BaseApplication.getInstance().getApplicationContext().getString(R.string.app_notification))
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(p.getTitle()))
        .setContentText(p.getTitle());
        mNotificationManager.notify(0, mBuilder.build());
    }

}
