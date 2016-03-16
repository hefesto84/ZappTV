package es.ubiqua.zapptv.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.listeners.OnSocialListener;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.Score;
import es.ubiqua.zapptv.manager.model.ShareModel;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

public class SendShareModelAsyncTask extends AsyncTask<ShareModel, Float, String>{

	private OnSocialListener mListener;
	private String mScore = "0";
	private boolean mStatus = false;
	private ShareModel mShareModel;
	String responseText = "";
	
	public SendShareModelAsyncTask(OnSocialListener listener){
		mListener = listener;
	}
	
	protected String doInBackground(ShareModel... s) {
		mShareModel = s[0];
		sendShareModel();
		return null;
	}
	
	private void sendShareModel(){
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(BaseApplication.WS_DOMAIN+"social.php");
		
		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(mShareModel.getId())));
			nameValuePairs.add(new BasicNameValuePair("title", mShareModel.getName()));
			nameValuePairs.add(new BasicNameValuePair("description",mShareModel.getDescription()));
			nameValuePairs.add(new BasicNameValuePair("image", mShareModel.getImage()));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			
			HttpResponse response = client.execute(post);
			
			responseText = null;
			
			try {
				responseText = EntityUtils.toString(response.getEntity());
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
	
	protected void onPostExecute(String bytes) {
		if(mStatus){
			mListener.onSharedBuilt(responseText);
		}else{
			BaseApplication.getEventsManager().show(BaseApplication.getInstance().getString(R.string.app_cant_share));
		}
    }

}
