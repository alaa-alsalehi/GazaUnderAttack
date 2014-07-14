package com.serveme.gazaunderattack;

import java.util.ArrayList;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class YoutubeAdapter extends BaseAdapter implements
		OnInitializedListener {

	private static final String API_KEY = "AIzaSyCNoYSWsEXLIPh1SQLfQ-vGX1fxQ8YbGEU";
	private Context context;
	private Video[] videos;
	private ArrayList<YouTubeThumbnailLoader> thumbnailLoaders = new ArrayList<YouTubeThumbnailLoader>();

	private class Holder {
		YouTubeThumbnailView thumbnailView;
		TextView titleView;
		Video video;
		YouTubeThumbnailLoader loader;
	}

	public YoutubeAdapter(Context context, Video[] videos) {
		this.context = context;
		this.videos = videos;
	}

	@Override
	public int getCount() {
		return videos.length;
	}

	@Override
	public Object getItem(int index) {
		return videos[index];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.youtube_listitem, parent, false);
			holder = new Holder();
			holder.thumbnailView = (YouTubeThumbnailView) convertView
					.findViewById(R.id.thumbnail);
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			holder.thumbnailView.setTag(holder);
			holder.thumbnailView.initialize(API_KEY, this);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
			if (holder.loader != null)
				holder.loader.setVideo(videos[position].getId());
		}
		holder.thumbnailView.setImageResource(android.R.drawable.progress_horizontal);
		holder.video = videos[position];
		holder.titleView.setText(holder.video.getTitle());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                GAManager.getInstance().trackEvent("Video", "Play", ((Holder) v.getTag()).video.getTitle());

                context.startActivity(YouTubeStandalonePlayer
						.createVideoIntent((Activity) context, API_KEY,
								((Holder) v.getTag()).video.getId(), 0, true,
								true));
			}
		});
		return convertView;
	}

	@Override
	public void onInitializationFailure(YouTubeThumbnailView arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		Log.e("error", arg1.toString());
	}

	@Override
	public void onInitializationSuccess(
			YouTubeThumbnailView youTubeThumbnailView,
			YouTubeThumbnailLoader youTubeThumbnailLoader) {

		Holder holder = (Holder) youTubeThumbnailView.getTag();
		String id = holder.video.getId();
		Log.d("id", id);
		holder.loader = youTubeThumbnailLoader;
		thumbnailLoaders.add(youTubeThumbnailLoader);
		youTubeThumbnailLoader.setVideo(id);
	}

	public void releaseLoaders() {
		for (int i = 0; i < thumbnailLoaders.size(); i++) {
			thumbnailLoaders.get(i).release();
		}
	}

}
