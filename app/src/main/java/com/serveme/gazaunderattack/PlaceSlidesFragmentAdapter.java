package com.serveme.gazaunderattack;

import com.viewpagerindicator.IconPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PlaceSlidesFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

	private int[] Images = new int[] { R.drawable.gaza1, R.drawable.gaza2,
			R.drawable.gaza3

	};

	protected static final int[] ICONS = new int[] { R.drawable.gaza1 };

	private int mCount = Images.length;

	public PlaceSlidesFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return new PlaceSlideFragment(Images[position]);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getIconResId(int index) {
		
		return R.drawable.gaza1;
	}
}