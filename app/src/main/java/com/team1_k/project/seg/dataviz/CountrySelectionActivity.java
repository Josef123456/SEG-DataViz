package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.api.QueryBuilder;

public class CountrySelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);
        QueryBuilder queryBuilder = new QueryBuilder(getApplicationContext());

        queryBuilder.getCountries();
    }
}
