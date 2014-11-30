package com.team1_k.project.seg.dataviz.data_exchange_rate;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.ExchangeRatesActivity;
import com.team1_k.project.seg.dataviz.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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
    public static String EXCHANGE_CHF = "CHF";
    public static String EXCHANGE_JPY = "JPY";
    public static String EXCHANGE_RUB = "RUB";
    public static String EXCHANGE_NOK = "NOK";
    public static String EXCHANGE_SEK = "SEK";
    public static String BASE_URL = "http://openexchangerates.org/api/";
    ExchangeRatesActivity xChangeActivity;
    ListView listView;
    ProgressBar pb;


    public ExchangeRate(ExchangeRatesActivity xChange, ListView listView, ProgressBar pb){
        this.xChangeActivity = xChange;
        this.listView = listView;
        this.pb = pb;
    }

    JSONObject rates;
    ArrayList<String> list;
    public ArrayList<String> getRates(){

        final String[] temp = {
                EXCHANGE_GBP, EXCHANGE_EUR, EXCHANGE_CHF, EXCHANGE_JPY, EXCHANGE_RUB, EXCHANGE_NOK, EXCHANGE_SEK
        };

        list  = new ArrayList<String>();
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
                    rates = new JSONObject(result);

                    for(int i = 0; i < 7; i++){
                        try {
                            list.add(rates.getJSONObject("rates").getString(temp[i]));
                            parseRates();

                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }

                } catch ( JSONException ex ) {
                    Log.e(LOG_TAG, ex.toString());
                    return;
                }
            }
        });


        return list;
    }

    private void parseRates(){
        StableArrayAdapter adapter = new StableArrayAdapter(xChangeActivity,
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        pb.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

}