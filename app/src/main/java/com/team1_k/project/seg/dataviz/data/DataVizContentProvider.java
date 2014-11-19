package com.team1_k.project.seg.dataviz.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DataVizContentProvider extends ContentProvider {

    private DataVizDbHelper dbHelper ;

    private static final UriMatcher sUriMatcher = buildUriMatcher() ;
    private static final int COUNTRY= 100 ;
    private static final int COUNTRY_ID = 101 ;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH) ;
        final String AUTHORITY = DataVizContract.CONTRACT_AUTHORITY ;

        matcher.addURI( AUTHORITY, DataVizContract.PATH_COUNTRY, COUNTRY );
        matcher.addURI( AUTHORITY, DataVizContract.PATH_COUNTRY + "/#", COUNTRY_ID );
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DataVizDbHelper(getContext());
    }



    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
