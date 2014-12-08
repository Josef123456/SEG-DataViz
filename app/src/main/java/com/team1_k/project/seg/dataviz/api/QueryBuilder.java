package com.team1_k.project.seg.dataviz.api;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.api.helper.CountryQuery;
import com.team1_k.project.seg.dataviz.api.helper.CountryWithMetricQuery;
import com.team1_k.project.seg.dataviz.api.helper.MetricQuery;
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

    public static final String API_BASE_URL = "http://api.worldbank.org/" ;
    public static final String JSON_FORMAT = "format=json" ;
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
    public void fetchDataForCountryAndMetric ( Country country, Metric metric) {
        new CountryWithMetricQuery(mContext).fetchDataForCountryAndMetric(country, metric);
    }

    /**
     * Grabs all of the countries available in the API.
     */
    public void getCountries () {
        CountryQuery helper = new CountryQuery(mContext);

        for ( int i = 1 ; i < 6 ; ++ i ) {
            helper.asyncCountryRequestWithPage(i);
        }
    }

    /**
     * Gets all the available metrics. API Call:
     */
    public void getMetrics () {
        MetricQuery helper = new MetricQuery(mContext);

        for (int i = 1; i<25; ++ i) {
            helper.asyncMetricRequestWithPage(i);
        }
    }

}
