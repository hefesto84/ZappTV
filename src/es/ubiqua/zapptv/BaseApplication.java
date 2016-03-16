package es.ubiqua.zapptv;

import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import es.ubiqua.das.discoveryapps.services;
import es.ubiqua.zapptv.manager.impl.DatabaseManager;
import es.ubiqua.zapptv.manager.impl.EventsManager;
import es.ubiqua.zapptv.manager.impl.WebserviceManager;
import es.ubiqua.zapptv.utils.AppRater;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class BaseApplication extends Application{
	
	private static BaseApplication singleton;
	private static DatabaseManager databaseManager;
	private static WebserviceManager webserviceManager;
	private static EventsManager eventsManager;
	
	public static String TAG = "es.ubiqua.zapptv";
	//public static String WS_DOMAIN = "http://www.frozenbullets.com/";
	public static String WS_DOMAIN = "http://ws.zapptv.es/";
	
	public static boolean isOnTopLevel = false;
	public static DiscoveryAppServices DiscoveryApps;
	public static AppRater appRater;
	
	public static BaseApplication getInstance(){
		return singleton;
	}
	
	public static EventsManager getEventsManager(){
		return eventsManager;
	}
	
	public static DatabaseManager getDatabaseManager(){
		return databaseManager;
	}
	
	public static WebserviceManager getWebserviceManager(){
		return webserviceManager;
	}

	public static synchronized Tracker getTracker(){
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(BaseApplication.getInstance().getApplicationContext());
		Tracker t = analytics.newTracker(R.xml.global_tracker);
		return t;
	}
	
	public void onCreate() {
		super.onCreate();
		singleton = this;
		eventsManager = new EventsManager();
		databaseManager = new DatabaseManager();
		webserviceManager = new WebserviceManager();
		DiscoveryApps = new DiscoveryAppServices(this.getBaseContext(), "es", "b2ea01bd9eeb");
		appRater = new AppRater();
		
		
		Log.d(BaseApplication.TAG, "Discovery items: "+DiscoveryApps.hasItems());
	}
	
	/* Implementación DiscoveryAppServices */
	
	class DiscoveryAppServices extends services {
        DiscoveryAppServices(Context context, String locale, String code) {
            super(context, locale, code);
        }
    }
	
	
	public boolean DiscoveryHasItems() {
        return DiscoveryApps.hasItems();
    }

    public int DiscoveryCountItems() {
        return DiscoveryApps.countItems();
    }

    public HashMap<String, String> DiscoveryGetElement(int elem) {
        return DiscoveryApps.getElement(elem);
    }

}
