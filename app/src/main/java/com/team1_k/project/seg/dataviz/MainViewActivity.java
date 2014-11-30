package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainViewActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

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
                Intent intentHome = new Intent(MainViewActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(MainViewActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
//            case R.id.action_Markets:
//                Intent intentMarkets = new Intent(MainViewActivity.this, ActivityForItemOne.class);
//                this.startActivity(intentMarkets);
//                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(MainViewActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
//            case R.id.action_More:
//                Intent intentMore = new Intent(MainViewActivity.this, ActivityForItemOne.class);
//                this.startActivity(intentMore);
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void startNewsActivity(View view) {
        // FOR CHOOSING YOUR STARTING AND END LOCATION POINTS
        Intent intentNews = new Intent(this, NewsActivity.class);
        //String message = "test string";
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intentNews);
    }

    public void startCountriesActivity(View view) {
        Intent intentCountries = new Intent(this, CountrySelectionActivity.class);
        startActivity(intentCountries);
    }

    public void startExchangeRatesActivity(View view) {
        Intent intentExchange = new Intent(this, ExchangeRatesActivity.class);
        startActivity(intentExchange);
    }


}
