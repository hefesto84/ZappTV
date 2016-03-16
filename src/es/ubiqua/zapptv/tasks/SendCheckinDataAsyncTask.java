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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.listeners.OnSocialListener;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.utils.Utils;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

public class SendCheckinDataAsyncTask extends AsyncTask<Program, Float, Integer>{

	private OnSocialListener mListener;
	private boolean mStatus = false;
	
	public SendCheckinDataAsyncTask(OnSocialListener listener){
		this.mListener = listener;
	}
	
	protected Integer doInBackground(Program... p) {
		check(p[0]);
		return null;
	}
	
	private void check(Program p){
	
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(BaseApplication.WS_DOMAIN+"checkin.php");
			String result = "";
			
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				String dataEncoded = URLEncoder.encode(new Gson().toJson(p),"UTF-8");
				nameValuePairs.add(new BasicNameValuePair("id",String.valueOf(p.getIdProgramme())));
				nameValuePairs.add(new BasicNameValuePair("program",new Gson().toJson(p)/*dataEncoded*/)); 
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
		mListener.onCheckin(mStatus);
    }

}
