package es.ubiqua.zapptv.adapters;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.FavouriteItem;

public class FavouritesListAdapter extends ArrayAdapter<FavouriteItem>{

	private Activity mActivity;
	
	public FavouritesListAdapter(Context context, List<FavouriteItem> objects, Activity activity) {
		super(context, 0, objects);
		this.mActivity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.favourites_list_header, null);
		}
		final int pos = position;
		final View cv = convertView;
		
		mActivity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				FavouriteItem item = (FavouriteItem)getItem(pos);
				TextView title = (TextView)cv.findViewById(R.id.txtTitle);
				title.setText(item.getProgram().getTitle());
				SmartImageView image = (SmartImageView)cv.findViewById(R.id.imgImage);
				image.setImageUrl(item.getProgram().getImage_path());
				TextView description = (TextView)cv.findViewById(R.id.txtDescription);
				description.setText(item.getProgram().getDescription());
			}
			
		});
		
		return convertView;
	}
}
