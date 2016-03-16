package es.ubiqua.zapptv.ui;

import java.util.HashMap;
import java.util.List;

import com.loopj.android.image.SmartImageView;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.AppDiaFragmentPagerAdapter;
import es.ubiqua.zapptv.listeners.OnAppDiscoveryListener;
import es.ubiqua.zapptv.manager.model.AppItem;
import es.ubiqua.zapptv.manager.model.AppItems;
import es.ubiqua.zapptv.tasks.DownloadDiscoveryAppItemsAsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecommendedAppFragment extends BaseFragment implements OnAppDiscoveryListener{
	
	private DownloadDiscoveryAppItemsAsyncTask mAppsAsyncTask;
	private ProgressDialog mProgressDialog;
	private List<AppItems> mItems;
	private View mView;
	private ImageButton mRight;
	private ImageButton mLeft;
	private int mCurrentApp;
	private int mMaxApps;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.recommended_app_fragment, container, false);
		mCurrentApp = 0;
		mView = v;
		mProgressDialog = ProgressDialog.show(getActivity(), getString(R.string.app_recommended_title), getString(R.string.app_recommended_subtitle));
		mProgressDialog.show();
		mAppsAsyncTask = new DownloadDiscoveryAppItemsAsyncTask(this);
		mAppsAsyncTask.execute(0);
		BaseApplication.getEventsManager().sendEvent(R.string.app_ga_discoveryapps, R.string.app_ga_action_view,"");
		return v;
	}

	@Override
	public void onUpdated() {
		mItems = BaseApplication.getWebserviceManager().getAppsRecommended();
		mProgressDialog.dismiss();
		mMaxApps = mItems.size();
		if(mItems.size()!=0){
			showApp(getFirstApp());
		}
	}
	
	private int getFirstApp(){
		String id = BaseApplication.getInstance().DiscoveryGetElement(0).get("nombre");
		for(int i=0; i<mItems.size(); i++){
			if(mItems.get(i).getResponse()[0].getNombre().equals(id)){
				return i;
			}
		}
		return 0;
	}
	
	private void showApp(int i){
		final AppItem ai = BaseApplication.getWebserviceManager().getAppsRecommended().get(i).getResponse()[0];
		TextView tv = (TextView)mView.findViewById(R.id.txtAppTitle);
		String text = ai.getNombre();
		tv.setText(text);
		SmartImageView image = (SmartImageView)mView.findViewById(R.id.imgApp);
		String url = ai.getImage();
		image.setImageUrl(url);
		TextView shortDescription = (TextView)mView.findViewById(R.id.txtShortDescription);
		shortDescription.setText(ai.getShortDescription());
		TextView longDescription = (TextView)mView.findViewById(R.id.txtLongDescription);
		String content = android.text.Html.fromHtml(ai.getLongDescription()).toString();
		content = content.replace("<br>", "\n\n");
		longDescription.setText(content);
		Button button = (Button)mView.findViewById(R.id.btnDownload);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BaseApplication.getEventsManager().sendEvent(R.string.app_ga_discoveryapps, R.string.app_ga_action_click,"");

				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ai.getUrlclick())));
			}
		});
		
		mLeft = (ImageButton)mView.findViewById(R.id.totheleft);
		mRight = (ImageButton)mView.findViewById(R.id.totheright);
		
		mLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCurrentApp!=0){
					mCurrentApp--;
				}else{
					mCurrentApp = mMaxApps-1;
				}
				showApp(mCurrentApp);
			}
		});
		
		mRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCurrentApp<mMaxApps-1){
					mCurrentApp++;
				}else{
					mCurrentApp = 0;
				}
				showApp(mCurrentApp);
			}
		});
	}
	
}
