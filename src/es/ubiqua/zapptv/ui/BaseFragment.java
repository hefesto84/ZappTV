package es.ubiqua.zapptv.ui;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.listeners.ApplicationRateListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class BaseFragment extends Fragment{

	@Override
	public void onDetach() {
		super.onDetach();
	}

	protected void openFragment(Fragment fragment, Bundle bundle){
    	fragment.setArguments(bundle);
    	FragmentTransaction transaction = getFragmentManager().beginTransaction();
    	transaction.replace(R.id.container,fragment,"nontoplevel");
    	transaction.addToBackStack(null);
    	transaction.commit();
    }

}
