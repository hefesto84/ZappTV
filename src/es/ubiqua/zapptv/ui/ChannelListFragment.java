package es.ubiqua.zapptv.ui;

import java.util.ArrayList;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.ChannelListAdapter;
import es.ubiqua.zapptv.adapters.item.ChannelItem;
import es.ubiqua.zapptv.listeners.OnWatchTVListener;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelList;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChannelListFragment extends BaseFragment implements OnWatchTVListener{
	
	private ListView mChannelListView;
	
	public ChannelListFragment(){
		super();
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	mChannelListView = (ListView)inflater.inflate(R.layout.channel_list_fragment, container, false);

    	final ArrayList<ChannelItem> items = new ArrayList<ChannelItem>();
    	ChannelList list = BaseApplication.getWebserviceManager().getChannelList();
    	for(Channel channel : list.getChannels()){
    		if(BaseApplication.getWebserviceManager().isChannelFavourite(channel)){
    			items.add(new ChannelItem(channel));
    		}
    	}
    	
    	mChannelListView.setAdapter(new ChannelListAdapter(getActivity().getApplicationContext(),items,this));
    	
    	mChannelListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arrayPosition, long arg3) {
				BaseApplication.getEventsManager().sendEvent(R.string.app_ga_channeldetail, R.string.app_ga_action_click, "");
				openFragment(new ChannelDetailListFragment(), createFragmentBundle(items.get(arrayPosition)));
			}
    		
		});
    	
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_channels, R.string.app_ga_action_view,"");
        return mChannelListView;	

    }  
    
    private Bundle createFragmentBundle(ChannelItem item){
    	Bundle b = new Bundle();
    	b.putInt("channelId", item.getChannel().getId());
    	b.putString("channelName", item.getChannel().getName());
    	b.putString("icon_path", item.getChannel().getIcon_path());
    	return b;
    }

	@Override
	public void onWatchTV(String url) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}
}
