package es.ubiqua.zapptv.ui;

import es.ubiqua.zapptv.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UndefinedFragment extends BaseFragment{
	
	private String mCaption;
	
	public UndefinedFragment(String caption){
		mCaption = caption;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = (View)inflater.inflate(R.layout.fragment_undefined, container,false);
		TextView tv = (TextView)v.findViewById(R.id.txtUndefined);
		tv.setText(mCaption);
		return v;
	}
}
