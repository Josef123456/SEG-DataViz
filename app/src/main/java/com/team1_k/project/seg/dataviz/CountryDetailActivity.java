package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract.DataPointEntry;
import com.team1_k.project.seg.dataviz.model.Client;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.Metric;


public class CountryDetailActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "ui.country.detail" ;

    protected static final String TAG_COUNTRY_ID = "COUNTRY_ID" ;
    private static final String[] DATA_POINT_COLUMNS = {
            DataPointEntry.COLUMN_YEAR,
            DataPointEntry.COLUMN_VALUE
    } ;

    private QueryBuilder mQueryBuilder;
    private Intent mIntent ;
    private Country mCountry ;
    private Client mClient ;
    private Metric[] mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);
        mIntent = getIntent();
        mQueryBuilder = new QueryBuilder(getApplicationContext());
        fetchClient();
        fetchCountry();
        fetchMetrics();
        fetchStats();
    }

    private void fetchStats() {
        for ( Metric metric: mMetrics) {
            mQueryBuilder.fetchDataForCountryAndMetric(mCountry, metric);
        }
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
        String country_api_id = mIntent.getStringExtra(TAG_COUNTRY_ID);
        try {
            mCountry = Country.getCountryWithApiId(getApplicationContext(), country_api_id);
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
        String sortOrder = DataPointEntry.COLUMN_YEAR + " DESC";
        return new CursorLoader(
                getApplicationContext(),
                DataPointEntry.CONTENT_URI,
                DATA_POINT_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


}
