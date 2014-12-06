package com.team1_k.project.seg.dataviz.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.team1_k.project.seg.dataviz.data.DataVizContract.CountryEntry;
import com.team1_k.project.seg.dataviz.data.DataVizContract.MetricEntry ;
import com.team1_k.project.seg.dataviz.data.DataVizContract.DataPointEntry ;
import com.team1_k.project.seg.dataviz.model.DataPoint;

/**
 * Created by alexstoick on 11/19/14.
 */
public class DataVizDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6 ;
    public static final String DATABASE_NAME = "dataviz.db" ;

    public DataVizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_COUNTRY_TABLE = "CREATE TABLE " + CountryEntry.TABLE_NAME + " " +
                "( "+
                CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CountryEntry.COLUMN_NAME + " TEXT NOT NULL," +
                CountryEntry.COLUMN_API_ID + " TEXT NOT NULL UNIQUE," +
                CountryEntry.COLUMN_CAPITAL_CITY + " TEXT," +
                CountryEntry.COLUMN_LATITUDE + " REAL," +
                CountryEntry.COLUMN_LONGITUDE + " REAL"
                + ")" ;
        sqLiteDatabase.execSQL(SQL_CREATE_COUNTRY_TABLE);

        final String SQL_CREATE_METRIC_TABLE = "CREATE TABLE " + MetricEntry.TABLE_NAME + " " +
                "( " +
                MetricEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                MetricEntry.COLUMN_API_ID + " TEXT NOT NULL UNIQUE," +
                MetricEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                MetricEntry.COLUMN_NAME + " TEXT NOT NULL"
                + ")";
        sqLiteDatabase.execSQL(SQL_CREATE_METRIC_TABLE);

        final String SQL_CREATE_DATA_POINT_TABLE = "CREATE TABLE " + DataPointEntry.TABLE_NAME +
                " (" +
                DataPointEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataPointEntry.COLUMN_COUNTRY_ID + " INTEGER NOT NULL," +
                DataPointEntry.COLUMN_METRIC_ID + " INTEGER NOT NULL," +
                DataPointEntry.COLUMN_YEAR + " INTEGER NOT NULL," +
                DataPointEntry.COLUMN_VALUE + " REAL NOT NULL," +
                "FOREIGN KEY (" + DataPointEntry.COLUMN_COUNTRY_ID + ") REFERENCES " +
                CountryEntry.TABLE_NAME + " (" + CountryEntry._ID + ")," +
                "FOREIGN KEY (" + DataPointEntry.COLUMN_METRIC_ID+ ") REFERENCES " +
                MetricEntry.TABLE_NAME + " (" + MetricEntry._ID + ")" +
                "UNIQUE(" + DataPointEntry.COLUMN_METRIC_ID + ", " +
                    DataPointEntry.COLUMN_COUNTRY_ID + " ," +
                    DataPointEntry.COLUMN_YEAR + ") ON CONFLICT IGNORE"
                + ")";
        sqLiteDatabase.execSQL(SQL_CREATE_DATA_POINT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + CountryEntry.TABLE_NAME );
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + MetricEntry.TABLE_NAME );
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + DataPointEntry.TABLE_NAME );
        onCreate(sqLiteDatabase);
    }
}
