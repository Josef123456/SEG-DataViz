package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.team1_k.project.seg.dataviz.activities.comparison.ComparisonActivity;
import com.team1_k.project.seg.dataviz.activities.country.CountrySelectionActivity;
import com.team1_k.project.seg.dataviz.data_exchange_rate.RssFragment;


/**
 * Activity to show RSS Feed of News (possibly from Bloomberg)
 */

public class NewsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        RssFragment trial = new RssFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, trial).commit();

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_view, menu);
        return true;
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
                Intent intentHome = new Intent(NewsActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(NewsActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
            case R.id.action_Markets:
                Intent intentMarkets = new Intent(NewsActivity.this, ExchangeRatesActivity.class);
                this.startActivity(intentMarkets);
                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(NewsActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
            case R.id.action_More:
                Intent intentMore = new Intent(NewsActivity.this, ComparisonActivity.class);
                this.startActivity(intentMore);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


}
