package es.ubiqua.zapptv.tasks;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.SplashActivity;
import es.ubiqua.zapptv.listeners.OnApplicationUpdatedListener;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DecompressDataAsyncTask extends AsyncTask<String, Float, Integer>{

	private OnApplicationUpdatedListener mListener;
	private boolean mDecompressed = false;
	
	public DecompressDataAsyncTask(OnApplicationUpdatedListener listener){
		this.mListener = listener;
		mDecompressed = false;
	}
	
	protected Integer doInBackground(String... arg0) {
		unpackZip("tvurls.zip","tvurls.json");
		unpackZip("channels.zip","channels.json");
		unpackZip("score.zip","score.json");
		unpackZip("checkin.zip","checkin.json");
		unpackZip("0_programs.zip","0_programs.json");
		unpackZip("1_programs.zip","1_programs.json");
		mListener.onDataDecompressed(mDecompressed);
		return null;
	}

	private boolean unpackZip(String zipname, String output_name)
	{       
	     InputStream is;
	     ZipInputStream zis;
	     try 
	     {
	         String filename;
	         is = BaseApplication.getInstance().getApplicationContext().openFileInput(zipname);//new FileInputStream(path + zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) 
	         {
	        	 
	             filename = ze.getName();

	             FileOutputStream fout = BaseApplication.getInstance().getApplicationContext().openFileOutput(output_name, Context.MODE_PRIVATE);

	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();   
	             
	             zis.closeEntry();
	         }

	         zis.close();
	     } 
	     catch(IOException e)
	     {
	         return false;
	     }

	    return true;
	}

	protected void onPostExecute(Integer bytes) {
        mListener.onDataDecompressed(true);
    }
}
