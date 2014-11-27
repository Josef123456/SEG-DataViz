package com.team1_k.project.seg.dataviz;

import android.app.Activity;
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

                Cursor cursor = mCountryAdapter.getCursor();
                cursor.moveToPosition(i);
                String country_api_id = cursor.getString(
                  CountryEntry.INDEX_COLUMN_API_ID
                );

                Intent intent = new Intent(getApplicationContext(), CountryDetailActivity.class)
                        .putExtra(CountryDetailActivity.TAG_COUNTRY_ID, country_api_id);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_view, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

//            case R.id.action_settings:
//                Intent intentSettings = new Intent(CountrySelectionActivity.this, ActivityForItemOne.class);
//                this.startActivity(intentSettings);
//                break;
            case R.id.action_home:
                Intent intentHome = new Intent(CountrySelectionActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(CountrySelectionActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
//            case R.id.action_Markets:
//                Intent intentMarkets = new Intent(CountrySelectionActivity.this, ActivityForItemOne.class);
//                this.startActivity(intentMarkets);
//                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(CountrySelectionActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
//            case R.id.action_More:
//                Intent intentMore = new Intent(CountrySelectionActivity.this, ActivityForItemOne.class);
//                this.startActivity(intentMore);
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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
