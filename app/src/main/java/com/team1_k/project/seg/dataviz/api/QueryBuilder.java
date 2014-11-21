package com.team1_k.project.seg.dataviz.api;

import android.content.ContentValues;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.Metric;

import org.json.JSONArray;
import org.json.JSONObject;

import com.team1_k.project.seg.dataviz.data.DataVizContract.*;

/**
 * Created by alexstoick on 11/17/14.
 */
public class QueryBuilder {

    private static final String API_BASE_URL = "http://api.worldbank.org/" ;
    private static final String JSON_FORMAT = "format=json" ;

    /**
     * Grabs specific metric for a country. API Call will look like this:
     * countries/country.iso/indicators/metric.id?per_page=10&format=json
     * @param country
     * @param metric
     * @return JSON String
     */
    public static String forParams ( Country country, Metric metric) {

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
    public static String forParams ( Country country, Metric metric, int startYear, int finalYear) {

        return "" ;

    }

    /**
     * Gets all the available metrics. API Call:
     * /source/2/indicators?format=json
     * @return JSON String
     */
    public static String getMetrics () {

        String url = API_BASE_URL + "source/2/indicators?" + JSON_FORMAT ;
        String parsedJSON  ;
        AsyncHttpClient.getDefaultInstance().getString(url, new AsyncHttpClient.StringCallback() {
            // Callback is invoked with any exceptions/errors, and the result, if available.
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if (e != null) {
                    e.printStackTrace();
                    Log.e("json", e.toString());
                    return;
                }
                JSONArray array;
                JSONObject page_info;
                JSONArray metrics ;
                try {
                    array = new JSONArray(result);
                    Log.d("json", String.valueOf(array.length()));
                    page_info = array.getJSONObject(0);
                    metrics = array.getJSONArray(1);
                    int metrics_length = metrics.length();
                    for (int i = 0 ; i < metrics_length; ++ i ){
                        JSONObject metric = metrics.getJSONObject(i);
                        Log.d("json",metric.toString());
                        ContentValues metricValues = new ContentValues();
                        metricValues.put(MetricEntry.COLUMN_NAME, metric.getString("name"));
                        metricValues.put(MetricEntry.COLUMN_DESCRIPTION, metric.getString("sourceNote"));
                        metricValues.put(MetricEntry.COLUMN_API_ID, metric.getString("id"));

                        return;
                    }
                    Log.d("json", String.valueOf(metrics.length()));
                } catch ( Exception ex ) {
                    Log.e("json", ex.toString());
                    return;
                }
            }
        });

        return "" ;
    }

}
