package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.team1_k.project.seg.dataviz.data.DataVizContract;
import com.team1_k.project.seg.dataviz.graph.BarGraphFragment;
import com.team1_k.project.seg.dataviz.graph.GraphFragment;
import com.team1_k.project.seg.dataviz.graph.LineGraphFragment;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.DataPoint;
import com.team1_k.project.seg.dataviz.model.Metric;

import java.util.ArrayList;


public class CountryComparisonDetailActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG_COUNTRY_DATABASE_IDS = "countries_database_ids" ;
    private static final String LOG_TAG = "ui.comparison.detail";
    public static final String TAG_METRIC_DATABASE_ID = "metric_database_id" ;

    private final static int DATA_POINT_LOADER = 0 ;

    long[] mCountryDatabaseIds ;
    long mMetricDatabaseId ;
    ArrayList<ArrayList<DataPoint>> mDataPoints;
    GraphFragment mGraphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_comparison_detail);
        Intent intent = getIntent();
        mGraphFragment = new LineGraphFragment();
        mCountryDatabaseIds = intent.getLongArrayExtra(TAG_COUNTRY_DATABASE_IDS);
        mMetricDatabaseId = intent.getLongExtra(TAG_METRIC_DATABASE_ID, 0);
        Log.d(LOG_TAG, String.valueOf(mMetricDatabaseId) );

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.countryComparisonDetailContainer, mGraphFragment).commit();
            getLoaderManager().initLoader(DATA_POINT_LOADER, null, this);
        } else {
            int length = savedInstanceState.getInt("dataLength");
            mDataPoints = new ArrayList<ArrayList<DataPoint>>();
            for ( int i = 0 ; i < length ; ++ i ) {
                ArrayList<DataPoint> currentArray = savedInstanceState.getParcelableArrayList("data"+i);
                mDataPoints.add(currentArray);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.comparison, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int i = 0 ;
        for( ArrayList<DataPoint> p : mDataPoints) {
            outState.putParcelableArrayList( "data" + i , p);
            ++ i;
        }
        outState.putInt("dataLength", mDataPoints.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.lineGraph: {
                mGraphFragment = LineGraphFragment.newInstance(mDataPoints);
                break;
            }
            case R.id.barGraph: {
                mGraphFragment = BarGraphFragment.newInstance(mDataPoints);
                break;
            }
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.countryComparisonDetailContainer, mGraphFragment).commit();

        return true;
    }


    private String generateQuestionMarks() {
        String returnString = "(";
        int length = mCountryDatabaseIds.length;
        for ( int i = 0 ; i < length - 1; ++ i )
            returnString += "?, " ;
        returnString += "?)";

        return returnString;
    }

    private String[] generateSelectionParams() {
        String[] returnParams = new String[1+mCountryDatabaseIds.length];
        returnParams[0] = String.valueOf(mMetricDatabaseId);
        int length = mCountryDatabaseIds.length;
        for( int i = 0 ; i < length ; ++ i )
            returnParams[i+1] = String.valueOf(mCountryDatabaseIds[i]);

        return returnParams;
    }

    private String generateSelection() {

        String returnString =
                DataVizContract.MetricEntry.TABLE_NAME
                + "." + DataVizContract.MetricEntry._ID
                + " = ?"
                + " AND "
                + DataVizContract.CountryEntry.TABLE_NAME
                + "." + DataVizContract.CountryEntry._ID + " IN "
                + generateQuestionMarks()
                + " AND "
                + DataVizContract.DataPointEntry.TABLE_NAME + "."
                + DataVizContract.DataPointEntry.COLUMN_VALUE + " > 0" ;
        Log.d ( LOG_TAG , "selection: " + returnString ) ;
        return returnString;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getApplicationContext(),
                DataVizContract.CountryEntry.buildCountriesWithMetricsUri(),
                DataVizContract.MetricEntry.COLUMNS_FOR_METRIC_QUERY,
                generateSelection() ,
                generateSelectionParams(),
                DataVizContract.MetricEntry.TABLE_NAME + "."
                        + DataVizContract.MetricEntry._ID + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        mDataPoints = new ArrayList<ArrayList<DataPoint>>();
        long lastCountryId = -1 ;
        ArrayList<DataPoint> selectedArray = new ArrayList<DataPoint>() ;
        Log.d ( LOG_TAG, "count of returns: " + String.valueOf(cursor.getCount()));
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
            String countryApiId = cursor.getString(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_COUNTRY_API_ID
            );
            String countryName = cursor.getString (
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_COUNTRY_NAME
            );
            long countryDatabaseId = cursor.getLong(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_COUNTRY_ID
            );
            Log.d ( LOG_TAG, "current country id:" + countryDatabaseId ) ;
            Metric currentMetric = new Metric(
                    metricApiId,
                    metricName,
                    metricDescription,
                    metricId
            );
            Country currentCountry = new Country(
                    countryName,
                    countryApiId,
                    countryDatabaseId
            );
            if ( lastCountryId != countryDatabaseId ) {
                selectedArray = new ArrayList<DataPoint>();
                lastCountryId = countryDatabaseId;
                mDataPoints.add(selectedArray);
            }
            DataPoint dataPoint = new DataPoint(
                    value,
                    year,
                    currentMetric,
                    currentCountry
            );
            selectedArray.add(dataPoint) ;
            cursor.moveToNext();
        }
        Log.d(LOG_TAG, String.valueOf(mDataPoints.size()));

        mGraphFragment.updateData(mDataPoints);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
