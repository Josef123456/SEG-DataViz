package com.team1_k.project.seg.dataviz.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexstoick on 11/23/14.
 */
public class DataPoint implements Parcelable{

    private static final String LOG_TAG = "model.dataPoint" ;

    private double mValue = 0 ;
    private int mYear = 0 ;

    private Metric mMetric ;
    private Country mCountry ;



    /**
     * Builds a data point from a {@link org.json.JSONObject}
     * @param dataPoint the object which contains all of the data
     */
    public DataPoint(JSONObject dataPoint) {
        try {
            this.mValue = Double.valueOf(dataPoint.getString("value"));
            this.mYear = Integer.valueOf(dataPoint.getString("date"));
        } catch ( JSONException e) {
            Log.e(LOG_TAG, e.toString());
        } catch ( NumberFormatException e ) {
            Log.e(LOG_TAG, e.toString());
        }
        Log.i(LOG_TAG, this.toString());
    }

    public DataPoint(double mValue, int mYear, Metric mMetric ) {
        this.mValue = mValue;
        this.mYear = mYear;
        this.mMetric = mMetric;
    }

    public DataPoint(double mValue, int mYear, Metric mMetric, Country mCountry) {
        this.mValue = mValue;
        this.mYear = mYear;
        this.mMetric = mMetric;
        this.mCountry = mCountry;
    }

    public Country getCountry() {
        return mCountry;
    }

    public String toString() {
        return String.valueOf(mYear) + " - " + String.valueOf(mValue);
    }

    public Metric getMetric() {
        return mMetric;
    }

    public double getValue() {
        return mValue;
    }

    public int getYear() {
        return mYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mValue);
        dest.writeInt(this.mYear);
        dest.writeParcelable(this.mMetric, flags);
        dest.writeParcelable(this.mCountry, flags);
    }

    private DataPoint(Parcel in) {
        this.mValue = in.readDouble();
        this.mYear = in.readInt();
        this.mMetric = in.readParcelable(Metric.class.getClassLoader());
        this.mCountry = in.readParcelable(Country.class.getClassLoader());
    }

    public static final Creator<DataPoint> CREATOR = new Creator<DataPoint>() {
        public DataPoint createFromParcel(Parcel source) {
            return new DataPoint(source);
        }

        public DataPoint[] newArray(int size) {
            return new DataPoint[size];
        }
    };
}
