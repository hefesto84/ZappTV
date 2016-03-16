package es.ubiqua.zapptv.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppDiaFragmentPagerAdapter extends FragmentPagerAdapter{

	List<Fragment> fragments;
	
	public AppDiaFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
	}

	public void addFragment(Fragment fragment){
		this.fragments.add(fragment);
	}
	
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}
