package com.team1_k.project.seg.dataviz.exchange_rate;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.team1_k.project.seg.dataviz.adapters.CustomAdapter;
import com.team1_k.project.seg.dataviz.model.ExchangeItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dbrisingr on 26/11/14.
 */
public class ExchangeRate {

    private static final String LOG_TAG = "api.data_exchange_rate" ;
    public static String EXCHANGE_APP_ID = ".json?app_id=41c2e84ad34a4407b8343a96606f6a07";
    public static String EXCHANGE_URL_BASE = "http://openexchangerates.org/api/";
    public static String EXCHANGE_HISTORICAL = "historical/2014-";
    public static String EXCHANGE_URL_LATEST = "latest.json?app_id=41c2e84ad34a4407b8343a96606f6a07";
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
    public static String EXCHANGE_CZK = "CZK";
    public static String EXCHANGE_HKD = "HKD";
    public static String EXCHANGE_BTC = "BTC";


    ExchangeRatesActivity xChangeActivity;
    ListView listView;
    ListView listView2;
    ProgressBar pb;
    LinearLayout ll_1;
    LinearLayout ll_2;
    TextView tvPortrait;
    boolean isPortrait;

    String temp[];
    String temp2[];


    public ExchangeRate(ExchangeRatesActivity xChange, ListView listView, ListView listView2, ProgressBar pb,
                        LinearLayout ll_1, LinearLayout ll_2, TextView tvPortrait){
        this.xChangeActivity = xChange;
        this.listView = listView;
        this.listView2 = listView2;
        this.pb = pb;
        this.ll_1 = ll_1;
        this.ll_2 = ll_2;
        this.tvPortrait = tvPortrait;

        if(xChangeActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            isPortrait = true;
        }else{
            isPortrait = false;
        }


        final String[] temp = {
                EXCHANGE_GBP, EXCHANGE_EUR, EXCHANGE_CHF, EXCHANGE_JPY, EXCHANGE_RUB, EXCHANGE_NOK,
                EXCHANGE_SEK, EXCHANGE_AUD, EXCHANGE_BZD, EXCHANGE_CAD, EXCHANGE_QAR, EXCHANGE_CZK, EXCHANGE_HKD,
                EXCHANGE_BTC
        };
        this.temp = temp;
        final String[] temp2 = {

        };
        this.temp2 = temp2;

    }

    JSONObject rates;
    List<ExchangeItem> list;
    List<ExchangeItem> listOthers;

    /**
     * Gets the exchange rates from the Internet from the given link, and then
     * parses the result to a JSONObject
     */
    public void getRates(){

        list  = new ArrayList<ExchangeItem>();
        listOthers = new ArrayList<ExchangeItem>();
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

                    for(int i = 0; i < 14; i++){

                        list.add(new ExchangeItem(temp[i], rates.getJSONObject("rates").getString(temp[i]), "x"));

                    }

                } catch ( JSONException ex ) {
                    Log.e(LOG_TAG, ex.toString());
                    return;
                }

                getDifference();
            }
        });
    }

    JSONObject toCompare;

    /**
     * Does the calculations for the "difference" column in the listView after getting
     * the exchange rates from the previous day.
     */
    public void getDifference(){

        Calendar c = Calendar.getInstance();
        String month = ""+(c.get(Calendar.MONTH)+1);
        String date = "-0"+(c.get(Calendar.DATE)-1);
        String toGet = EXCHANGE_URL_BASE+EXCHANGE_HISTORICAL+month+date+EXCHANGE_APP_ID;


        AsyncHttpClient.getDefaultInstance().getString(toGet, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if (e != null) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                    return;
                }

                try {
                    toCompare = new JSONObject(result);
                    for(int i=0; i<14; i++){

                        float xChangeToday = Float.parseFloat(list.get(i).getTitle());
                        float xChangeYesterday = Float.parseFloat(toCompare.getJSONObject("rates").getString(temp[i]));

                        float temp = xChangeYesterday - xChangeToday;
                        String diff = String.format("%.6f", temp);

                        list.get(i).setDifference(""+diff);
                    }
                }catch (JSONException ex){
                    Log.e(LOG_TAG, ex.toString());
                    return;
                }

                setAdapters();
            }
        });


    }

    /**
     * Sets the adapters and finally removes the Progress Bar.
     */
    private void setAdapters(){

        CustomAdapter adapter = new CustomAdapter(xChangeActivity, list);
        listView.setAdapter(adapter);
        pb.setVisibility(View.GONE);
        ll_1.setVisibility(View.VISIBLE);


    }

}