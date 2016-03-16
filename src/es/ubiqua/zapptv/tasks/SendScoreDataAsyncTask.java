package es.ubiqua.zapptv.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.listeners.OnSocialListener;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.Score;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

public class SendScoreDataAsyncTask extends AsyncTask<Program, Float, Integer>{

	private OnSocialListener mListener;
	private boolean mStatus = false;
	private int mRate = 0;
	private String result = "";
	
	public SendScoreDataAsyncTask(OnSocialListener listener){
		this.mListener = listener;
	}
	
	protected Integer doInBackground(Program... p) {
		rate(p[0]);
		return null;
	}
	
	private void rate(Program p){
	
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(BaseApplication.WS_DOMAIN+"score.php");
			
			
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				String dataEncoded = URLEncoder.encode(new Gson().toJson(p),"UTF-8");
				nameValuePairs.add(new BasicNameValuePair("id",String.valueOf(p.getIdProgramme())));
				nameValuePairs.add(new BasicNameValuePair("program",dataEncoded)); 
				nameValuePairs.add(new BasicNameValuePair("score",String.valueOf(p.getScore())));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpResponse response = client.execute(post);
				
				result = null;
				
				try {
					result = EntityUtils.toString(response.getEntity());
					mStatus = true;
				}catch (ParseException e) {
					mStatus = false;
				}
					
			} catch (UnsupportedEncodingException e) {
				mStatus = false;
			} catch (ClientProtocolException e) {
				mStatus = false;
			} catch (IOException e) {
				mStatus = false;
			}

	}
	
	protected void onPostExecute(Integer bytes) {
		mListener.onRated(mStatus,mRate);
    }

}
