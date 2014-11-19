package com.team1_k.project.seg.dataviz.data;

import android.test.MoreAsserts ;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.team1_k.project.seg.dataviz.data.DataVizContract.CountryEntry;

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
    }

    public void testDataFetch() throws Exception {

        ContentValues countryValues = new ContentValues();
        countryValues.put(CountryEntry.COLUMN_NAME,"abc");

        getContext().getContentResolver().insert(CountryEntry.CONTENT_URI, countryValues ) ;

        CursorLoader loader = new CursorLoader(
                getContext(),
                CountryEntry.CONTENT_URI,
                CountryEntry.COLUMNS,
                null,
                null,
                null
        );

        Cursor returnCurson = loader.loadInBackground();

        assertEquals( 1, returnCurson.getCount() );

    }

    public void testDataInsert() throws Exception {



    }
}
