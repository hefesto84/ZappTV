package es.ubiqua.zapptv.adapters;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csform.android.uiapptemplate.model.DummyModel;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.ChannelItem;
import es.ubiqua.zapptv.adapters.item.RightNowItem;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.model.DynamicChannelModel;
import es.ubiqua.zapptv.utils.Utils;

public class DynamicRightNowListAdapter extends BaseAdapter implements Filterable, UndoAdapter, OnDismissCallback {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<RightNowItem> mDummyModelList;
	private ArrayList<RightNowItem> orig;
	private ChannelStatus mChannelStatus;
	private boolean mShouldShowDragAndDropIcon;
	
	public DynamicRightNowListAdapter(Context context, ArrayList<RightNowItem> dummyModelList, boolean shouldShowDragAndDropIcon) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDummyModelList = dummyModelList;
		mShouldShowDragAndDropIcon = shouldShowDragAndDropIcon;
		mChannelStatus = BaseApplication.getWebserviceManager().loadChannelStatus();
	}
	
	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
	}
	 
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public int getCount() {
		return mDummyModelList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDummyModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDummyModelList.get(position).getId();
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.rightnow_list_header, null);
		}

		RightNowItem p = (RightNowItem)getItem(position);
		
		TextView title = (TextView)convertView.findViewById(R.id.txtTitle);
		TextView start = (TextView)convertView.findViewById(R.id.txtStart);
		TextView end = (TextView)convertView.findViewById(R.id.txtEnd);
		ImageView icon = (ImageView)convertView.findViewById(R.id.imgChannel);
		ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.progressBar);
		
		start.setText(Utils.parseEmisionTime(p.getProgram().getStart()) + "h");
		end.setText(Utils.parseEmisionTime(p.getProgram().getStop())+"h");
		title.setText(p.getProgram().getTitle());
		
		int rId = parent.getContext().getResources().getIdentifier(p.getChannel().getIcon_path(), "drawable", "es.ubiqua.zapptv");
	
		icon.setImageResource(rId);
		progress.setProgress(Utils.getEmissionProgress(p.getProgram().getTimestamp_a(), p.getProgram().getTimestamp_b()));
		
		return convertView;
	}
	
	@Override
	public void onDismiss(@NonNull final ViewGroup listView,
			@NonNull final int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			remove(position);
		}
	}

	@Override
	public View getUndoClickView(View arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getUndoView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void remove(int position) {
		mDummyModelList.remove(position);
	}

	@Override
	public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<RightNowItem> results = new ArrayList<RightNowItem>();
                if (orig == null)
                    orig = mDummyModelList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final RightNowItem g : orig) {
                            if (g.getProgram().getTitle().toLowerCase().contains(constraint.toString()) || g.getChannel().getName().toLowerCase().contains(constraint.toString())){
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
            	mDummyModelList = (ArrayList<RightNowItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
