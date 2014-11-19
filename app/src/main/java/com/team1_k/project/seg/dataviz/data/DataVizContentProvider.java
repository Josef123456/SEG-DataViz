package com.team1_k.project.seg.dataviz.data;

import com.team1_k.project.seg.dataviz.data.DataVizContract.* ;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.sql.SQLException;

public class DataVizContentProvider extends ContentProvider {

    private DataVizDbHelper dbHelper ;

    private static final UriMatcher sUriMatcher = buildUriMatcher() ;
    private static final int COUNTRY= 100 ;
    private static final int COUNTRY_ID = 101 ;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH) ;
        final String AUTHORITY = DataVizContract.CONTENT_AUTHORITY;

        matcher.addURI( AUTHORITY, DataVizContract.PATH_COUNTRY, COUNTRY );
        matcher.addURI( AUTHORITY, DataVizContract.PATH_COUNTRY + "/#", COUNTRY_ID );
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DataVizDbHelper(getContext());
        return false ;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri) ;
        switch(match) {
            case COUNTRY: return CountryEntry.CONTENT_TYPE ;
            case COUNTRY_ID: return CountryEntry.CONTENT_ITEM_TYPE ;
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
        final SQLiteDatabase db = dbHelper.getWritableDatabase() ;
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
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        return returnUri ;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
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
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        return returnCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
