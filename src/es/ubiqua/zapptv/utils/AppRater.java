package es.ubiqua.zapptv.utils;

import es.ubiqua.zapptv.R;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

public class AppRater {
	private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches
    
    public void app_launched(Context context){
    
    	/*
    	SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        
        SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(context, editor);
            }
        }
		
        editor.commit();
     	*/
    	
    	showRateDialog(context /*, editor*/);
    }
    
    public  void showRateDialog(final Context context/*, final SharedPreferences.Editor editor*/){
    	final Dialog dialog = new Dialog(context);
    	dialog.setContentView(R.layout.ratemyapp_step1);
    	dialog.setTitle("Rate app");
    	dialog.show();
    }
}
