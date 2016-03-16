package es.ubiqua.zapptv.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.ProgramItem;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.utils.Utils;

public class ProgramListAdapter extends ArrayAdapter{

	public ProgramListAdapter(Context context, List objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.program_list_header, null);
		}

		ProgramItem p = (ProgramItem)getItem(position);
		
		TextView title = (TextView)convertView.findViewById(R.id.txtTitle);
		TextView hour = (TextView)convertView.findViewById(R.id.txtHour);
		TextView description = (TextView)convertView.findViewById(R.id.txtDescription);
		hour.setText(Utils.parseEmisionTime(p.getProgram().getStart()));
		title.setText(p.getProgram().getTitle());
		description.setText(p.getProgram().getDescription());
		return convertView;
	}
	
	
}
