package es.ubiqua.zapptv.adapters;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.MostViewedItem;

@SuppressLint("InflateParams")
public class MostViewedListAdapter extends ArrayAdapter<MostViewedItem>{

	public MostViewedListAdapter(Context context, List<MostViewedItem> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.mostviewed_list_header, null);
		}

		MostViewedItem item = (MostViewedItem)getItem(position);
		TextView title = (TextView)convertView.findViewById(R.id.txtTitle);
		title.setText(item.getProgram().getTitle());
		SmartImageView image = (SmartImageView)convertView.findViewById(R.id.imgImage);
		image.setImageUrl(item.getProgram().getImage_path());
		
		
		return convertView;
	}
}
