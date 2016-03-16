package es.ubiqua.zapptv.utils;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.listeners.ApplicationRateListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationRate {
	
	private Context mContext;
	private ApplicationRateListener mListener;
	private LayoutInflater mInflater;
	private View mLayout;
	private AlertDialog.Builder mBuilder;
	private AlertDialog mAlertDialog;
	private boolean mIsPending;
	private boolean mIsImprove;
	private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 3;
    
	public ApplicationRate(Context context, ApplicationRateListener listener){
		this.mContext = context;
		this.mListener = listener;
		this.mIsPending = false;
		this.mIsImprove = false;
	}
	
	public void init(){
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        
        SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
      
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
            	handle();
            }
        }
		
        editor.commit();
		//handle();
	}
	
	public void showStep2(){
		
		mLayout = mInflater.inflate(R.layout.ratemyapp_step2,null);
		ImageView icon = (ImageView)mLayout.findViewById(R.id.imgRate);
		TextView title = (TextView)mLayout.findViewById(R.id.txtRateTitle);
		TextView subtitle = (TextView)mLayout.findViewById(R.id.txtRateSubtitle);
		Button btnRateImprove = (Button)mLayout.findViewById(R.id.btnRateImprove);
		
		if(mIsImprove){
			icon.setImageResource(R.drawable.rate_app_triste);
			title.setText(mContext.getString(R.string.app_rate_title_step_3));
			subtitle.setText(mContext.getString(R.string.app_rate_subtitle_step_3));
			btnRateImprove.setText(R.string.app_rate_button_send_step_3);
			
		}else{
			icon.setImageResource(R.drawable.rate_app_contento);
			title.setText(mContext.getString(R.string.app_rate_title_step_2));
			subtitle.setText(mContext.getString(R.string.app_rate_subtitle_step_2));
			btnRateImprove.setText(R.string.app_rate_button_rate_step_2);
		}
		
		Button btnRateClose = (Button)mLayout.findViewById(R.id.btnRateClose);
		btnRateClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAlertDialog.dismiss();
			}
		});
		
		btnRateImprove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mIsImprove){
					mListener.onButtonClicked(ApplicationRateListener.BTN_SEND_IMPROVEMENTS);
				}else{
					mListener.onButtonClicked(ApplicationRateListener.BTN_RATE);
				}
				mAlertDialog.dismiss();
			}
		});
		
		mBuilder = new AlertDialog.Builder(mContext);
		mBuilder.setView(mLayout);
		mAlertDialog = mBuilder.create();
		mAlertDialog.show();
	}
	
	private void handle(){
		createRateMyApp();
	}
	
	private void createRateMyApp(){
	
			mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mLayout = mInflater.inflate(R.layout.ratemyapp_step1,null);
			createButtonHandlers(mLayout);
			mBuilder = new AlertDialog.Builder(mContext);
			mBuilder.setView(mLayout);
			mAlertDialog = mBuilder.create();
			mAlertDialog.show();
	
	}
	
	private void createButtonHandlers(View layout){
		
			Button btnRateLike = (Button)layout.findViewById(R.id.btnRateLike);
			btnRateLike.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onButtonClicked(ApplicationRateListener.BTN_LIKE);
					mAlertDialog.dismiss();
					mIsImprove = false;
					showStep2();
				}
			});
			
			Button btnRateImprove = (Button)layout.findViewById(R.id.btnRateImprove);
			btnRateImprove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mListener.onButtonClicked(ApplicationRateListener.BTN_NEED_TO_IMPROVE);
					mAlertDialog.dismiss();
					mIsImprove = true;
					showStep2();
				}
			});
		
		Button btnRateClose = (Button)layout.findViewById(R.id.btnRateClose);
		btnRateClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onButtonClicked(ApplicationRateListener.BTN_CLOSE);
				mAlertDialog.dismiss();
				
			}
		});
		
		Button btnRateDontShow = (Button)layout.findViewById(R.id.btnRateDontAsk);
		btnRateDontShow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onButtonClicked(ApplicationRateListener.BTN_DONT_SHOW);
				mAlertDialog.dismiss();
			}
		});
	}
}
