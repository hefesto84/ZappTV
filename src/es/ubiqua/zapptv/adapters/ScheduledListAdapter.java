package es.ubiqua.zapptv.adapters;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.MostRatedItem;
import es.ubiqua.zapptv.adapters.item.ScheduledItem;
import es.ubiqua.zapptv.listeners.OnSocialListener;

@SuppressLint("InflateParams")
public class ScheduledListAdapter extends ArrayAdapter<ScheduledItem>{

	private Activity mActivity;
	private final OnSocialListener mListener;
	
	public ScheduledListAdapter(Context context, List<ScheduledItem> objects, Activity activity, OnSocialListener listener) {
		super(context, 0, objects);
		mActivity = activity;
		mListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.scheduled_list_header, null);
		}

		final int pos = position;
		final View cv = convertView;
		
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ScheduledItem item = (ScheduledItem)getItem(pos);
				TextView title = (TextView)cv.findViewById(R.id.txtTitle);
				title.setText(item.getProgram().getTitle());
				SmartImageView image = (SmartImageView)cv.findViewById(R.id.imgImage);
				image.setImageUrl(item.getProgram().getImage_path());
			}
		});
		
		
		return convertView;
	}
	
}
