package com.team1_k.project.seg.dataviz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/23/14.
 */
public class DataPoint {

    private static final String LOG_TAG = "model.dataPoint" ;

    private double mValue = 0 ;
    private int mYear = 0 ;

    public DataPoint(JSONObject dataPoint) {
        Log.i(LOG_TAG, dataPoint.toString());
        try {
            this.mValue = Double.valueOf(dataPoint.getString("value"));
            this.mYear = Integer.valueOf(dataPoint.getString("date"));
        } catch ( JSONException e) {
            Log.e(LOG_TAG, e.toString());
            e.printStackTrace();
        } catch ( NumberFormatException e ) {
            Log.e(LOG_TAG, e.toString());
            e.printStackTrace();
        }
        Log.i(LOG_TAG, this.toString());
    }

    public DataPoint(double mValue, int mYear) {
        this.mValue = mValue;
        this.mYear = mYear;
    }

    public String toString() {
        return String.valueOf(mYear) + " - " + String.valueOf(mValue);
    }

    public double getValue() {
        return mValue;
    }

    public int getYear() {
        return mYear;
    }
}
