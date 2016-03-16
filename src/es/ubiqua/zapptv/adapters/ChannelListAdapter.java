package es.ubiqua.zapptv.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.ChannelItem;
import es.ubiqua.zapptv.listeners.OnWatchTVListener;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.manager.model.TVUrl;

public class ChannelListAdapter extends ArrayAdapter<ChannelItem>{
	
	private OnWatchTVListener mListener;
	
	public ChannelListAdapter(Context context, List<ChannelItem> objects, OnWatchTVListener listener) {
		super(context, 0, objects);
		mListener = listener;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.channel_list_header, null);
		}

		TextView name = (TextView) convertView.findViewById(R.id.channelName);
		ImageView icon = (ImageView)convertView.findViewById(R.id.channelIcon);
		LinearLayout channel = (LinearLayout)convertView.findViewById(R.id.channelViewOnline);
		
		ChannelItem item = (ChannelItem) getItem(position);
		
		name.setText(item.getChannel().getName());
		
		int rId = parent.getContext().getResources().getIdentifier(item.getChannel().getIcon_path(), "drawable", "es.ubiqua.zapptv");
		icon.setImageResource(rId);
		
		TVUrl a = BaseApplication.getWebserviceManager().getTVUrl(item.getChannel());
		
		try{
			
			final String url = BaseApplication.getWebserviceManager().getTVUrl(item.getChannel()).getUrl();
			
			channel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mListener.onWatchTV(url);
				}
			});
			
		}catch(Exception e){
			//channel.setVisibility(ImageView.INVISIBLE);
		}
		return convertView;
	}

}
