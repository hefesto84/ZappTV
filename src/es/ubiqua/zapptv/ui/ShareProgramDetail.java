package es.ubiqua.zapptv.ui;

import es.ubiqua.zapptv.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShareProgramDetail extends BaseFragment{
	
	private Bundle mFragmentArguments;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentArguments = this.getArguments();
		View v = (View)inflater.inflate(R.layout.share_dialog, container,false);
		return v;
	}
}
