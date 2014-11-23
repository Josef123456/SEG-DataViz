package com.team1_k.project.seg.dataviz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Country {

    private static final String LOG_TAG = "model.country" ;

    private String mName;
    private String mApiId ;
    private double mLatitude ;
    private double mLongitude ;
    private String mCapitalCity ;

    Metric[] metrics ;

    public String getName() {
        return mName;
    }

    public String getApiId() {
        return mApiId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getCapitalCity() {
        return mCapitalCity;
    }

    public Country(JSONObject country) {
        try {

            this.mName = country.getString("name");
            this.mApiId = country.getString("id");
            this.mLatitude = country.getDouble("latitude");
            this.mLongitude = country.getDouble("longitude");
            this.mCapitalCity = country.getString("capitalCity");

        } catch ( JSONException exception ) {
            Log.e(LOG_TAG, exception.toString());
            exception.printStackTrace();
        }
    }
}
