package com.team1_k.project.seg.dataviz.data_exchange_rate;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.ExchangeRatesActivity;
import com.team1_k.project.seg.dataviz.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dbrisingr on 26/11/14.
 */
public class ExchangeRate {

    private static final String LOG_TAG = "api.data_exchange_rate" ;
    public static String EXCHANGE_URL_BASE = "http://openexchangerates.org/api/";
    public static String EXCHANGE_URL_LATEST = "latest.json?app_id=41c2e84ad34a4407b8343a96606f6a07";
    public static String EXCHANGE_URL_CURRENCIES = "currencies.json?app_id=41c2e84ad34a4407b8343a96606f6a07";
    public static String EXCHANGE_GBP = "GBP";
    public static String EXCHANGE_EUR = "EUR";
    public static String EXCHANGE_CHF = "CHF";
    public static String EXCHANGE_JPY = "JPY";
    public static String EXCHANGE_RUB = "RUB";
    public static String EXCHANGE_NOK = "NOK";
    public static String EXCHANGE_SEK = "SEK";
    public static String EXCHANGE_AUD = "AUD";
    public static String EXCHANGE_BZD = "BZD";
    public static String EXCHANGE_CAD = "CAD";
    public static String EXCHANGE_QAR = "QAR";


    ExchangeRatesActivity xChangeActivity;
    ListView listView;
    ProgressBar pb;
    LinearLayout ll_1;
    LinearLayout ll_2;


    public ExchangeRate(ExchangeRatesActivity xChange, ListView listView, ProgressBar pb, LinearLayout ll_1, LinearLayout ll_2){
        this.xChangeActivity = xChange;
        this.listView = listView;
        this.pb = pb;
        this.ll_1 = ll_1;
        this.ll_2 = ll_2;
    }

    JSONObject rates;
    List<String> list;

    public void getRates(){

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
                            list.add(temp[i]+"\n"+rates.getJSONObject("rates").getString(temp[i]));
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
    }

    private void parseRates(){
        //StableArrayAdapter adapter = new StableArrayAdapter(xChangeActivity,
        //R.layout.exchange_item, list);

        //CustomAdapter adapterx = new CustomAdapter(xChangeActivity, list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(xChangeActivity ,R.layout.exchange_item,list);
        listView.setAdapter(adapter);

        pb.setVisibility(View.GONE);
        ll_1.setVisibility(View.VISIBLE);
    }

}