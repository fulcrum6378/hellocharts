package lecho.lib.hellocharts.samples;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.ViewportChangeListener;
import lecho.lib.hellocharts.gesture.ChartZoomer;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class PreviewColumnChartActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_column_chart);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ColumnChartView chart;
		private PreviewColumnChartView previewChart;
		private ColumnChartData data;
		/**
		 * Deep copy of data.
		 */
		private ColumnChartData previewData;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			View rootView = inflater.inflate(R.layout.fragment_preview_column_chart, container, false);

			chart = (ColumnChartView) rootView.findViewById(R.id.chart);
			previewChart = (PreviewColumnChartView) rootView.findViewById(R.id.chart_preview);

			// Generate data for previewed chart and copy of that data for preview chart.
			generateDefaultData();

			chart.setColumnChartData(data);
			// Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
			// zoom/scroll is unnecessary.
			chart.setZoomEnabled(false);
			chart.setScrollEnabled(false);

			previewChart.setColumnChartData(previewData);
			previewChart.setViewportChangeListener(new ViewportListener());

			previewXY(false);

			return rootView;
		}

		// MENU
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.preview_column_chart, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			if (id == R.id.action_reset) {
				generateDefaultData();
				chart.setColumnChartData(data);
				previewChart.setColumnChartData(previewData);
				previewXY(true);
				return true;
			}
			if (id == R.id.action_preview_both) {
				previewXY(true);
				previewChart.setZoomType(ChartZoomer.ZOOM_HORIZONTAL_AND_VERTICAL);
				return true;
			}
			if (id == R.id.action_preview_horizontal) {
				previewX();
				return true;
			}
			if (id == R.id.action_preview_vertical) {
				previewY();
				return true;
			}
			if (id == R.id.action_change_color) {
				int color = Utils.pickColor();
				while (color == previewChart.getPreviewColor()) {
					color = Utils.pickColor();
				}
				previewChart.setPreviewColor(color);
				return true;
			}
			return super.onOptionsItemSelected(item);
		}

		private void generateDefaultData() {
			int numSubcolumns = 1;
			int numColumns = 50;
			List<Column> columns = new ArrayList<Column>();
			List<ColumnValue> values;
			for (int i = 0; i < numColumns; ++i) {

				values = new ArrayList<ColumnValue>();
				for (int j = 0; j < numSubcolumns; ++j) {
					values.add(new ColumnValue((float) Math.random() * 50f + 5, Utils.pickColor()));
				}

				columns.add(new Column(values));
			}

			data = new ColumnChartData(columns);

			// Auto-generated axes with empty names.
			// data.getAxisX().setName("Axis X");
			// data.getAxisY().setName("Axis Y");

			// prepare preview data, is better to use separate deep copy for preview chart.
			// set color to grey to make preview area more visible.
			previewData = new ColumnChartData(data);
			for (Column column : previewData.getColumns()) {
				for (ColumnValue value : column.getValues()) {
					value.setColor(Utils.DEFAULT_COLOR);
				}
			}

		}

		private void previewY() {
			Viewport tempViewport = new Viewport(chart.getMaxViewport());
			float dy = tempViewport.height() / 4;
			tempViewport.inset(0, dy);
			previewChart.setViewport(tempViewport, true);
			previewChart.setZoomType(ChartZoomer.ZOOM_VERTICAL);
		}

		private void previewX() {
			Viewport tempViewport = new Viewport(chart.getMaxViewport());
			float dx = tempViewport.width() / 4;
			tempViewport.inset(dx, 0);
			previewChart.setViewport(tempViewport, true);
			previewChart.setZoomType(ChartZoomer.ZOOM_HORIZONTAL);
		}

		private void previewXY(boolean animate) {
			// Better to not modify viewport of any chart directly so create a copy.
			Viewport tempViewport = new Viewport(chart.getMaxViewport());
			// Make temp viewport smaller.
			float dx = tempViewport.width() / 4;
			float dy = tempViewport.height() / 4;
			tempViewport.inset(dx, dy);
			previewChart.setViewport(tempViewport, animate);
		}

		/**
		 * Viewport listener for preview chart(lower one). in {@link #onViewportChanged(Viewport)} method change
		 * viewport of upper chart.
		 */
		private class ViewportListener implements ViewportChangeListener {

			@Override
			public void onViewportChanged(Viewport newViewport) {
				// don't use animation, it is unnecessary when using preview chart because usually viewport changes
				// happens to often.
				chart.setViewport(newViewport, false);
			}

		}
	}
}