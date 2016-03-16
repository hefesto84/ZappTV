package es.ubiqua.zapptv.tasks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.listeners.OnAppDiscoveryListener;
import es.ubiqua.zapptv.manager.model.AppItem;
import es.ubiqua.zapptv.manager.model.AppItems;
import es.ubiqua.zapptv.utils.Utils;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadDiscoveryAppItemsAsyncTask extends AsyncTask<Integer, Float, Integer>{

	private OnAppDiscoveryListener mListener;
	private Gson mGson = new Gson();
	
	public DownloadDiscoveryAppItemsAsyncTask(OnAppDiscoveryListener listener){
		mListener = listener;
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		int i = BaseApplication.getInstance().DiscoveryCountItems();
		for(int j = 0; j<i; j++){
			try{
				AppItems item = new AppItems();
				String id = BaseApplication.getInstance().DiscoveryGetElement(j).get("id");
				AppItem ai = new AppItem();
				ai.setId(id);
				item.setResponse(new AppItem[1]);
				item.getResponse()[0] = ai;
				if(BaseApplication.getWebserviceManager().isAppRecommended(item)){
					break;
				}else{
					String url = BaseApplication.getInstance().DiscoveryGetElement(j).get("urlview");
					item = mGson.fromJson(Utils.GET(url), AppItems.class);
					BaseApplication.getWebserviceManager().setAppRecommended(item);
				}
			}catch(Exception e){
				Log.e(BaseApplication.TAG,"Error downloading element from Discovery App Services: "+e.getMessage());
			}
		}
		return null;
	}
	
	protected void onPostExecute(Integer bytes) {
        mListener.onUpdated();
    }

}
