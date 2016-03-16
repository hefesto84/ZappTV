package es.ubiqua.zapptv.adapters;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.MostRatedItem;

@SuppressLint("InflateParams")
public class MostRatedListAdapter extends ArrayAdapter<MostRatedItem>{

	private Activity mActivity;
	
	public MostRatedListAdapter(Context context, List<MostRatedItem> objects, Activity activity) {
		super(context, 0, objects);
		mActivity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.mostrated_list_header, null);
		}

		final int pos = position;
		final View cv = convertView;
		
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				MostRatedItem item = (MostRatedItem)getItem(pos);
				TextView title = (TextView)cv.findViewById(R.id.txtTitle);
				title.setText(item.getProgram().getTitle());
				SmartImageView image = (SmartImageView)cv.findViewById(R.id.imgImage);
				image.setImageUrl(item.getProgram().getImage_path());
			}
		});
		
		
		return convertView;
	}
	
}
