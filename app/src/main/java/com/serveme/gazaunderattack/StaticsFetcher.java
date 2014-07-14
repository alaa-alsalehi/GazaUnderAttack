package com.serveme.gazaunderattack;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;

public class StaticsFetcher implements Response.Listener<JSONArray>,
		Response.ErrorListener {
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	public static final int[] colors = { 0xff3366CC, 0xffDC3912, 0xffFF9900,
			Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.WHITE,
			Color.YELLOW };
	private Activity activity;

	public StaticsFetcher(Activity activity) {
		this.activity = activity;
		RequestQueue mRequestQueue;

		mRequestQueue = Volley.newRequestQueue(activity);
		JsonArrayRequest jr = new JsonArrayRequest(
				"http://gazaunderattack.com/GetData.aspx", this, this);

		mRequestQueue.add(jr);
	}

	@Override
	public void onResponse(JSONArray result) {
		try {

			JSONArray titles = ((JSONArray) result.get(0));
			XYSeries[] mCurrentSeries = new XYSeries[titles.length() - 1];
			for (int i = 1; i < titles.length(); i++) {
				mCurrentSeries[i - 1] = new XYSeries(titles.getString(i));
				XYSeriesRenderer mCurrentRenderer = new XYSeriesRenderer();
				mCurrentRenderer.setPointStrokeWidth(20.0f);
				mCurrentRenderer.setColor(colors[i - 1 % colors.length]);
				mCurrentRenderer.setLineWidth(3.0f);
                mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
				mCurrentRenderer.setChartValuesTextSize(30.0f);
				mRenderer.addSeriesRenderer(mCurrentRenderer);
				mDataset.addSeries(mCurrentSeries[i - 1]);
			}
			int killed = 0;
			int injured = 0;
			for (int i = 1; i < result.length(); i++) {
				JSONArray values = ((JSONArray) result.get(i));
				for (int j = 1; j < values.length(); j++) {
					int temp = Integer.parseInt(values.getString(j));
					mCurrentSeries[j - 1].add(i, temp);
					/*
					 * if (j == values.length() - 1) { mCurrentSeries[j -
					 * 1].addAnnotation( String.valueOf(temp), i - 0.15, temp -
					 * 30); }else{ mCurrentSeries[j - 1].addAnnotation(
					 * String.valueOf(temp), i + 0.15, temp + 30); }
					 */
					if (j == 2) {
						killed = temp;
					} else if (j == 1) {
						injured = temp;
					}
				}
			}
			mRenderer.setChartTitle(killed + " Palestinians killed and over\n"
					+ injured + " injured in Gaza");
			float textSize = TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 15, activity.getResources()
							.getDisplayMetrics());
			mRenderer.setChartTitleTextSize(textSize);
			mRenderer.setLabelsTextSize(textSize);
			mRenderer.setLegendTextSize(textSize);
			mRenderer.setLabelsColor(Color.BLACK);
			mRenderer.setXLabelsColor(Color.BLACK);
			mRenderer.setYLabelsColor(0, Color.BLACK);
			mRenderer.setMarginsColor(Color.WHITE);
			mRenderer.setAxesColor(Color.BLACK);
			mRenderer.setGridColor(Color.BLACK);
			mRenderer.setShowGridX(true);
			mRenderer.setXTitle("Day");
			mRenderer.setAxisTitleTextSize(textSize);
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);
            mRenderer.setXAxisMin(0);
            mRenderer.setYAxisMin(0);



            mRenderer.setLabelFormat(NumberFormat.getIntegerInstance());
			int margin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 20, activity.getResources()
							.getDisplayMetrics());
			mRenderer.setMargins(new int[] { 0, margin, margin, 0 });

			LinearLayout content = (LinearLayout) activity
					.findViewById(R.id.content);
			final GraphicalView mChart = ChartFactory.getLineChartView(
					activity, mDataset, mRenderer);
			mChart.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// handle the click event on the chart
					SeriesSelection seriesSelection = mChart
							.getCurrentSeriesAndPoint();
					if (seriesSelection != null) {
						// display information of the clicked point
						Toast.makeText(
								activity,
								"Day "
										+ (int) seriesSelection.getXValue()
										+ " "
										+ mDataset.getSeriesAt(
												seriesSelection
														.getSeriesIndex())
												.getTitle() + " = "
										+ (int) seriesSelection.getValue(),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
            double[] limits = new double[] {-1,result.length(),-1,Double.MAX_VALUE};
            mRenderer.setPanLimits(limits);
			//mRenderer.setZoomEnabled(true);
            mChart.setZoomRate(0);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 0);
			layoutParams.weight = 1;
			layoutParams.topMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 10, activity.getResources()
							.getDisplayMetrics());
			mChart.setLayoutParams(layoutParams);
			content.findViewById(R.id.progress_container).setVisibility(
					View.GONE);
			content.addView(mChart);
			content.invalidate();
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
