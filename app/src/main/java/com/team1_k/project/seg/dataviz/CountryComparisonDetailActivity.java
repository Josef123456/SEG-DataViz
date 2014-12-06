package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class CountryComparisonDetailActivity extends Activity {

    public static final String TAG_COUNTRY_DATABASE_IDS = "countries_database_ids" ;
    private static final String LOG_TAG = "ui.comparison.detail";
    public static final String TAG_METRIC_DATABASE_ID = "metric_database_id" ;
    long[] mCountryDatabaseIds ;
    long mMetricDatabaseId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_comparison_detail);
        Intent intent = getIntent();
        mCountryDatabaseIds = intent.getLongArrayExtra(TAG_COUNTRY_DATABASE_IDS);
        mMetricDatabaseId = intent.getLongExtra(TAG_METRIC_DATABASE_ID, 0);
        Log.d(LOG_TAG, String.valueOf(mMetricDatabaseId) );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_country_comparison_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
