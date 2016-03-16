package es.ubiqua.zapptv.ui;

import java.util.ArrayList;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.MostViewedListAdapter;
import es.ubiqua.zapptv.adapters.item.MostViewedItem;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MostViewedListFragment extends BaseFragment{
	
	private ListView mMostViewedListView;
	
	public MostViewedListFragment(){
		super();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	mMostViewedListView = (ListView)inflater.inflate(R.layout.mostviewed_list_fragment, container, false);

    	final ArrayList<MostViewedItem> items = new ArrayList<MostViewedItem>();
    	
    	ProgramList programs = getMostViewedItems();
    	for(Program p : programs.getProgrammes()){
    		items.add(new MostViewedItem(p));
    	}
    	
    	mMostViewedListView.setAdapter(new MostViewedListAdapter(getActivity().getApplicationContext(), items));

    	mMostViewedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arrayPosition, long arg3) {
		    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_mostviewed, R.string.app_ga_action_click,"");

				openFragment(new ProgramDetailFragment(), createFragmentBundle(items.get(arrayPosition)));
			}
    		
		});
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_mostviewed, R.string.app_ga_action_view,"");
        return mMostViewedListView;
    }
    
    private Bundle createFragmentBundle(MostViewedItem item){
    	Bundle b = new Bundle();
    	b.putInt("id", item.getProgram().getIdProgramme());
    	return b;
    }
    
    private ProgramList getMostViewedItems(){
    	return BaseApplication.getWebserviceManager().getListProgramsByCheckin();
    }
}
