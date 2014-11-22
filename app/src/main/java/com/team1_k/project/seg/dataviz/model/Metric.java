package com.team1_k.project.seg.dataviz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Metric {

    private static String LOG_TAG = "model.metric" ;

    /**
     * The id with which we grab the metric from the API, eg: AG.CON.FERT.ZS
     */
    private String mApiId;

    /**
     * The human readable version of the id, eg: Fertilizer consumption (kilograms per
     * hectare of arable land)
     */
    private String mName;

    /**
     * The description of this metric, eg: "Fertilizer consumption measures the quantity of ...
     */
    private String mDescription;

    /**
     * dataPoints - first integer represents year, second the value for that year for this metric
     */
    private HashMap<Integer,Integer> mDataPoints;

    public Metric(JSONObject metric) {
        try {
            this.mName = metric.getString("name");
            this.mDescription = metric.getString("sourceNote");
            this.mApiId = metric.getString("id");
        } catch ( JSONException e ) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.toString());
        }
    }

    public String getApiId() {
        return mApiId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

}
