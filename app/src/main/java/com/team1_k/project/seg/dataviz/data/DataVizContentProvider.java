package com.team1_k.project.seg.dataviz.data;

import com.team1_k.project.seg.dataviz.data.DataVizContract.* ;
import com.team1_k.project.seg.dataviz.model.DataPoint;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataVizContentProvider extends ContentProvider {

    private DataVizDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher() ;

    private static final String LOG_TAG = "content_provider" ;

    private static final int COUNTRY= 100 ;
    private static final int COUNTRY_ID = 101 ;
    private static final int COUNTRY_WITH_METRICS = 102 ;
    private static final int COUNTRY_WITH_METRIC = 103 ;
    private static final int COUNTRIES_WITH_METRICS = 104 ;

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

        matcher.addURI(
                AUTHORITY,
                DataVizContract.PATH_COUNTRY + "/metrics", COUNTRIES_WITH_METRICS
        );
        matcher.addURI(
                AUTHORITY,
                DataVizContract.PATH_COUNTRY + "/#/metrics/#",
                COUNTRY_WITH_METRIC
        );

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DataVizDbHelper(getContext());
        return false ;
    }

    /**
     * Matches the current URI against all of the ones for which we can query our
     * database.
     * @param uri The URI we wanna query the database
     * @return The {@link java.lang.String} which represents the type of the item returned
     * by the query
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri) ;
        switch(match) {
            case COUNTRY: return CountryEntry.CONTENT_TYPE ;
            case COUNTRY_ID: return CountryEntry.CONTENT_ITEM_TYPE ;
            case COUNTRY_WITH_METRICS: return CountryEntry.CONTENT_TYPE ;
            case COUNTRY_WITH_METRIC: return CountryEntry.CONTENT_TYPE ;
            case COUNTRIES_WITH_METRICS: return CountryEntry.CONTENT_TYPE ;

            case METRIC: return MetricEntry.CONTENT_TYPE ;
            case METRIC_ID: return MetricEntry.CONTENT_ITEM_TYPE ;

            case DATA_POINT: return DataPointEntry.CONTENT_TYPE ;
            case DATA_POINT_ID: return DataPointEntry.CONTENT_ITEM_TYPE ;

        }
        //TODO: better this.
        return null;
    }

    /**
     * We are not deleting any data, at the moment, so there is no need to implement this method.
     * @param uri
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    /**
     * Inserting at the URI the given content values object.
     * @param uri The URI of the database at which we are going to insert.
     * @param values Formatted {@link android.content.ContentValues} object to insert
     *               into databse.
     * @return Returns an {@link java.net.URI} of the inserted object.
     */
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

    /**
     * Querying the database at the given URL with the following params.
     * @param uri The URI we wanna query the database on.
     * @param projection The projection (selected columns) we want to get back from the database
     * @param selection The selection part of the query (WHERE) with `?` in place for params
     *                  to guard against SQL injection
     * @param selectionArgs A matching number of params to replace the `?` in the query
     * @param sortOrder The sort order for the query
     * @return {@link android.database.Cursor} for the query, which contains all of the rows
     * returned by the database
     */
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
                        CountryEntry.TABLE_NAME + "." + CountryEntry._ID + " = ?" + " AND "
                                + DataPointEntry.TABLE_NAME + "."
                                + DataPointEntry.COLUMN_VALUE + " > 0",
                        new String[] { String.valueOf(ContentUris.parseId(uri)) },
                        null,
                        null,
                        sortOrder
                );
                break ;
            }
            case COUNTRY_WITH_METRIC: {
                Log.d(LOG_TAG, uri.toString());
                ContentUris.parseId(uri);
                List<String> path = uri.getPathSegments();
                String metric_id = path.get(3);
                String country_id = path.get(1);
                returnCursor = countryWithMetricsQueryBuilder.query(
                        db,
                        projection,
                        CountryEntry.TABLE_NAME + "." + CountryEntry._ID + " = ?"
                                + " AND "
                                + MetricEntry.TABLE_NAME + "." + MetricEntry._ID + " = ?"
                                + " AND "
                                + DataPointEntry.TABLE_NAME + "."
                                + DataPointEntry.COLUMN_VALUE + " > 0",
                        new String[] { country_id, metric_id },
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case COUNTRIES_WITH_METRICS: {
                Log.d ( LOG_TAG, uri.toString());
                returnCursor = countryWithMetricsQueryBuilder.query(
                        db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                Log.d ( LOG_TAG, selection ) ;
                Log.d ( LOG_TAG, selectionArgs[0]);// + " " + selectionArgs[1] ) ;
                break;
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

    /**
     * We are not updating any values, since the database schema makes sure to replace the new
     * entries if the params conflict.
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }


    /**
     * One of the most used functions in the application. This is doing the same job as the insert
     * function, but all in one SQL transaction which increases the speed drastically when you have
     * to insert a large number of items into the database.
     * @param uri The URI of the database at which we are going to insert.
     * @param values An array of {@link android.content.ContentValues} which we are going to insert
     *               into the database.
     * @return the number of rows that were inserted into the database.
     */
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
                        long _id = db.insertWithOnConflict(
                                MetricEntry.TABLE_NAME,
                                null,
                                value,
                                SQLiteDatabase.CONFLICT_IGNORE
                        );
                        if ( _id != -1 ) {
                            ++ returnCount;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                Log.d ( LOG_TAG, "Metric return count:" + returnCount ) ;
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            case COUNTRY: {
                db.beginTransaction();
                int returnCount = 0 ;
                try {
                    for ( ContentValues value: values ) {
                        long _id = db.insertWithOnConflict(
                                CountryEntry.TABLE_NAME,
                                null,
                                value,
                                SQLiteDatabase.CONFLICT_IGNORE
                        );
                        if (_id != -1) {
                            ++returnCount;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                Log.d ( LOG_TAG, "Country return count:" + returnCount ) ;
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
                Log.d ( LOG_TAG, "data point bulk insert: " + returnCount);
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
