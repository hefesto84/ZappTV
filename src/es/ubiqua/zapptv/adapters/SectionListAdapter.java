package es.ubiqua.zapptv.adapters;

import java.util.HashMap;
import java.util.List;

import com.loopj.android.image.SmartImageView;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.adapters.item.SectionItem;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SectionListAdapter extends ArrayAdapter{

	private boolean isAppRecomended = false;
	
	public SectionListAdapter(Context context, List objects) {
		super(context, 0, objects);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		if(((SectionItem)getItem(position)).getName().contains(BaseApplication.getInstance().getApplicationContext().getString(R.string.section_recommended_app_item))){
			isAppRecomended = true;
		}else{
			isAppRecomended = false;
		}
		
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(!isAppRecomended){
            	convertView = inflater.inflate(R.layout.section_item_layout, null);
            	TextView name = (TextView) convertView.findViewById(R.id.sectionName);

                SectionItem item = (SectionItem) getItem(position);
                name.setText(item.getName());
            }else{
            	convertView = inflater.inflate(R.layout.recommended_app_item, null);
            	if(BaseApplication.getInstance().DiscoveryHasItems()){
            		SectionItem item = (SectionItem) getItem(position);
            		TextView section = (TextView) convertView.findViewById(R.id.sectionName);
            		section.setText(BaseApplication.getInstance().getApplicationContext().getString(R.string.section_recommended_app));
            		HashMap<String,String> element = BaseApplication.getInstance().DiscoveryGetElement(0);
            		TextView name = (TextView)convertView.findViewById(R.id.txtRecommendedAppTitle);
            		name.setText(element.get("nombre"));
            		SmartImageView image = (SmartImageView)convertView.findViewById(R.id.imgRecommendedAppImage);
            		image.setImageUrl(element.get("thumbnail"));
            	}
            }
        }

        return convertView;
    }
}
