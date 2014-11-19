package com.team1_k.project.seg.dataviz.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.team1_k.project.seg.dataviz.data.DataVizContract.CountryEntry;

/**
 * Created by alexstoick on 11/19/14.
 */
public class DataVizDbHelperTest extends AndroidTestCase {

    public void testDbCreation() throws Exception {

        DataVizDbHelper dbHelper = new DataVizDbHelper( getContext() ) ;
        SQLiteDatabase db = dbHelper.getReadableDatabase() ;

        Cursor cursor = db.query(CountryEntry.TABLE_NAME, CountryEntry.COLUMNS,
                null, null, null, null, null) ;

        assertNotNull(cursor);
    }
}
