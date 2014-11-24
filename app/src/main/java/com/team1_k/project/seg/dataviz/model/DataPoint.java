package com.team1_k.project.seg.dataviz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/23/14.
 */
public class DataPoint {

    private static final String LOG_TAG = "model.dataPoint" ;

    private double mValue ;
    private int mYear ;

    public DataPoint(JSONObject dataPoint) {
        try {
            this.mValue = Double.valueOf(dataPoint.getString("value"));
            this.mYear = Integer.valueOf(dataPoint.getString("date"));
        } catch ( JSONException e ) {
            Log.e(LOG_TAG, e.toString());
            e.printStackTrace();
        } catch ( NumberFormatException e ) {
            Log.e(LOG_TAG, e.toString());
            e.printStackTrace();
        }
    }

    public double getValue() {
        return mValue;
    }

    public int getYear() {
        return mYear;
    }
}
