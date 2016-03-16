package es.ubiqua.zapptv.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.ProgramItem;
import es.ubiqua.zapptv.adapters.item.RightNowItem;
import es.ubiqua.zapptv.utils.Utils;

public class RightNowListAdapter extends ArrayAdapter<RightNowItem> implements Filterable{

	public RightNowListAdapter(Context context, ArrayList<RightNowItem> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.rightnow_list_header, null);
		}

		RightNowItem p = (RightNowItem)getItem(position);
		
		TextView title = (TextView)convertView.findViewById(R.id.txtTitle);
		TextView start = (TextView)convertView.findViewById(R.id.txtStart);
		TextView end = (TextView)convertView.findViewById(R.id.txtEnd);
		ImageView icon = (ImageView)convertView.findViewById(R.id.imgChannel);
		ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.progressBar);
		
		start.setText(Utils.parseEmisionTime(p.getProgram().getStart()) + "h");
		end.setText(Utils.parseEmisionTime(p.getProgram().getStop())+"h");
		title.setText(p.getProgram().getTitle());
		
		
		int rId = parent.getContext().getResources().getIdentifier(p.getChannel().getIcon_path(), "drawable", "es.ubiqua.zapptv");
		icon.setImageResource(rId);
		progress.setProgress(Utils.getEmissionProgress(p.getProgram().getTimestamp_a(), p.getProgram().getTimestamp_b()));
		
		return convertView;
	}
}
