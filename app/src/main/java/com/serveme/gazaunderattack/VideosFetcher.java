package com.serveme.gazaunderattack;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;

public class VideosFetcher implements Response.Listener<JSONArray>,
		Response.ErrorListener {
	private Activity activity;

	public VideosFetcher(Activity activity) {
		this.activity = activity;
		RequestQueue mRequestQueue;

		mRequestQueue = Volley.newRequestQueue(activity);
		JsonArrayRequest jr = new JsonArrayRequest(
				"http://gazaunderattack.com/GetVideos.aspx", this, this);

		mRequestQueue.add(jr);
	}

	@Override
	public void onResponse(JSONArray result) {
		try {
			Video[] videos = new Video[result.length()];
			for (int i = 0; i < result.length(); i++) {
				JSONArray values = ((JSONArray) result.get(i));
				String id = values.getString(0).split("v=")[1].split("&")[0];
				Video video = new Video(values.getString(1), id);
				videos[i] = video;
			}
			((VideoActivity) activity).setVideoes(videos);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		LinearLayout content = (LinearLayout) activity
				.findViewById(R.id.content);
		TextView textView = new TextView(activity);
		textView.setText(R.string.network_failure);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0);
		layoutParams.weight = 1;
		textView.setGravity(Gravity.CENTER);
		content.addView(textView, layoutParams);
		content.findViewById(R.id.progress_container).setVisibility(View.GONE);
		content.invalidate();
	}

}
