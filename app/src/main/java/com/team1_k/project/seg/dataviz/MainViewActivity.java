package com.team1_k.project.seg.dataviz;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class MainViewActivity extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        TabHost tabHost = getTabHost();

        // Tab for News
        TabSpec newsActivity = tabHost.newTabSpec("News");
        // setting Title and Icon for the Tab
        newsActivity.setIndicator("News", getResources().getDrawable(R.drawable.news));
        Intent newsIntent = new Intent(this, NewsActivity.class);
        newsActivity.setContent(newsIntent);

        // Tab for ExchangeRate
        TabSpec exchangeRateActivity = tabHost.newTabSpec("ExchangeRate");
        exchangeRateActivity.setIndicator("ExchangeRate", getResources().getDrawable(R.drawable.market));
        Intent exchangeIntent = new Intent(this, ExchangeRatesActivity.class);
        exchangeRateActivity.setContent(exchangeIntent);

        // Tab for Countries
        TabSpec countriesActivity = tabHost.newTabSpec("Countries");
        countriesActivity.setIndicator("Countries", getResources().getDrawable(R.drawable.world));
        Intent countriesIntent = new Intent(this, CountrySelectionActivity.class);
        countriesActivity.setContent(countriesIntent);


        TabSpec compareActivity = tabHost.newTabSpec("Comparison");
        compareActivity.setIndicator("Comparison", getResources().getDrawable(R.drawable.more));
        Intent comparisonIntent = new Intent(this, ComparisonActivity.class);
        compareActivity.setContent(comparisonIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(newsActivity);
        tabHost.addTab(exchangeRateActivity);
        tabHost.addTab(countriesActivity);
        tabHost.addTab(compareActivity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:
                Intent intentHome = new Intent(MainViewActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(MainViewActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
            case R.id.action_Markets:
                Intent intentMarkets = new Intent(MainViewActivity.this, ExchangeRatesActivity.class);
                this.startActivity(intentMarkets);
                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(MainViewActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
            case R.id.action_More:
                Intent intentMore = new Intent(MainViewActivity.this, ComparisonActivity.class);
                this.startActivity(intentMore);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
