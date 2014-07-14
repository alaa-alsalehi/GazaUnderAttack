package com.serveme.gazaunderattack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

public class PlaceSlidesFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

	private int[] Images = new int[] { R.drawable.gaza1, R.drawable.gaza2,
			R.drawable.gaza3

	};

	private int mCount = Images.length;

	public PlaceSlidesFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
        PlaceSlideFragment placeSlideFragment = new PlaceSlideFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("image",Images[position]);
        placeSlideFragment.setArguments(bundle);
        return placeSlideFragment;
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