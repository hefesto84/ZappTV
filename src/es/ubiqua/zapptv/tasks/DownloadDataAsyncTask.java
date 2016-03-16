package es.ubiqua.zapptv.tasks;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.listeners.OnApplicationUpdatedListener;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadDataAsyncTask extends AsyncTask<String, Float, Integer>{

	private OnApplicationUpdatedListener mListener;
	private boolean mSuccess;
	
	public DownloadDataAsyncTask(OnApplicationUpdatedListener listener){
		this.mListener = listener;
		mSuccess = false;
	}
	
	protected Integer doInBackground(String... params) {
		downloadAndSave(BaseApplication.WS_DOMAIN+"tvurls.zip","tvurls.zip");
		downloadAndSave(BaseApplication.WS_DOMAIN+"0/score.zip","score.zip");
		downloadAndSave(BaseApplication.WS_DOMAIN+"0/checkin.zip","checkin.zip");
		downloadAndSave(BaseApplication.WS_DOMAIN+"0/programs.zip","0_programs.zip");
		downloadAndSave(BaseApplication.WS_DOMAIN+"1/programs.zip","1_programs.zip");
		downloadAndSave(BaseApplication.WS_DOMAIN+"0/channels.zip","channels.zip");
		return null;
	}

	private void downloadAndSave(String file_path, String file_name){
		try{
			URL url = new URL(file_path);
	
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);
	        urlConnection.connect();

	        FileOutputStream fos = BaseApplication.getInstance().getApplicationContext().openFileOutput(file_name, Context.MODE_PRIVATE);
	    
	        InputStream inputStream = urlConnection.getInputStream();

	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; 
	     
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	        	fos.write(buffer, 0, bufferLength);
	        }
	        fos.close();
	        urlConnection.disconnect();
	        mSuccess = true;
		}catch (MalformedURLException e) {
			Log.d(BaseApplication.TAG,"malformed E:"+e.getMessage());
			mSuccess = false;
		}catch (IOException e) {
			Log.d(BaseApplication.TAG,"ioexception E:"+e.getMessage());
			mSuccess = false;
		}
	}
	
	protected void onPostExecute(Integer bytes) {
        mListener.onDataDownloaded(mSuccess);
    }

}
