package com.team1_k.project.seg.dataviz.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.team1_k.project.seg.dataviz.data.DataVizContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Country implements Parcelable {


    private static final String LOG_TAG = "model.country" ;

    private String mName;
    private String mApiId ;
    private double mLatitude ;
    private double mLongitude ;
    private String mCapitalCity ;
    private long mDatabaseId;

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

    /**
     * Builds a country object from a {@link org.json.JSONObject}
     * @param country the object which contains all the country data
     */
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

    public Country(String mApiId, long mDatabaseId) {
        this.mApiId = mApiId;
        this.mDatabaseId = mDatabaseId;
    }

    public Country(String mName, String mApiId, long mDatabaseId) {
        this.mName = mName;
        this.mApiId = mApiId;
        this.mDatabaseId = mDatabaseId;
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

    /**
     * Constructor which takes the API id and queries the database for the country
     * @param context Required to be able to query the database
     * @param apiId the API ID for which we want to query
     * @return a country entity with the info from the database
     * @throws Exception In case we can't find the country in the database, an exception is thrown
     */
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mApiId);
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
        dest.writeString(this.mCapitalCity);
        dest.writeLong(this.mDatabaseId);
    }

    private Country(Parcel in) {
        this.mName = in.readString();
        this.mApiId = in.readString();
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
        this.mCapitalCity = in.readString();
        this.mDatabaseId = in.readLong();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
