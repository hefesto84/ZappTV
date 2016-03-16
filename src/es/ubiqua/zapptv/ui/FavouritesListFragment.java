package es.ubiqua.zapptv.ui;

import java.util.ArrayList;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.FavouritesListAdapter;
import es.ubiqua.zapptv.adapters.item.FavouriteItem;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavouritesListFragment extends BaseFragment{
	
	private ListView mFavouritesListView;
	
	private static final String ID_ARGUMENT = "id";
	
	public FavouritesListFragment(){
		super();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
    	mFavouritesListView = (ListView)inflater.inflate(R.layout.favourites_list_fragment, container, false);

    	final ArrayList<FavouriteItem> items = new ArrayList<FavouriteItem>();
    	ProgramList programs = BaseApplication.getWebserviceManager().getProgramListFavourites();
    	
    	for(Program program : programs.getProgrammes()){
    		items.add(new FavouriteItem(program));
    	}
    
    	mFavouritesListView.setAdapter(new FavouritesListAdapter(getActivity().getApplicationContext(), items,getActivity()));

    	mFavouritesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arrayPosition, long arg3) {
		    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_favourites, R.string.app_ga_action_click,"");
				openFragment(new ProgramDetailFragment(), createFragmentBundle(items.get(arrayPosition)));
			}
    		
		});
    	
    	BaseApplication.getEventsManager().sendEvent(R.string.app_ga_favourites, R.string.app_ga_action_view,"");
        return mFavouritesListView;
    }
    
    private Bundle createFragmentBundle(FavouriteItem item){
    	Bundle b = new Bundle();
    	b.putInt(ID_ARGUMENT, item.getProgram().getIdProgramme());
    	return b;
    }
}
