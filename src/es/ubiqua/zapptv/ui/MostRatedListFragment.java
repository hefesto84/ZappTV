package es.ubiqua.zapptv.ui;

import java.util.ArrayList;
import java.util.List;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.MostRatedListAdapter;
import es.ubiqua.zapptv.adapters.item.MostRatedItem;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MostRatedListFragment extends BaseFragment{
	
	private ListView mMostRatedListView;
	
	public MostRatedListFragment(){
		super();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	ProgramList programs = getMostRatedItems();
    	
    	mMostRatedListView = (ListView)inflater.inflate(R.layout.mostrated_list_fragment, container, false);

    	final ArrayList<MostRatedItem> items = new ArrayList<MostRatedItem>();
    	
    	for(Program p : programs.getProgrammes()){
    		items.add(new MostRatedItem(p));
    	}
    	
    	mMostRatedListView.setAdapter(new MostRatedListAdapter(getActivity().getApplicationContext(), items,getActivity()));
    	mMostRatedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arrayPosition, long arg3) {
		    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_mostrated, R.string.app_ga_action_click,"");

				openFragment(new ProgramDetailFragment(), createFragmentBundle(items.get(arrayPosition)));
			}
    		
		});
    	
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_mostrated, R.string.app_ga_action_view,"");
        return mMostRatedListView;
    }

    private Bundle createFragmentBundle(MostRatedItem item){
    	Bundle b = new Bundle();
    	b.putInt("id", item.getProgram().getIdProgramme());
    	return b;
    }
    
    private ProgramList getMostRatedItems(){
    	return BaseApplication.getWebserviceManager().getListProgramsByScore();
    }
}
