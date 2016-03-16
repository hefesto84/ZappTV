package es.ubiqua.zapptv.ui;

import java.util.ArrayList;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.ProgramListAdapter;
import es.ubiqua.zapptv.adapters.item.ProgramItem;
import es.ubiqua.zapptv.manager.impl.DatabaseManager.DAY;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import es.ubiqua.zapptv.utils.Utils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChannelDetailListFragment extends BaseFragment{
	
	private ListView mChannelDetailListView;
	private ImageView mChannelImageView;
	private Bundle mFragmentArguments;
	
	public ChannelDetailListFragment(){
		super();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	mFragmentArguments = this.getArguments();
    	
    	View v = (View)inflater.inflate(R.layout.channel_detail, container,false);
    	
    	mChannelDetailListView = (ListView)v.findViewById(R.id.lstListPrograms);
    	mChannelImageView = (ImageView)v.findViewById(R.id.imgChannel);
    	
    	final ArrayList<ProgramItem> items = new ArrayList<ProgramItem>();
    	Channel c = new Channel();
    	c.setId(mFragmentArguments.getInt("channelId"));
    	c.setName(mFragmentArguments.getString("channelName"));
    	ProgramList list = BaseApplication.getWebserviceManager().getProgramList(c,DAY.TODAY);
    	
    
    	for(Program p : list.getProgrammes()){
    		items.add(new ProgramItem(p));
    	}
    	
    	int closestElement = getClosestElement(list);
    	
    	mChannelDetailListView.setAdapter(new ProgramListAdapter(getActivity().getApplicationContext(), items));
		
    	mChannelDetailListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arrayPosition, long arg3) {
				BaseApplication.getEventsManager().sendEvent(R.string.app_ga_channeldetail, R.string.app_ga_action_click, "");
				openFragment(new ProgramDetailFragment(), createFragmentBundle(items.get(arrayPosition)));
			}
    		
    	});
    	
    	int rId = getResources().getIdentifier(mFragmentArguments.getString("icon_path"), "drawable", "es.ubiqua.zapptv");
    	mChannelImageView.setImageResource(rId);
    	
    	TextView channelName = (TextView)v.findViewById(R.id.txtChannelName);
    	channelName.setText(c.getName());
    	
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_channeldetail, R.string.app_ga_action_view,"");
        mChannelDetailListView.setSelection(closestElement);
    	return v;
    }

    private Bundle createFragmentBundle(ProgramItem item){
    	Bundle b = new Bundle();
    	b.putInt("broadcast", item.getProgram().getIdBroadcast());
    	return b;
    }
    
    private int getClosestElement(ProgramList list){
    	long currentTime = System.currentTimeMillis();
    	int i = 0;
    	for(Program p : list.getProgrammes()){
    		long ctime = Utils.toTimestamp(p.getStart());
    		if(currentTime<ctime){
    			break;
    		}
    		i++;
    	}
    	return i;
    }
}
