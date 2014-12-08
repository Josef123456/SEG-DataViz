package com.team1_k.project.seg.dataviz.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.team1_k.project.seg.dataviz.data.DataVizContract.MetricEntry;

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

    private long mDatabaseId;

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

    public long getDatabaseId() {
        return mDatabaseId;
    }

    public Metric(String mApiId, String mName,
                   String mDescription, long mDatabaseId) {
        this.mApiId = mApiId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mDatabaseId = mDatabaseId;
    }

    public Metric(String mApiId, long mDatabaseId) {
        this.mApiId = mApiId;
        this.mDatabaseId = mDatabaseId;
    }

    /**
     * Constructor which takes the API id and queries the database for the metric
     * @param context Required to be able to query the database
     * @param apiId the API ID for which we want to query
     * @return a metric entry with the info from the database
     * @throws Exception In case we can't find the metric in the database, an exception is thrown
     */
    public static Metric getMetricWithApiId(Context context, String apiId) throws Exception{

        Cursor cursor = context.getContentResolver().query(
                MetricEntry.CONTENT_URI,
                MetricEntry.COLUMNS,
                MetricEntry.COLUMN_API_ID + " = ?",
                new String[] { apiId },
                null
        );
        if ( cursor.moveToFirst() ) {
            String name = cursor.getString(MetricEntry.INDEX_COLUMN_NAME);
            String description = cursor.getString(MetricEntry.INDEX_COLUMN_DESCRIPTION);
            long databaseId = cursor.getLong(MetricEntry.INDEX_COLUMN_ID);
            return new Metric(apiId, name, description, databaseId);
        }
        throw new Exception("Can't find metric with api_id " + apiId ) ;
    }

}
