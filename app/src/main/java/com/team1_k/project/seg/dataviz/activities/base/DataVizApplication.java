package com.team1_k.project.seg.dataviz.activities.base;

import android.app.Application;
import android.util.Log;

import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizDbHelper;

/**
 * Created by alexstoick on 11/27/14.
 */
public class DataVizApplication extends Application {

    private static final String LOG_TAG = "base.application" ;

    @Override
    public void onCreate() {
        super.onCreate();
        DataVizDbHelper dbHelper = new DataVizDbHelper(this);
        dbHelper.getWritableDatabase();
        QueryBuilder queryBuilder = new QueryBuilder(getApplicationContext());
        queryBuilder.getMetrics();
        queryBuilder.getCountries();
        Log.d(LOG_TAG, "ran the base app");
    }
}
