package es.ubiqua.zapptv.ui;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.ProgramItem;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ScheduleFragment extends BaseFragment{
	
	private Bundle mFragmentArguments;
	private Activity mActivity;
	private WebView webview;
	private WebChromeClient chrome_client;
	private ProgressDialog mProgressDialog;
	
	public ScheduleFragment(){
		this.mActivity = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mFragmentArguments = this.getArguments();
		
		View v = (View)inflater.inflate(R.layout.schedule_fragment, container,false);
		mProgressDialog = ProgressDialog.show(getActivity(), getString(R.string.app_schedule_title), getString(R.string.app_schedule_subtitle));
		mProgressDialog.show();
		initialize(v);
		BaseApplication.getEventsManager().sendEvent(R.string.app_ga_scheduler, R.string.app_ga_action_view,"");
		return v;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initialize(View v){
		
		/* Objects instantiation */
		chrome_client = new WebChromeClient();
		
		webview = (WebView)v.findViewById(R.id.schedule);
		
		/* Settings configuration */
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSaveFormData(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		/* WebView clients configuration */
		webview.setWebChromeClient(chrome_client);
		webview.addJavascriptInterface(this, "SchedulerBridge");
		webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		webview.setWebViewClient(new WebViewClient() {
			
			@Override
		    public void onPageFinished(WebView view, String url) {
		        super.onPageFinished(view, url);
		        mProgressDialog.dismiss();
			}
			
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				return false;
			}
			
		});
		
		Utils.createScheduler_v2(BaseApplication.getWebserviceManager().getChannelList());
		webview.loadUrl(getString(R.string.app_path_scheduler));
	
	}
	
	private Bundle createFragmentBundle(ProgramItem item){
    	Bundle b = new Bundle();
    	b.putInt("broadcast", item.getProgram().getIdBroadcast());
    	return b;
    }
	
	@JavascriptInterface
	public void onProgramSelected(String id){
		BaseApplication.getEventsManager().sendEvent(R.string.app_ga_scheduler, R.string.app_ga_action_click,"");

		Program p = new Program();
		p.setIdBroadcast(Integer.valueOf(id));
		ProgramItem pi = new ProgramItem(p);
		openFragment(new ProgramDetailFragment(), createFragmentBundle(pi));
	}

}
