package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.team1_k.project.seg.dataviz.data.DataVizContract;
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
    LineGraphFragment mGraphFragment;

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
        }
        getLoaderManager().initLoader(DATA_POINT_LOADER, null, this);
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
