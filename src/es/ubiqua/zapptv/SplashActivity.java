package es.ubiqua.zapptv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import es.ubiqua.das.discoveryapps.services;
import es.ubiqua.zapptv.listeners.OnApplicationUpdatedListener;
import es.ubiqua.zapptv.tasks.DecompressDataAsyncTask;
import es.ubiqua.zapptv.tasks.DownloadDataAsyncTask;
import es.ubiqua.zapptv.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

public class SplashActivity extends Activity implements OnApplicationUpdatedListener{
	
	private DownloadDataAsyncTask downloadDataAsyncTask;
	private DecompressDataAsyncTask decompressDataAsyncTask;
	private GoogleCloudMessaging gcm;
	private String regid;
	private Context context;
	private int SPLASH_TIME = 10000;
	private boolean isTimeout = false;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String SMSIP_CLAVE_API        = "32537b7350";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		
		setContentView(R.layout.activity_splash);
		
		detectTimeout();
		
		initialize();
		
		downloadDataAsyncTask = new DownloadDataAsyncTask(this);
		decompressDataAsyncTask = new DecompressDataAsyncTask(this);
		onUpdateRequired(true);
		
	}
	
	private void detectTimeout(){
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				connectionError();
			}
			
		}, SPLASH_TIME);
	}
	
	private void connectionError(){
		/*
		new AlertDialog.Builder(SplashActivity.this)
        .setTitle(R.string.app_alert_error_title)
        .setMessage(R.string.app_alert_error_subtitle)
        .setCancelable(false)
        .setPositiveButton(R.string.app_alert_error_ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	closeApp();
            }
        }).create().show();    
        */
		try{
			FrameLayout error = (FrameLayout)findViewById(R.id.app_error);
			error.setVisibility(FrameLayout.VISIBLE);
		}catch(Exception e){
			
		}
	}
	
	private void closeApp(){
		this.finish();
	}
	
	private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.equals("")) {
            Log.i(BaseApplication.TAG, "Registration not found.");
            return "";
        }
        
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(BaseApplication.TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

	private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

	private SharedPreferences getGcmPreferences(Context context) {
        return getSharedPreferences(SplashActivity.class.getSimpleName(),Context.MODE_PRIVATE);
    }

	private void registerInBackground() {
		
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(getString(R.string.app_gcm_projectid));
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                    
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	if(msg.contains("Error")){
            		onRegistered(false);
            	}else{
            		onRegistered(true);
            	}
            }
        }.execute(null, null, null);
    }
	
	private void sendRegistrationIdToBackend() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String url = "http://smsip.ubiqua.us/device/register";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);

                nameValuePairs.add(new BasicNameValuePair("ubiquaId", SMSIP_CLAVE_API));
                nameValuePairs.add(new BasicNameValuePair("secret",   "TFSHJP"));
                nameValuePairs.add(new BasicNameValuePair("master",   "TLJMMB"));
                nameValuePairs.add(new BasicNameValuePair("telefono", "00000000000"));
                nameValuePairs.add(new BasicNameValuePair("tipo",     "GCM"));
                nameValuePairs.add(new BasicNameValuePair("regId",    regid));
                nameValuePairs.add(new BasicNameValuePair("idioma",   "es"));
                nameValuePairs.add(new BasicNameValuePair("version",  String.valueOf(getAppVersion(SplashActivity.this))));
                String result = Utils.POST(url, nameValuePairs);
                return result;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }
	
	private void storeRegistrationId(Context context, String regId) {
		Log.d(BaseApplication.TAG, "Registrando: "+regId);
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(BaseApplication.TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

	public void onRegistered(boolean result) {
		if(result){
			storeRegistrationId(context, regid);
		}else{
			storeRegistrationId(context, "null");
		}
	}

	private void initialize(){
		gcm = GoogleCloudMessaging.getInstance(this);
        regid = getRegistrationId(this.getApplicationContext());
        registerInBackground();
	}

	@Override
	public void onUpdateRequired(boolean required) {
		if(required){
			downloadDataAsyncTask.execute("");
		}else{
			onDataDecompressed(true);
		}
	}

	@Override
	public void onDataDownloaded(boolean downloaded) {
		Log.d(BaseApplication.TAG,"downloaded: "+downloaded);
		if(downloaded){
			decompressDataAsyncTask.execute("");
		}
	}

	@Override
	public void onDataDecompressed(boolean decompressed) {
		if(decompressed){
			BaseApplication.getDatabaseManager().initialize();
			updateAppDia();
		}
	}
	
	private void updateAppDia() {
        new UpdateAppdiaTask(null, null, null).getListado();
    }

	private void endSplash(){
		Intent app = new Intent(BaseApplication.getInstance().getApplicationContext(),MainActivity.class);
		app.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		BaseApplication.getInstance().getApplicationContext().startActivity(app);
		this.finish();
	}
	
	public class UpdateAppdiaTask extends services{
		@Override
        public void getListado() {
            new listado().execute();
        }

        public UpdateAppdiaTask(Context context, String locale, String code) {
            super(context, locale, code);
        }

        public class listado extends getListadoAsyncTask {

            protected void onPostExecute(Void r) {
            	endSplash();
            }
        }
	}
}
