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
import android.widget.TextView;

import com.csform.android.uiapptemplate.model.DummyModel;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.ChannelItem;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.model.DynamicChannelModel;

public class DynamicChannelListAdapter extends BaseAdapter implements Filterable, Swappable, UndoAdapter, OnDismissCallback {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<DynamicChannelModel> mDummyModelList;
	private ArrayList<DynamicChannelModel> orig;
	private ChannelStatus mChannelStatus;
	private boolean mShouldShowDragAndDropIcon;
	
	public DynamicChannelListAdapter(Context context, ArrayList<DynamicChannelModel> dummyModelList, boolean shouldShowDragAndDropIcon) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDummyModelList = dummyModelList;
		mShouldShowDragAndDropIcon = shouldShowDragAndDropIcon;
		mChannelStatus = BaseApplication.getWebserviceManager().loadChannelStatus();
		//Log.d(BaseApplication.TAG,"channel status: "+mChannelStatus.toString());
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.channel_dynamic_list_header, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.channelIcon);
			holder.text = (TextView) convertView.findViewById(R.id.channelName);
			holder.checkBox = (ImageView)convertView.findViewById(R.id.chkStatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final DynamicChannelModel dm = mDummyModelList.get(position);
		
		holder.text.setText(dm.getName());
		int rId = parent.getContext().getResources().getIdentifier(dm.getIcon_path(), "drawable", "es.ubiqua.zapptv");
		holder.image.setImageResource(rId);
		
		Channel c = new Channel();
		c.setId(dm.getChannelId());
	
		if(BaseApplication.getWebserviceManager().isChannelFavourite(c)){
			holder.checkBox.setImageResource(R.drawable.checkbox_on);
		}else{
			holder.checkBox.setImageResource(R.drawable.checkbox_off);
		}
		
		holder.checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				ImageView button = (ImageView)view;
				Channel c = new Channel();
				c.setId(dm.getChannelId());
				
				if(BaseApplication.getWebserviceManager().isChannelFavourite(c)){
					button.setImageResource(R.drawable.checkbox_off);
					BaseApplication.getWebserviceManager().unsetFavourite(c);
				}else{
					button.setImageResource(R.drawable.checkbox_on);
					BaseApplication.getWebserviceManager().setFavourite(c);
				}
			}
		});
	
		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public TextView text;
		public ImageView checkBox;
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

	@Override
	public void swapItems(int positionOne, int positionTwo) {
		Collections.swap(mDummyModelList, positionOne, positionTwo);
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
                final ArrayList<DynamicChannelModel> results = new ArrayList<DynamicChannelModel>();
                if (orig == null)
                    orig = mDummyModelList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final DynamicChannelModel g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
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
            	mDummyModelList = (ArrayList<DynamicChannelModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
