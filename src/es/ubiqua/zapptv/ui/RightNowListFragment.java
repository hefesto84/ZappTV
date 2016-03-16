package es.ubiqua.zapptv.ui;

import java.util.ArrayList;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.DynamicRightNowListAdapter;
import es.ubiqua.zapptv.adapters.RightNowListAdapter;
import es.ubiqua.zapptv.adapters.item.RightNowItem;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelList;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RightNowListFragment extends BaseFragment implements SearchView.OnQueryTextListener{
	
	private ListView mRightNowListView;
	private SearchView mSearchView;
	
	public RightNowListFragment(){
		super();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	View v = (View)inflater.inflate(R.layout.rightnow_list_fragment, container, false);
    	mRightNowListView = (ListView)v.findViewById(R.id.lstRightNow);
    	mSearchView = (SearchView)v.findViewById(R.id.search_view);
    	
    	
    	final ArrayList<RightNowItem> items = new ArrayList<RightNowItem>();
    	ProgramList programs = BaseApplication.getWebserviceManager().getProgramListRightNow();
    	ChannelList channels = BaseApplication.getWebserviceManager().getChannelList();
    	
    	for(Channel channel : channels.getChannels()){
    		for(Program program : programs.getProgrammes()){
    			if(program.getChannel()==channel.getId() && BaseApplication.getWebserviceManager().isChannelFavourite(channel)){
    				RightNowItem rni = new RightNowItem(program);
    				rni.setChannel(channel);
    				items.add(rni);
    				break;
    			}
    		}
    	}
    	
    	mRightNowListView.setAdapter(new DynamicRightNowListAdapter(getActivity().getApplicationContext(), items,false));

    	mRightNowListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arrayPosition, long arg3) {
		    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_rightnow, R.string.app_ga_action_click,"");

				openFragment(new ProgramDetailFragment(), createFragmentBundle(items.get(arrayPosition)));
			}
    		
		});
    	
    	mRightNowListView.setTextFilterEnabled(true);
    	setupSearchView();
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_rightnow, R.string.app_ga_action_view,"");
        return v;
    }
    
    private Bundle createFragmentBundle(RightNowItem item){
    	Bundle b = new Bundle();
    	b.putInt("broadcast", item.getProgram().getIdBroadcast());
    	return b;
    }

	@Override
	public boolean onQueryTextChange(String text) {
		 if (TextUtils.isEmpty(text)) {
			 mRightNowListView.clearTextFilter();
	        } else {
	        	mRightNowListView.setFilterText(text.toString());
	        }
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void setupSearchView(){
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true);
		mSearchView.setQueryHint(getString(R.string.app_search_programs));
	}
}
