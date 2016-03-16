package es.ubiqua.zapptv.ui;

import java.util.regex.Pattern;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends BaseFragment implements OnClickListener{

	public AboutFragment(){
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_about, container, false);
		
		TextView tv1 = (TextView)v.findViewById(R.id.txtAbout1);
		TextView tv2 = (TextView)v.findViewById(R.id.txtAbout2);
		TextView tv3 = (TextView)v.findViewById(R.id.txtAbout3);
		
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		
		tv1.setText(Html.fromHtml(getString(R.string.about1)));
		tv2.setText(Html.fromHtml(getString(R.string.about2)));
		tv3.setText(Html.fromHtml(getString(R.string.about3)));
		
		linkify(tv1,tv3);
		BaseApplication.getEventsManager().sendEvent(R.string.app_ga_about, R.string.app_ga_action_view,"");
		
		return v;
	}
	
	private void linkify(TextView text, TextView mail){
		Pattern pattern = Pattern.compile("Ubiqua");
		Linkify.addLinks(text,pattern,"http://www.zapptv.es");
		pattern = Pattern.compile("OneData");
		Linkify.addLinks(text,pattern,"http://www.onedata.es");
		pattern = Pattern.compile("info@zapptv.es");
		Linkify.addLinks(mail,pattern,"mailto:info@zapptv.es");
	}
	
	public void sendEmailToUbiqua(){
	    	Intent intent = new Intent(Intent.ACTION_SENDTO);
	    	intent.setType("text/plain");
	    	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_subject_contact_mail));
	    	intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_content_contact_mail));
	    	intent.setData(Uri.parse("mailto:"+getString(R.string.app_email_contact_mail))); 
	    	intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK); 
	    	startActivity(intent);
	}
	  
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.txtAbout1:
			break;
		case R.id.txtAbout2:
			break;
		case R.id.txtAbout3:
			sendEmailToUbiqua();
			break;
		}
	}
	
}
