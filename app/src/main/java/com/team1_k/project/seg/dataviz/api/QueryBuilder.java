package com.team1_k.project.seg.dataviz.api;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.Metric;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.team1_k.project.seg.dataviz.data.DataVizContract.*;

/**
 * Created by alexstoick on 11/17/14.
 */
public class QueryBuilder {

    private static final String API_BASE_URL = "http://api.worldbank.org/" ;
    private static final String JSON_FORMAT = "format=json" ;
    private static final String LOG_TAG = "api.query_builder" ;

    private Context mContext;


    public QueryBuilder(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Grabs specific metric for a country. API Call will look like this:
     * countries/country.iso/indicators/metric.id?per_page=10&format=json
     * @param country
     * @param metric
     * @return JSON String
     */
    public String forParams ( Country country, Metric metric) {

        return "" ;

    }

    /**
     * Grabs specific metric for a country bound by years. API call will look like this
     * /countries/country.iso/indicators/metric.id?per_page=10&date=startYear:endYear&format=json
     * @param country
     * @param metric
     * @param startYear
     * @param finalYear
     * @return JSON String
     */
    public String forParams ( Country country, Metric metric, int startYear, int finalYear) {

        return "" ;

    }

    private long addMetric(String name, String description, String api_id){

        Log.d(LOG_TAG, "inserting " + api_id );

        Cursor cursor = mContext.getContentResolver().query(
                MetricEntry.CONTENT_URI,
                new String[] { MetricEntry._ID },
                MetricEntry.COLUMN_NAME + " = ? ",
                new String[] { name },
                null
        );

        if ( cursor.moveToFirst() ){
            int metricIdIndex = cursor.getColumnIndex(MetricEntry._ID);
            long _id = cursor.getLong(metricIdIndex);
            Log.d(LOG_TAG, "found metric with " + api_id + " in DB at " + String.valueOf(_id) ) ;
            return _id ;
        } else {


            Uri insert_uri = mContext.getContentResolver().insert(MetricEntry.CONTENT_URI, metricValues);
            return ContentUris.parseId(insert_uri);
        }
    }

    private ContentValues createMetricContentValues(Metric metric) {
        ContentValues metricValues = new ContentValues();
        metricValues.put(MetricEntry.COLUMN_NAME, metric.getName());
        metricValues.put(MetricEntry.COLUMN_DESCRIPTION,metric.getDescription());
        metricValues.put(MetricEntry.COLUMN_API_ID, metric.getApiId());
        return metricValues ;
    }

    private void parseMetrics(JSONArray metrics) throws JSONException {
        int metrics_length = metrics.length();

        ContentValues[] bulkContentValues = new ContentValues[metrics_length] ;
        for (int i = 0 ; i < metrics_length; ++ i ){
            JSONObject metric = metrics.getJSONObject(i);
            bulkContentValues[i] = createMetricContentValues(new Metric(metric));
            Log.i( LOG_TAG, "Metric with ID: " +  _id ) ;
        }
        
    }

    private void asyncMetricRequestWithPage(int page)
    {
        String url = API_BASE_URL + "source/2/indicators?page=" + String.valueOf(page) + "&" + JSON_FORMAT ;

        AsyncHttpClient.getDefaultInstance().getString(url, new AsyncHttpClient.StringCallback() {
            // Callback is invoked with any exceptions/errors, and the result, if available.
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if (e != null) {
                    e.printStackTrace();
                    Log.e("json", e.toString());
                    return;
                }
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject page_info = array.getJSONObject(0);
                    JSONArray metrics = array.getJSONArray(1);
                    parseMetrics(metrics);
                } catch ( Exception ex ) {
                    Log.e(LOG_TAG, ex.toString());
                    return;
                }
            }
        });
    }

    /**
     * Gets all the available metrics. API Call:
     * /source/2/indicators?format=json
     * @return JSON String
     */
    public void getMetrics () {
        for (int i = 1; i<2; ++ i)
            asyncMetricRequestWithPage(i);
    }

}
