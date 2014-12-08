package com.team1_k.project.seg.dataviz;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.team1_k.project.seg.dataviz.data.DataVizContract;
import com.team1_k.project.seg.dataviz.graph.LineGraphFragment;
import com.team1_k.project.seg.dataviz.model.DataPoint;
import com.team1_k.project.seg.dataviz.model.Metric;

import java.util.ArrayList;


public class CountryWithMetricActivity extends FragmentActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "ui.graph.line";
    public static final String TAG_COUNTRY_ID = "country_id";
    public static final String TAG_METRIC_ID = "metric_id";
    public static final String TAG_COUNTRY_API_ID = "country_api_id";

    private long mCountryDatabaseId;
    private long mMetricDatabaseId;
    private String mCountryApiId;
    private ArrayList<DataPoint> mDataPoints = new ArrayList<DataPoint>() ;

    private final static int DATA_POINT_LOADER = 0 ;

    private LineGraphFragment mGraphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_with_metric);
        mGraphFragment = new LineGraphFragment () ;
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.countryWithMetricContainer, mGraphFragment).commit();
        }
        mCountryDatabaseId = getIntent().getLongExtra(TAG_COUNTRY_ID,0);
        mMetricDatabaseId = getIntent().getLongExtra(TAG_METRIC_ID,0);
        mCountryApiId = getIntent().getStringExtra(TAG_COUNTRY_API_ID);
        getLoaderManager().initLoader(DATA_POINT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = DataVizContract.DataPointEntry.TABLE_NAME + "." + DataVizContract.DataPointEntry.COLUMN_YEAR + " ASC";
        return new CursorLoader(
                getApplicationContext(),
                DataVizContract.CountryEntry.
                        buildCountryWithMetricId(mCountryDatabaseId, mMetricDatabaseId),
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
            String metricName = cursor.getString(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_NAME
            );
            String metricDescription = cursor.getString(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_DESCRIPTION
            );
            String metricApiId = cursor.getString(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_API_ID
            );
            if ( metricId == mMetricDatabaseId ) {
                DataPoint dataPoint = new DataPoint(
                        value,
                        year,
                        new Metric( metricApiId, metricName, metricDescription, metricId)
                );
                mDataPoints.add( dataPoint) ;
            }
            cursor.moveToNext();
        }
        Log.d(LOG_TAG, String.valueOf(mDataPoints.size()));

        //send mDataPoints to graph.
        ArrayList<ArrayList<DataPoint>> x = new ArrayList<ArrayList<DataPoint>>() ;
        x.add(mDataPoints);
        mGraphFragment.updateData(x);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }
}

