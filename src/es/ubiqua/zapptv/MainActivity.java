package es.ubiqua.zapptv;


import es.ubiqua.zapptv.listeners.ApplicationRateListener;
import es.ubiqua.zapptv.ui.AboutFragment;
import es.ubiqua.zapptv.ui.ChannelListFragment;
import es.ubiqua.zapptv.ui.ConfigurationFragment;
import es.ubiqua.zapptv.ui.FavouritesListFragment;
import es.ubiqua.zapptv.ui.MostRatedListFragment;
import es.ubiqua.zapptv.ui.MostViewedListFragment;
import es.ubiqua.zapptv.ui.NavigationDrawerFragment;
import es.ubiqua.zapptv.ui.RecommendedAppFragment;
import es.ubiqua.zapptv.ui.RightNowListFragment;
import es.ubiqua.zapptv.ui.ScheduleFragment;
import es.ubiqua.zapptv.ui.ScheduledListFragment;
import es.ubiqua.zapptv.ui.UndefinedFragment;
import es.ubiqua.zapptv.utils.AppRater;
import es.ubiqua.zapptv.utils.ApplicationRate;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, DialogInterface.OnClickListener, View.OnClickListener, ApplicationRateListener{

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private AlertDialog dialog;
    private ApplicationRate appRate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        
        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
        appRate = new ApplicationRate(this,this);
        appRate.init();
    }
    
    public void onBackPressed() {
    	Fragment fr = (Fragment)getFragmentManager().findFragmentByTag("toplevel");
    	if(fr.isVisible()){
    		createExitDialog();
			dialog.show();
    	}else{
    		super.onBackPressed();
    	}
	}

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    	Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        
        switch(position){
        	default:
        	case 0:
        		fragment = new RightNowListFragment();
        		getActionBar().setTitle(R.string.section_rightnow);
        		break;
        	case 1:
        		fragment = new ScheduleFragment();
        		getActionBar().setTitle(R.string.section_schedule);
        		break;
        	case 2:
        		fragment = new ChannelListFragment();
        		getActionBar().setTitle(R.string.section_channels);
        		break;
        	case 3:
        		if(BaseApplication.getWebserviceManager().getProgramListFavourites().getProgrammes().size()!=0){
        			fragment = new FavouritesListFragment();
        			getActionBar().setTitle(R.string.section_favourites);
        		}else{
        			fragment = new UndefinedFragment(getString(R.string.app_favourites_undefined));
        			getActionBar().setTitle(R.string.section_favourites);
        		}
        		break;
        	case 4:
        		if(BaseApplication.getWebserviceManager().getListProgramsByAlarm().getProgrammes().size()!=0){
        			fragment = new ScheduledListFragment();
        			getActionBar().setTitle(R.string.section_alarms);
        		}else{
        			fragment = new UndefinedFragment(getString(R.string.app_alarms_undefined));
        			getActionBar().setTitle(R.string.section_alarms);
        		}
        		break;
        	case 5:
        		fragment = new MostViewedListFragment();
        		getActionBar().setTitle(R.string.section_mostviewed);
        		break;
        	case 6:
        		fragment = new MostRatedListFragment();
        		getActionBar().setTitle(R.string.section_mostrated);
        		break;
        	case 7:
        		fragment = new ConfigurationFragment();
        		getActionBar().setTitle(R.string.section_configuration);
        		break;
        	case 9:
        		fragment = new RecommendedAppFragment();
        		getActionBar().setTitle(R.string.section_recommended_app);
        		break;
        	case 8:
        		fragment = new AboutFragment();
        		getActionBar().setTitle(R.string.section_about);
        		break;
        }
	    if(fragment!=null){
	        fragmentManager.popBackStack();
	        FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.container, fragment,"toplevel");
	        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
	        ft.commit();
	        //fragmentManager.beginTransaction().replace(R.id.container, fragment,"toplevel").commit();
	        
        }
    }

    public void onSectionAttached(int number){
    	Log.d(BaseApplication.TAG,"ATTACHED FRAGMENT: "+number);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    
    private void createExitDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	
		builder.setTitle(getResources().getString(R.string.exit_dialog_title));
		builder.setMessage(getResources().getString(R.string.exit_dialog_subtitle));
		builder.setPositiveButton(getResources().getString(R.string.exit_dialog_option_yes), this);
		builder.setNegativeButton(getResources().getString(R.string.exit_dialog_option_no), this);
		dialog = builder.create();
	}
  
    public static class PlaceholderFragment extends Fragment {
   
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            return rootView;
        }

        
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
        
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which==-1){ finish(); }
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onButtonClicked(int button) {
		
		if(button==ApplicationRateListener.BTN_SEND_IMPROVEMENTS){
			Intent intent = new Intent(Intent.ACTION_SENDTO);
	    	intent.setType("text/plain");
	    	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_subject_contact_mail));
	    	intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_content_contact_mail));
	    	intent.setData(Uri.parse("mailto:"+getString(R.string.app_email_contact_mail))); 
	    	intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK); 
	    	startActivity(intent);
		}
		
		if(button==ApplicationRateListener.BTN_RATE){
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=es.ubiqua.zapptv")));
		}
		
		if(button==ApplicationRateListener.BTN_DONT_SHOW){
			SharedPreferences prefs = getSharedPreferences("apprater", 0);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("dontshowagain", true);
			editor.commit();
		}
	}

}
