package com.serveme.gazaunderattack;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class VideoActivity extends ActionBarActivity {

	private YoutubeAdapter youtubeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		new VideosFetcher(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

    @Override
    protected void onResume() {
        super.onResume();
        GAManager.getInstance().trackScreen("Video Screen");
    }

    public void setVideoes(Video[] videos) {
		ListView videoList = (ListView) findViewById(R.id.video_list);
		youtubeAdapter = new YoutubeAdapter(this, videos);
		videoList.setAdapter(youtubeAdapter);
		videoList.setVisibility(View.VISIBLE);
		findViewById(R.id.progress_container).setVisibility(View.GONE);
	}
	
	@Override
	protected void onDestroy() {
		youtubeAdapter.releaseLoaders();
		super.onDestroy();
	}

}
