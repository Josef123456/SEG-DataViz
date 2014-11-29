package com.team1_k.project.seg.dataviz.data_exchange_rate;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.ExchangeRatesActivity;
import com.team1_k.project.seg.dataviz.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dbrisingr on 26/11/14.
 */
public class ExchangeRate {

    private static final String LOG_TAG = "api.data_exchange_rate" ;
    public static String EXCHANGE_URL_BASE = "http://openexchangerates.org/api/";
    public static String EXCHANGE_URL_LATEST = "latest.json?app_id=41c2e84ad34a4407b8343a96606f6a07";
    public static String EXCHANGE_URL_CURRENCIES = "currencies.json?app_id=41c2e84ad34a4407b8343a96606f6a07";
    public static String EXCHANGE_URL_DEFAULT = EXCHANGE_URL_BASE+EXCHANGE_URL_LATEST;
    public static String EXCHANGE_GBP = "GBP";
    public static String EXCHANGE_USD = "USD";
    public static String EXCHANGE_EUR = "EUR";
    public static String EXCHANGE_CHF;
    public static String EXCHANGE_JPY;
    public static String EXCHANGE_RUB;
    public static String EXCHANGE_NOK;
    public static String EXCHANGE_SEK;
    public static String BASE_URL = "http://openexchangerates.org/api/";
    ExchangeRatesActivity xChange;


    public ExchangeRate(ExchangeRatesActivity xChange){
        this.xChange = xChange;
    }

    String toReturn;

    public String getRate(String initialRate, String finalRate){

        String toGet = EXCHANGE_URL_BASE+EXCHANGE_URL_LATEST;

        AsyncHttpClient.getDefaultInstance().getString(toGet, new AsyncHttpClient.StringCallback() {
            // Callback is invoked with any exceptions/errors, and the result, if available.
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if (e != null) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                    return;
                }
                try {
                    JSONObject rate = new JSONObject(result);
                    toReturn = rate.getString("rates");
                    System.out.println(toReturn);
                    parseRate(toReturn);

                } catch ( JSONException ex ) {
                    Log.e(LOG_TAG, ex.toString());
                    return;
                }
            }
        });


        return toReturn;
    }

    public void parseRate(String toSet){

        String a;
        String b;
        String result;
        toSet.replace("{", "");
        toSet.replace("}", "");

        TextView exchangeView = (TextView) xChange.findViewById(R.id.exchangeView);
        exchangeView.setText(toSet);
    }
}