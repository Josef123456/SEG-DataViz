package com.team1_k.project.seg.dataviz.data;

import android.content.ContentUris;
import android.net.Uri;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.team1_k.project.seg.dataviz.data.DataVizContract.*;

import java.util.Map;
import java.util.Set;

/**
 * Created by alexstoick on 11/19/14.
 */
public class DataVizContentProviderTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DataVizDbHelper dbHelper = new DataVizDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(CountryEntry.TABLE_NAME,null,null);
        db.delete(MetricEntry.TABLE_NAME,null,null);
        db.delete(DataPointEntry.TABLE_NAME,null,null);
    }

    static void validateCursor(Cursor cursor, ContentValues expectedValues) {

        assertTrue(cursor.moveToFirst());

        Set<Map.Entry<String,Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String,Object> entry : valueSet ) {
            String columnName = entry.getKey() ;
            int idx = cursor.getColumnIndex(columnName);
            assertFalse( idx == -1 );
            String expectedValue = entry.getValue().toString() ;
            assertEquals(expectedValue, cursor.getString(idx));
        }

    }

    public void testCountryDataInsertAndFetch() throws Exception {

        ContentValues countryValues = new ContentValues();
        countryValues.put(CountryEntry.COLUMN_NAME,"abc");
        countryValues.put(CountryEntry.COLUMN_API_ID,"AKR");
        countryValues.put(CountryEntry.COLUMN_CAPITAL_CITY,"test city");
        countryValues.put(CountryEntry.COLUMN_LATITUDE, 23.5);
        countryValues.put(CountryEntry.COLUMN_LONGITUDE, 44.14);

        getContext().getContentResolver().insert(CountryEntry.CONTENT_URI, countryValues ) ;

        CursorLoader loader = new CursorLoader(
                getContext(),
                CountryEntry.CONTENT_URI,
                CountryEntry.COLUMNS,
                null,
                null,
                null
        );

        Cursor returnCursor = loader.loadInBackground();

        assertEquals( 1, returnCursor.getCount() );
        validateCursor(returnCursor, countryValues);

        countryValues = new ContentValues() ;
        countryValues.put(CountryEntry.COLUMN_NAME, "ffdes");
        countryValues.put(CountryEntry.COLUMN_API_ID,"AKFFER");
        countryValues.put(CountryEntry.COLUMN_CAPITAL_CITY,"test city");
        countryValues.put(CountryEntry.COLUMN_LATITUDE, 23.5);
        countryValues.put(CountryEntry.COLUMN_LONGITUDE, 44.14);

        Uri insertUri = getContext().getContentResolver()
                .insert(CountryEntry.CONTENT_URI, countryValues);
        long _ID = ContentUris.parseId(insertUri);

        loader = new CursorLoader(
                getContext(),
                CountryEntry.buildCountryUri(_ID),
                CountryEntry.COLUMNS,
                null,
                null,
                null);

        returnCursor = loader.loadInBackground() ;

        assertEquals( 1 , returnCursor.getCount() );
        validateCursor( returnCursor, countryValues );

    }

    public void testMetricDataInsertAndFetch() throws Exception {

        ContentValues metricValues = new ContentValues() ;
        metricValues.put(MetricEntry.COLUMN_API_ID, "api_id");
        metricValues.put(MetricEntry.COLUMN_DESCRIPTION, "description");
        metricValues.put(MetricEntry.COLUMN_NAME, "name");

        getContext().getContentResolver().insert(MetricEntry.CONTENT_URI, metricValues);

        CursorLoader loader ;
        loader = new CursorLoader(
                getContext(),
                MetricEntry.CONTENT_URI,
                MetricEntry.COLUMNS,
                null,
                null,
                null
        );

        Cursor returnCursor ;
        returnCursor = loader.loadInBackground();

        assertEquals(1, returnCursor.getCount());
        validateCursor( returnCursor, metricValues);
    }

    public void testCountryAndMetricAndDataPointInsertAndFetch() throws Exception {

        ContentValues countryValues = new ContentValues();
        countryValues.put(CountryEntry.COLUMN_NAME,"abc");
        countryValues.put(CountryEntry.COLUMN_API_ID,"AKR");
        countryValues.put(CountryEntry.COLUMN_CAPITAL_CITY,"test city");
        countryValues.put(CountryEntry.COLUMN_LATITUDE, 23.5);
        countryValues.put(CountryEntry.COLUMN_LONGITUDE, 44.14);

        ContentValues metricValues = new ContentValues() ;
        metricValues.put(MetricEntry.COLUMN_API_ID, "metric api_id");
        metricValues.put(MetricEntry.COLUMN_DESCRIPTION, "metric description");
        metricValues.put(MetricEntry.COLUMN_NAME, "metric name");


        Uri insertUri ;
        insertUri = getContext().getContentResolver()
                .insert(CountryEntry.CONTENT_URI, countryValues );

        long country_id = ContentUris.parseId(insertUri);

        assertNotNull(country_id);

        insertUri = getContext().getContentResolver()
                .insert(MetricEntry.CONTENT_URI, metricValues);

        long metric_id = ContentUris.parseId(insertUri);

        assertNotNull(metric_id);
        ContentValues dataPointEntryValues = new ContentValues();
        dataPointEntryValues.put(DataPointEntry.COLUMN_COUNTRY_ID, country_id);
        dataPointEntryValues.put(DataPointEntry.COLUMN_METRIC_ID, metric_id);
        dataPointEntryValues.put(DataPointEntry.COLUMN_YEAR, 2014);
        dataPointEntryValues.put(DataPointEntry.COLUMN_VALUE, 1.25);

        insertUri = getContext().getContentResolver()
                .insert(DataPointEntry.CONTENT_URI, dataPointEntryValues);

        long data_point_id = ContentUris.parseId(insertUri);

        assertNotNull(data_point_id);

        CursorLoader loader;

        loader = new CursorLoader(
                getContext(),
                CountryEntry.buildCountryWithMetricsUriWithId(country_id),
                MetricEntry.COLUMNS_FOR_METRIC_QUERY,
                null,
                null,
                null);

        Cursor cursor;
        cursor = loader.loadInBackground();

        assertEquals( 1 , cursor.getCount());
        cursor.moveToFirst();
        int id = cursor.getColumnIndex("name");

        Log.d("cursordebug", cursor.getString(id));

    }


}
