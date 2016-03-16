package es.ubiqua.zapptv.ui;

import java.util.ArrayList;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.MostViewedListAdapter;
import es.ubiqua.zapptv.adapters.ScheduledListAdapter;
import es.ubiqua.zapptv.adapters.item.MostViewedItem;
import es.ubiqua.zapptv.adapters.item.ScheduledItem;
import es.ubiqua.zapptv.listeners.OnSocialListener;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ScheduledListFragment extends BaseFragment implements OnSocialListener{
	
	private ListView mScheduledListView;
	private ArrayList<ScheduledItem> items;
	private ScheduledListAdapter adapter;
	
	public ScheduledListFragment(){
		super();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	mScheduledListView = (ListView)inflater.inflate(R.layout.scheduled_list_fragment, container, false);

    	items = new ArrayList<ScheduledItem>();
    	
    	ProgramList programs = getScheduledItems();
    	for(Program p : programs.getProgrammes()){
    		items.add(new ScheduledItem(p));
    	}
    	
    	adapter = new ScheduledListAdapter(getActivity().getApplicationContext(), items, getActivity(),this);
    	mScheduledListView.setAdapter(adapter);

    	mScheduledListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arrayPosition, long arg3) {
		    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_scheduler, R.string.app_ga_action_click,"");

				openFragment(new ProgramDetailFragment(), createFragmentBundle(items.get(arrayPosition)));
			}
    		
		});
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_scheduler, R.string.app_ga_action_view,"");
        return mScheduledListView;
    }
    
    private Bundle createFragmentBundle(ScheduledItem item){
    	Bundle b = new Bundle();
    	b.putInt("id", item.getProgram().getIdProgramme());
    	return b;
    }
    
    private ProgramList getScheduledItems(){
    	return BaseApplication.getWebserviceManager().getListProgramsByAlarm();
    }

    @Override
	public void onAlarmDeleted(int id) {
		for(int i = 0; i<mScheduledListView.getAdapter().getCount(); i++){
			ScheduledItem si = (ScheduledItem)mScheduledListView.getAdapter().getItem(i);
			if(si.getProgram().getIdBroadcast()==id){
				BaseApplication.getEventsManager().show(getString(R.string.app_scheduled_alarm_deleted));
				BaseApplication.getWebserviceManager().deleteReminder(si.getProgram());
				items.remove(i);
				adapter.notifyDataSetChanged();
			}
		}
	}
    
	@Override
	public void onAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRated(boolean status, int rating) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFavourite(boolean status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckin(boolean status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShared() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSharedBuilt(String url) {
		// TODO Auto-generated method stub
		
	}

	

}
