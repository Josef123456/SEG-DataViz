package com.team1_k.project.seg.dataviz.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.team1_k.project.seg.dataviz.data.DataVizContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Country {

    private static final String LOG_TAG = "model.country" ;

    private String mName;
    private String mApiId ;
    private double mLatitude ;
    private double mLongitude ;
    private String mCapitalCity ;
    private long mDatabaseId;
    Metric[] metrics ;

    public String getName() {
        return mName;
    }

    public String getApiId() {
        return mApiId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getCapitalCity() {
        return mCapitalCity;
    }

    public long getDatabaseId() {
        return mDatabaseId;
    }

    public Country(JSONObject country) {
        try {

            this.mName = country.getString("name");
            this.mApiId = country.getString("id");
            try {
                this.mLatitude = Double.parseDouble(country.getString("latitude"));
            } catch ( JSONException exc) {
                this.mLatitude = 0.0 ;
            } catch ( NumberFormatException exc ) {
                this.mLatitude = 0.0 ;
            }
            try {
                this.mLongitude = Double.parseDouble(country.getString("longitude"));
            } catch ( JSONException exc ) {
                this.mLongitude = 0.0 ;
            } catch ( NumberFormatException exc ) {
                this.mLongitude = 0.0 ;
            }
            this.mCapitalCity = country.getString("capitalCity");
            Log.i ( LOG_TAG, "city with name " + mName);
        } catch ( JSONException exception ) {
            Log.e(LOG_TAG, exception.toString());
            exception.printStackTrace();
        }
    }

    private Country(String mName, String mApiId,
                   double mLatitude, double mLongitude,
                   String mCapitalCity, long mDatabaseId) {
        this.mName = mName;
        this.mApiId = mApiId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mCapitalCity = mCapitalCity;
        this.mDatabaseId = mDatabaseId;
    }

    public static Country getCountryWithApiId(Context context, String apiId) throws Exception{
        Cursor cursor = context.getContentResolver().query(
                DataVizContract.CountryEntry.CONTENT_URI,
                DataVizContract.CountryEntry.COLUMNS,
                DataVizContract.CountryEntry.COLUMN_API_ID + " = ?",
                new String[] { apiId},
                null
        );
        if ( cursor.moveToFirst() ) {
            String name = cursor.getString(DataVizContract.CountryEntry.INDEX_COLUMN_NAME);
            double longitude = cursor.getDouble(DataVizContract.CountryEntry.INDEX_COLUMN_LONGITUDE);
            double latitude = cursor.getDouble(DataVizContract.CountryEntry.INDEX_COLUMN_LATITUDE);
            String capital_city = cursor.getString(DataVizContract.CountryEntry.INDEX_COLUMN_CAPITAL_CITY);
            long database_id = cursor.getLong(DataVizContract.CountryEntry.INDEX_COLUMN_ID);
            return new Country(name, apiId,latitude, longitude,capital_city, database_id);
        }
        throw new Exception("Can't find country with api_id " + apiId);
    }
}
