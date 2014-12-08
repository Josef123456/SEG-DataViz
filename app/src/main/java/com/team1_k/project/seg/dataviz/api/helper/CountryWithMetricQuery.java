package com.team1_k.project.seg.dataviz.api.helper;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract.DataPointEntry;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.DataPoint;
import com.team1_k.project.seg.dataviz.model.Metric;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/23/14.
 */
public class CountryWithMetricQuery {

    private static final String LOG_TAG = "api.helper.country&metric";

    private Country mCountry ;
    private Metric mMetric ;
    private Context mContext ;

    public CountryWithMetricQuery(Context context) {
        mContext = context ;
    }

    /**
     * Takes the country and the metric and then starts the async HTTP request
     * @param country The country we are getting info about
     * @param metric The metric for which we are getting info
     */
    public void fetchDataForCountryAndMetric ( Country country, Metric metric) {
        mCountry = country ;
        mMetric = metric ;

        asyncRequestWithCountryIdAndMetricId(country.getApiId(), metric.getApiId());
    }

    /**
     * This goes through all of the elements and parses them to
     * {@link android.content.ContentValues} and then does a
     * {@link com.team1_k.project.seg.dataviz.data.DataVizContentProvider
     * #bulkInsert(android.net.Uri, android.content.ContentValues[])} with the data to ensure
     * a low running time for the SQL query.
     * @param data {@link org.json.JSONArray} form from the request to the API
     * @throws JSONException if we can't parse the data in the expected form
     */
    private void parseMetricData(JSONArray data) throws JSONException {

        int length = data.length();
        ContentValues[] bulkContentValues = new ContentValues[length];

        for ( int i = 0 ; i < length ; ++ i ) {
            JSONObject data_point = data.getJSONObject(i);
            bulkContentValues[i] = createDataPointContentValues(new DataPoint(data_point));
        }

        mContext.getContentResolver()
                .bulkInsert(DataPointEntry.CONTENT_URI, bulkContentValues);
    }

    /**
     * Creates a ContentValues object with all of the required info for the data point.
     * @param dataPoint {@link com.team1_k.project.seg.dataviz.model.DataPoint} which we want to
     *                                                                         save in the database
     * @return a formatted {@link android.content.ContentValues} object which we can insert
     * in the database
     */
    private ContentValues createDataPointContentValues(DataPoint dataPoint){

        ContentValues dataPointContentValues = new ContentValues();

        dataPointContentValues.put(DataPointEntry.COLUMN_VALUE, dataPoint.getValue());
        dataPointContentValues.put(DataPointEntry.COLUMN_YEAR, dataPoint.getYear());
        dataPointContentValues.put(DataPointEntry.COLUMN_METRIC_ID, mMetric.getDatabaseId());
        dataPointContentValues.put(DataPointEntry.COLUMN_COUNTRY_ID, mCountry.getDatabaseId());
        Log.i(LOG_TAG, "data point for year " + dataPoint.getYear()
                + " with value " + dataPoint.getValue()
                + " for country " + mCountry.getDatabaseId() + " & metric "
                + mMetric.getDatabaseId()
        ) ;
        return dataPointContentValues;
    }

    /**
     * Based on the params passed it builds the right URL and starts fetching the results
     * async. When that is done it calls {@link #parseMetricData(org.json.JSONArray)} method.
     * @param countryId used to make the API request
     * @param metricId used to make the API request
     */
    public void asyncRequestWithCountryIdAndMetricId(String countryId, String metricId) {

        String url = QueryBuilder.API_BASE_URL + "countries/" + countryId
                + "/indicators/" + metricId + "?date=2000:2014" + "&" +
                QueryBuilder.JSON_FORMAT;

        Log.i(LOG_TAG,url);

        AsyncHttpClient.getDefaultInstance().getString(url, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if ( e != null ) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                    return ;
                }
                Log.i(LOG_TAG, result);
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject page_info = array.getJSONObject(0);
                    JSONArray data = array.getJSONArray(1);
                    parseMetricData(data);
                } catch ( JSONException ex ) {
                    Log.e(LOG_TAG, ex.toString());
                    ex.printStackTrace();
                    return;
                }
            }
        });
    }

}
