package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class CountryComparisonDetailActivity extends Activity {

    public static final String TAG_COUNTRY_DATABASE_IDS = "countries_database_ids";
    private static final String LOG_TAG = "ui.comparison.detail";
    public static final String TAG_METRIC_DATABASE_ID = "metric_database_id";
    long[] mCountryDatabaseIds;
    long mMetricDatabaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_comparison_detail);
        Intent intent = getIntent();
        mCountryDatabaseIds = intent.getLongArrayExtra(TAG_COUNTRY_DATABASE_IDS);
        mMetricDatabaseId = intent.getLongExtra(TAG_METRIC_DATABASE_ID, 0);
        Log.d(LOG_TAG, String.valueOf(mMetricDatabaseId));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_home:
                Intent intentHome = new Intent(CountryComparisonDetailActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(CountryComparisonDetailActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
            case R.id.action_Markets:
                Intent intentMarkets = new Intent(CountryComparisonDetailActivity.this, ExchangeRatesActivity.class);
                this.startActivity(intentMarkets);
                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(CountryComparisonDetailActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
            case R.id.action_More:
                Intent intentMore = new Intent(CountryComparisonDetailActivity.this, ComparisonActivity.class);
                this.startActivity(intentMore);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
