package com.team1_k.project.seg.dataviz.graph;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team1_k.project.seg.dataviz.CountrySelectionActivity;
import com.team1_k.project.seg.dataviz.ExchangeRatesActivity;
import com.team1_k.project.seg.dataviz.MainViewActivity;
import com.team1_k.project.seg.dataviz.NewsActivity;
import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.data.DataVizContract;
import com.team1_k.project.seg.dataviz.model.DataPoint;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.LineChartView;


public class LineGraphActivity extends FragmentActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "ui.graph.line";
    public static final String TAG_COUNTRY_ID = "country_id";
    public static final String TAG_METRIC_ID = "metric_id";

    private long mCountryDatabaseId;
    private long mMetricDatabaseId;
    private ArrayList<DataPoint> mDataPoints = new ArrayList<DataPoint>() ;

    private final static int DATA_POINT_LOADER = 0 ;

    private LineGraph mGraphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mGraphFragment = new LineGraph () ;
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mGraphFragment ).commit();
        }
        mCountryDatabaseId = getIntent().getLongExtra(TAG_COUNTRY_ID,0);
        mMetricDatabaseId = getIntent().getLongExtra(TAG_METRIC_ID,0);
        getLoaderManager().initLoader(DATA_POINT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = DataVizContract.DataPointEntry.TABLE_NAME + "." + DataVizContract.DataPointEntry.COLUMN_YEAR + " DESC";
        return new CursorLoader(
                getApplicationContext(),
                DataVizContract.CountryEntry.
                        buildCountryWithMetricUriWithId(mCountryDatabaseId),
                DataVizContract.MetricEntry.COLUMNS_FOR_METRIC_QUERY,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        cursor.moveToFirst();
        mDataPoints = new ArrayList<DataPoint>();
        while ( ! cursor.isAfterLast() ) {

            long metricId = cursor.getLong(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_METRIC_ID
            ) ;
            int year = cursor.getInt(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_YEAR
            );
            Double value = cursor.getDouble(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_VALUE
            );
            if ( metricId == mMetricDatabaseId ) {
                DataPoint dataPoint = new DataPoint(value, year );
                mDataPoints.add( dataPoint) ;
            }
            cursor.moveToNext();
        }
        Log.d(LOG_TAG, String.valueOf(mDataPoints.size()));

        //send mDataPoints to graph.
        mGraphFragment.updateData(mDataPoints);
        Log.i(LOG_TAG, "reached here");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public static class LineGraph extends Fragment {

        LineChartView chart;
        LineChartData data;
        int numberOfLines = 1;
        int maxNumberOfLines = 1;
        int numberOfPoints = 0;

        float[][] randomNumbersTab ;

        boolean hasAxes = true;
        boolean hasAxesNames = true;
        boolean hasLines = true;
        boolean hasPoints = true;
        ValueShape shape = ValueShape.CIRCLE;
        boolean isFilled = false;
        boolean hasLabels = true;
        boolean isCubic = false;
        boolean hasLabelForSelected = false;
        boolean isInteractive = true;
//        ArrayList<DataPoint>

        public LineGraph() {
        }

        public void updateData(ArrayList<DataPoint> dataPoints) {
            int i = 0 ;
            numberOfPoints = dataPoints.size();
            randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
            for (int j = 0; j < numberOfPoints; ++j) {
                DataPoint currentDataPoint = dataPoints.get(j);
                randomNumbersTab[i][j] =
                        (float) currentDataPoint.getValue();
            }
            generateData();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());
            chart.setInteractive(true);
            // Disable viewpirt recalculations, see toggleCubic() method for more info.

            return rootView;
        }

        private void generateData() {

            List<Line> lines = new ArrayList<Line>();
            for (int i = 0; i < numberOfLines; ++i) {

                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    values.add(new PointValue(j, randomNumbersTab[i][j]));
                    Log.i ( LOG_TAG, String.valueOf(randomNumbersTab[i][j]));
                }

                Line line = new Line(values);
                line.setColor(Utils.COLORS[i]);
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                lines.add(line);
            }

            data = new LineChartData(lines);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Year");
                    axisY.setName("GDP");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);
        }


        private class ValueTouchListener implements LineChartView.LineChartOnValueTouchListener {

            @Override
            public void onValueTouched(int selectedLine, int selectedValue, PointValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingTouched() {
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_view, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_home:
                Intent intentHome = new Intent(LineGraphActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(LineGraphActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
            case R.id.action_Markets:
                Intent intentMarkets = new Intent(LineGraphActivity.this, ExchangeRatesActivity.class);
                this.startActivity(intentMarkets);
                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(LineGraphActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
//            case R.id.action_More:
//                Intent intentMore = new Intent(CountryDetailActivity.this, ActivityForItemOne.class);
//                this.startActivity(intentMore);
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}

