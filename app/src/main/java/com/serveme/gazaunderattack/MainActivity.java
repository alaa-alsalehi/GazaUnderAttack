package com.serveme.gazaunderattack;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	PlaceSlidesFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	public static final String TAG = "detailsFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new PlaceSlidesFragmentAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setCurrentItem(mAdapter.getCount() / 2);
		((CirclePageIndicator) mIndicator).setSnap(true);

		mIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});
		new StaticsFetcher(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_facebook:
			String url = "https://www.facebook.com/hashtag/gazaunderattach";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.action_twitter:
			url = "https://twitter.com/search?q=%23GazaUnderAttack&src=tyah";
			i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.action_youtube:
			i = new Intent(this, VideoActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
