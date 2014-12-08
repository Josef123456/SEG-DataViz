package com.team1_k.project.seg.dataviz.activities.base;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.activities.comparison.ComparisonActivity;
import com.team1_k.project.seg.dataviz.activities.country.CountrySelectionActivity;
import com.team1_k.project.seg.dataviz.exchange_rate.ExchangeRatesActivity;
import com.team1_k.project.seg.dataviz.news.NewsActivity;

/**
 * The MainViewActivity is showing the content of NewsActivity,
 * ExchangeRateActivity, CountrySelectionActivity and ComparisonActivity in 4 tabs.
 * The user can easily click on the tabs to change the activity.
 * @author Team2K
 */
public class MainViewActivity extends TabActivity {

    /**
     * This method is going to set the content of the 4 activities and starts the relevant intents
     * once the user click on the corresponding tabs
     *
     * @param savedInstanceState
     */
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
        TabSpec exchangeRateActivity = tabHost.newTabSpec("Exchange Rate");
        exchangeRateActivity.setIndicator("Exchange Rate", getResources().getDrawable(R.drawable.market));
        Intent exchangeIntent = new Intent(this, ExchangeRatesActivity.class);
        exchangeRateActivity.setContent(exchangeIntent);

        // Tab for Countries
        TabSpec countriesActivity = tabHost.newTabSpec("Countries");
        countriesActivity.setIndicator("Countries", getResources().getDrawable(R.drawable.world));
        Intent countriesIntent = new Intent(this, CountrySelectionActivity.class);
        countriesActivity.setContent(countriesIntent);

        // Tab for Comparison
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

    /**
     * Inside the menu the user can easily change the activities by selecting the menu items.
     * There are five cases inside the switch statement.
     * The user can go to the main page (home), to see the news, exchange rate, countries and comparing the countries.
     *
     * @param item
     * @return
     */
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
