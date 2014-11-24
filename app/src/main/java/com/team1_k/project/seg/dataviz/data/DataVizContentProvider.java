package com.team1_k.project.seg.dataviz.data;

import com.team1_k.project.seg.dataviz.data.DataVizContract.* ;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataVizContentProvider extends ContentProvider {

    private DataVizDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher() ;

    private static final int COUNTRY= 100 ;
    private static final int COUNTRY_ID = 101 ;
    private static final int COUNTRY_WITH_METRICS = 102 ;

    private static final int METRIC = 200 ;
    private static final int METRIC_ID = 201 ;
    private static final int ALL_METRIC_DATA = 202 ;

    private static final int DATA_POINT = 300 ;
    private static final int DATA_POINT_ID = 301 ;

    private static final SQLiteQueryBuilder countryWithMetricsQueryBuilder;

    static {
        countryWithMetricsQueryBuilder = new SQLiteQueryBuilder();
        countryWithMetricsQueryBuilder.setTables(
                CountryEntry.TABLE_NAME +
                " INNER JOIN " + DataPointEntry.TABLE_NAME + " ON " +
                CountryEntry.TABLE_NAME + "." + CountryEntry._ID + " = "+
                DataPointEntry.TABLE_NAME + "." + DataPointEntry.COLUMN_COUNTRY_ID
                +
                " LEFT JOIN " + MetricEntry.TABLE_NAME + " ON " +
                MetricEntry.TABLE_NAME + "." + MetricEntry._ID + " = "+
                DataPointEntry.TABLE_NAME + "." + DataPointEntry.COLUMN_METRIC_ID
        );
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH) ;
        final String AUTHORITY = DataVizContract.CONTENT_AUTHORITY;

        matcher.addURI( AUTHORITY, DataVizContract.PATH_COUNTRY, COUNTRY );
        matcher.addURI( AUTHORITY, DataVizContract.PATH_COUNTRY + "/#", COUNTRY_ID );
        matcher.addURI( AUTHORITY, DataVizContract.PATH_COUNTRY + "/metrics/#",
                COUNTRY_WITH_METRICS);

        matcher.addURI( AUTHORITY, DataVizContract.PATH_METRIC, METRIC ) ;
        matcher.addURI( AUTHORITY, DataVizContract.PATH_METRIC + "/#", METRIC_ID ) ;

        matcher.addURI( AUTHORITY, DataVizContract.PATH_DATA_POINT, DATA_POINT ) ;
        matcher.addURI( AUTHORITY, DataVizContract.PATH_DATA_POINT + "/#", DATA_POINT_ID ) ;

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DataVizDbHelper(getContext());
        return false ;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri) ;
        switch(match) {
            case COUNTRY: return CountryEntry.CONTENT_TYPE ;
            case COUNTRY_ID: return CountryEntry.CONTENT_ITEM_TYPE ;
            case COUNTRY_WITH_METRICS: return CountryEntry.CONTENT_TYPE ;

            case METRIC: return MetricEntry.CONTENT_TYPE ;
            case METRIC_ID: return MetricEntry.CONTENT_ITEM_TYPE ;

            case DATA_POINT: return DataPointEntry.CONTENT_TYPE ;
            case DATA_POINT_ID: return DataPointEntry.CONTENT_ITEM_TYPE ;

        }
        //TODO: better this.
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase() ;
        Uri returnUri ;
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case COUNTRY: {
                long _id = db.insert(CountryEntry.TABLE_NAME,null, values);
                if ( _id > 0 ) {
                    returnUri = CountryEntry.buildCountryUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case METRIC: {
                long _id = db.insert(MetricEntry.TABLE_NAME, null, values);
                if ( _id > 0 ) {
                    returnUri = MetricEntry.buildMetricUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case DATA_POINT: {
                long _id = db.insert(DataPointEntry.TABLE_NAME, null, values);
                if ( _id > 0 ) {
                    returnUri = DataPointEntry.buildDataPointUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break ;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        return returnUri ;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Cursor returnCursor ;
        switch (match) {
            case COUNTRY: {
                returnCursor = db.query(
                        CountryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break ;
            }
            case COUNTRY_ID: {
                returnCursor = db.query(
                        CountryEntry.TABLE_NAME,
                        projection,
                        CountryEntry._ID + " = ?",
                        new String[] { String.valueOf(ContentUris.parseId(uri)) },
                        null,
                        null,
                        sortOrder
                );
                break ;
            }
            case COUNTRY_WITH_METRICS: {
                returnCursor = countryWithMetricsQueryBuilder.query(
                        db,
                        projection,
                        CountryEntry.TABLE_NAME + "." + CountryEntry._ID + " = ?",
                        new String[] { String.valueOf(ContentUris.parseId(uri)) },
                        null,
                        null,
                        sortOrder
                );
                break ;
            }
            case METRIC: {
                returnCursor = db.query(
                        MetricEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase() ;
        final int match = sUriMatcher.match(uri);

        switch(match) {

            case METRIC: {
                db.beginTransaction();
                int returnCount = 0 ;
                try {
                    for( ContentValues value: values) {
                        long _id = db.insert( MetricEntry.TABLE_NAME, null, value);
                        if ( _id != -1 ) {
                            ++ returnCount;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            case COUNTRY: {
                db.beginTransaction();
                int returnCount = 0 ;
                try {
                    for ( ContentValues value: values ) {
                        long _id = db.insert(CountryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            ++returnCount;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case DATA_POINT: {
                db.beginTransaction();
                int returnCount = 0 ;
                try {
                    for ( ContentValues value: values ) {
                        long _id = db.insert(DataPointEntry.TABLE_NAME, null, value);
                        if (_id!= -1) {
                            ++ returnCount ;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                getContext().getContentResolver()
                        .notifyChange(CountryEntry.buildCountryWithMetricUri(), null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri,values);

        }
    }
}
