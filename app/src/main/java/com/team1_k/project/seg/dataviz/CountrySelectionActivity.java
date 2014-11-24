package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract.CountryEntry;
import com.team1_k.project.seg.dataviz.data.DataVizDbHelper;

public class CountrySelectionActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private CursorAdapter mCountryAdapter ;

    private static final String LOG_TAG = "ui.country" ;

    private static final int COUNTRY_LOADER = 0 ;

    private static final String[] COUNTRY_COLUMNS = {
            CountryEntry.TABLE_NAME + "." + CountryEntry._ID,
            CountryEntry.COLUMN_NAME,
            CountryEntry.COLUMN_API_ID
    };

    public static int COL_COUNTRY_ID = 0 ;
    public static int COL_NAME = 1 ;
    public static int COL_API_ID = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataVizDbHelper dbHelper = new DataVizDbHelper(getApplicationContext());
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(COUNTRY_LOADER, null, this);
        setContentView(R.layout.activity_country_selection);
        QueryBuilder queryBuilder = new QueryBuilder(getApplicationContext());
        queryBuilder.getCountries();
        mCountryAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.country_list_view_layout,
                null,
                new String[] {
                        CountryEntry.COLUMN_NAME
                },
                new int[]{
                        R.id.countryName
                },
                0
        );
        ListView listView = (ListView) findViewById(R.id.country_list_view);
        listView.setAdapter(mCountryAdapter);
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CountryDetailActivity.class)
                        .putExtra(CountryDetailActivity.TAG_COUNTRY_ID, "GBR");
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {

        String sortOrder = CountryEntry.TABLE_NAME + "." + CountryEntry.COLUMN_NAME + " ASC";

        return new CursorLoader(
                getApplicationContext(),
                CountryEntry.CONTENT_URI,
                COUNTRY_COLUMNS,
                null,
                null,
                sortOrder
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d(LOG_TAG, "finished loading");
        mCountryAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d(LOG_TAG, "reset loader");
        mCountryAdapter.swapCursor(null);
    }
}
