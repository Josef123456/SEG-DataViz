package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.team1_k.project.seg.dataviz.adapters.DataPointArrayAdapter;
import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract;
import com.team1_k.project.seg.dataviz.data.DataVizContract.DataPointEntry;
import com.team1_k.project.seg.dataviz.graph.LineGraphActivity;
import com.team1_k.project.seg.dataviz.model.Client;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.DataPoint;
import com.team1_k.project.seg.dataviz.model.Metric;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class CountryDetailActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "ui.country.detail" ;

    public static final String TAG_COUNTRY_API_ID = "COUNTRY_ID" ;

    private static final int DATA_POINT_LOADER = 0 ;
    private static final String[] DATA_POINT_COLUMNS = {
            DataPointEntry.COLUMN_YEAR,
            DataPointEntry.COLUMN_VALUE
    } ;

    private QueryBuilder mQueryBuilder;
    private Intent mIntent ;
    private Country mCountry ;
    private String mCountryApiId ;
    private Client mClient ;
    private Metric[] mMetrics;
    private ArrayAdapter mDataPointAdapter;
    private ArrayList<DataPoint> mDataPoints = new ArrayList<DataPoint>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);
        if ( savedInstanceState != null && savedInstanceState.containsKey(TAG_COUNTRY_API_ID)) {
            mCountryApiId = savedInstanceState.getString(TAG_COUNTRY_API_ID);
        } else {
            mIntent = getIntent();
            mCountryApiId = mIntent.getStringExtra(TAG_COUNTRY_API_ID);
        }
        mQueryBuilder = new QueryBuilder(getApplicationContext());
        Log.d ( LOG_TAG , "creating country detail" );
        fetchCountry();
        fetchClient();
        fetchMetrics();
        fetchStats();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_view, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_home:
                Intent intentHome = new Intent(CountryDetailActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(CountryDetailActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
            case R.id.action_Markets:
                Intent intentMarkets = new Intent(CountryDetailActivity.this, ExchangeRatesActivity.class);
                this.startActivity(intentMarkets);
                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(CountryDetailActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
            case R.id.action_More:
                Intent intentMore = new Intent(CountryDetailActivity.this,ComparisonActivity.class);
                this.startActivity(intentMore);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_COUNTRY_API_ID, mCountry.getApiId());
    }

    private void fetchStats() {
        for ( Metric metric: mMetrics) {
            mQueryBuilder.fetchDataForCountryAndMetric(mCountry, metric);
        }
        mDataPointAdapter = new DataPointArrayAdapter(
                getApplicationContext(),
                R.layout.list_row_country_detail_data_point,
                new DataPoint[] {}
        );
        ListView listView = (ListView) findViewById(R.id.countryDataPointListView);
        listView.setAdapter(mDataPointAdapter);
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), LineGraphActivity.class)
                        .putExtra(LineGraphActivity.TAG_COUNTRY_ID, mCountry.getDatabaseId())
                        .putExtra(LineGraphActivity.TAG_METRIC_ID, mMetrics[i].getDatabaseId())
                        .putExtra(LineGraphActivity.TAG_COUNTRY_API_ID, mCountry.getApiId());
                startActivity(intent);
            }
        });
    }

    private void fetchMetrics() {
        String[] metric_api_ids = mClient.getType().getInterests() ;
        int length = metric_api_ids.length ;
        mMetrics = new Metric[length];
        try {
            for (int i = 0; i < length; ++i) {
                mMetrics[i] = Metric.getMetricWithApiId(getApplicationContext(), metric_api_ids[i]);
            }
        } catch ( Exception e ) {
            Log.e( LOG_TAG , e.toString());
            e.printStackTrace();
        }
    }

    private void fetchCountry() {
        try {
            Log.d ( LOG_TAG, "in fetch country");
            mCountry = Country.getCountryWithApiId(getApplicationContext(), mCountryApiId);
            getLoaderManager().initLoader(DATA_POINT_LOADER, null, this);
            TextView countryName = (TextView) findViewById(R.id.countryName);
            countryName.setText(mCountry.getName());
            Log.d ( LOG_TAG, "loader should start");
        } catch ( Exception e ) {
            Log.e(LOG_TAG, e.toString() );
            e.printStackTrace();
        }
    }

    private void fetchClient() {
        //TODO: get this from somewhere in the app
        mClient = new Client(Client.Type.INVESTOR);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = DataPointEntry.TABLE_NAME + "." + DataPointEntry.COLUMN_YEAR + " DESC";
        return new CursorLoader(
                getApplicationContext(),
                DataVizContract.CountryEntry.
                        buildCountryWithMetricsUriWithId(mCountry.getDatabaseId()),
                DataVizContract.MetricEntry.COLUMNS_FOR_METRIC_QUERY,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        //need to only show the first entry for each metric
        ArrayList<Long> selectedMetricIds = new ArrayList<Long>();

        cursor.moveToFirst();
        mDataPoints = new ArrayList<DataPoint>();
        while ( ! cursor.isAfterLast() ) {

            long metricId = cursor.getLong(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_METRIC_ID
            );
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
            if ( ! selectedMetricIds.contains(metricId) ) {
                selectedMetricIds.add(metricId);
                DataPoint dataPoint = new DataPoint(
                        value,
                        year,
                        new Metric( metricApiId, metricName, metricDescription, metricId)
                );
                mDataPoints.add( dataPoint) ;
            }
            cursor.moveToNext();
        }
        mDataPointAdapter.clear();
        mDataPointAdapter.addAll(mDataPoints);
        Log.d(LOG_TAG, String.valueOf(mDataPoints.size()));
        mDataPointAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mDataPointAdapter.clear();
        mDataPointAdapter.notifyDataSetChanged();
    }

}
