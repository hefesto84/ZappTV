package es.ubiqua.zapptv.ui;

import java.util.ArrayList;

import com.csform.android.uiapptemplate.adapter.DefaultAdapter;
import com.csform.android.uiapptemplate.util.DummyContent;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import android.widget.SearchView;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.DynamicChannelListAdapter;
import es.ubiqua.zapptv.adapters.item.ChannelItem;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelList;
import es.ubiqua.zapptv.manager.model.ChannelOrder;
import es.ubiqua.zapptv.model.DynamicChannelModel;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class ConfigurationFragment extends BaseFragment implements SearchView.OnQueryTextListener{
	
	private DynamicListView mChannelsListView;
	private ArrayList<DynamicChannelModel> items = new ArrayList<DynamicChannelModel>();
	private SearchView mSearchView;
	
	public ConfigurationFragment(){
		super();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = (View)inflater.inflate(R.layout.configuration_fragment, container,false);
		mSearchView = (SearchView)v.findViewById(R.id.search_view);
		
		ChannelList list = BaseApplication.getWebserviceManager().getChannelList();
		int a = 0;
    	for(Channel channel : list.getChannels()){
    		items.add(new DynamicChannelModel(a,channel.getName(),channel.getId(),channel.getIcon_path()));
    		a++;
    	}
    	
		mChannelsListView = (DynamicListView)v.findViewById(R.id.lstChannelsDynamic);
    	setUpListView();
    	mChannelsListView.setTextFilterEnabled(true);
    	setupSearchView();
    	
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_addchannels, R.string.app_ga_action_view,"");
		return v;
	}
	
	private void setUpListView(){
		setUpDragAndDrop();
	}
	
	private void setUpDragAndDrop(){
		final DynamicChannelListAdapter adapter = new DynamicChannelListAdapter(getActivity().getApplicationContext(), items, true);
		mChannelsListView.setAdapter(adapter);
		mChannelsListView.enableDragAndDrop();
		mChannelsListView.setDraggableManager(new TouchViewDraggableManager(R.id.icon));
		mChannelsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				mChannelsListView.startDragging(position);
				BaseApplication.getEventsManager().sendEvent(R.string.app_ga_addchannels, R.string.app_ga_action_click, "");

	            return true;
			}
		});
	}
	
	@Override
	public void onDetach() {
		save();
		super.onDetach();	
	}
	
	private void save(){
		ChannelOrder order = new ChannelOrder();
		order.setItems(items);
		BaseApplication.getWebserviceManager().saveChannelOrder(order);
	}

	@Override
	public boolean onQueryTextChange(String text) {
		 if (TextUtils.isEmpty(text)) {
			 mChannelsListView.clearTextFilter();
	        } else {
	        	mChannelsListView.setFilterText(text.toString());
	        }
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String text) {
		return false;
	}
	
	private void setupSearchView(){
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true);
		mSearchView.setQueryHint(getString(R.string.app_search_channels));
	}
}
