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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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


}
