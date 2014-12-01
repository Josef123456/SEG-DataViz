package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.team1_k.project.seg.dataviz.data_exchange_rate.ExchangeRate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExchangeRatesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);

        LinearLayout ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        LinearLayout ll_2 = (LinearLayout) findViewById(R.id.ll_2);

        ListView listView = (ListView) findViewById(R.id.exchange_listview);
        ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
        ExchangeRate xChange = new ExchangeRate(this, listView, pb, ll_1, ll_2);
        xChange.getRates();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exchange_rates, menu);
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