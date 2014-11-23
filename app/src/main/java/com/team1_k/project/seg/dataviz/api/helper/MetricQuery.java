package com.team1_k.project.seg.dataviz.api.helper;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract;
import com.team1_k.project.seg.dataviz.model.Metric;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/23/14.
 */
public class MetricQuery {

    private static final String LOG_TAG = "api.helper.metric" ;

    private ContentValues createMetricContentValues(Metric metric) {
        ContentValues metricValues = new ContentValues();
        metricValues.put(DataVizContract.MetricEntry.COLUMN_NAME, metric.getName());
        metricValues.put(DataVizContract.MetricEntry.COLUMN_DESCRIPTION,metric.getDescription());
        metricValues.put(DataVizContract.MetricEntry.COLUMN_API_ID, metric.getApiId());
        Log.i(LOG_TAG, "metric with " + metric.getApiId());
        return metricValues ;
    }

    private void parseMetrics(JSONArray metrics) throws JSONException {
        int metrics_length = metrics.length();

        ContentValues[] bulkContentValues = new ContentValues[metrics_length] ;
        for (int i = 0 ; i < metrics_length; ++ i ){
            JSONObject metric = metrics.getJSONObject(i);
            bulkContentValues[i] = createMetricContentValues(new Metric(metric));
        }
        mContext.getContentResolver().bulkInsert(DataVizContract.MetricEntry.CONTENT_URI, bulkContentValues);
    }

    public void asyncMetricRequestWithPage(int page)
    {
        String url = QueryBuilder.API_BASE_URL + "source/2/indicators?page=" + String.valueOf(page) + "&" + QueryBuilder.JSON_FORMAT ;

        AsyncHttpClient.getDefaultInstance().getString(url, new AsyncHttpClient.StringCallback() {
            // Callback is invoked with any exceptions/errors, and the result, if available.
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if (e != null) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                    return;
                }
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject page_info = array.getJSONObject(0);
                    JSONArray metrics = array.getJSONArray(1);
                    parseMetrics(metrics);
                } catch ( JSONException ex ) {
                    Log.e(LOG_TAG, ex.toString());
                    return;
                }
            }
        });
    }

    private Context mContext ;

    public MetricQuery(Context mContext) {
        this.mContext = mContext;
    }
}
