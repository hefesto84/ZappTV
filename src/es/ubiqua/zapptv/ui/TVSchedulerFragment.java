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

public class TVSchedulerFragment extends BaseFragment{
	
	private Bundle mFragmentArguments;
	private Activity mActivity;
	private WebView webview;
	private WebChromeClient chrome_client;
	private ProgressDialog mProgressDialog;
	
	public TVSchedulerFragment(){
		this.mActivity = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mFragmentArguments = this.getArguments();
		
		View v = (View)inflater.inflate(R.layout.schedule_fragment, container,false);
		
	
		return v;
	}
	
	
	private Bundle createFragmentBundle(ProgramItem item){
    	Bundle b = new Bundle();
    	b.putInt("broadcast", item.getProgram().getIdBroadcast());
    	return b;
    }
	
}
