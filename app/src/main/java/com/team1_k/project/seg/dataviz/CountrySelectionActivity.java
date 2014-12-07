package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.team1_k.project.seg.dataviz.data.DataVizContract.CountryEntry;
import com.team1_k.project.seg.dataviz.data.DataVizDbHelper;

public class CountrySelectionActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    protected CursorAdapter mCountryAdapter;

    private static final String LOG_TAG = "ui.country";

    private static final String SORT_ORDER = CountryEntry.TABLE_NAME + "." + CountryEntry.COLUMN_NAME + " ASC";

    private static final int COUNTRY_LOADER = 0;

    private static final String[] COUNTRY_COLUMNS = {
            CountryEntry.TABLE_NAME + "." + CountryEntry._ID,
            CountryEntry.COLUMN_NAME,
            CountryEntry.COLUMN_API_ID
    };
    protected ListView listView;
    protected EditText editText;

    public static int COL_COUNTRY_ID = 0;
    public static int COL_NAME = 1;
    public static int COL_API_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(COUNTRY_LOADER, null, this);
        setContentView(R.layout.activity_country_selection);
        mCountryAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.list_row_country,
                null,
                new String[]{
                        CountryEntry.COLUMN_NAME
                },
                new int[]{
                        R.id.countryName
                },
                0
        );

        mCountryAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                String constraint = charSequence.toString();
                DataVizDbHelper mDbHelper = new DataVizDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                Log.i(LOG_TAG, "cnstr: " + constraint);

                return db.query(
                        CountryEntry.TABLE_NAME,
                        CountryEntry.COLUMNS,
                        CountryEntry.COLUMN_NAME + " LIKE ?",
                        new String[]{constraint + "%"},
                        null,
                        null,
                        SORT_ORDER
                );
            }
        });

        listView = (ListView) findViewById(R.id.country_list_view);
        listView.setAdapter(mCountryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = mCountryAdapter.getCursor();
                cursor.moveToPosition(i);
                String country_api_id = cursor.getString(
                        CountryEntry.INDEX_COLUMN_API_ID
                );

                Intent intent = new Intent(getApplicationContext(), CountryDetailActivity.class)
                        .putExtra(CountryDetailActivity.TAG_COUNTRY_API_ID, country_api_id);
                startActivity(intent);
            }
        });

        editText = (EditText) findViewById(R.id.search_global);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = editText.getText().toString();
                mCountryAdapter.getFilter().filter(searchString);
                mCountryAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.country_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Inside the menu the user can easily change the activities by selecting the menu items.
     * There are six cases inside the switch statement.
     * There is sixth one is for searching in the menu.
     * The user can go to the main page (home), to see the news, exchange rate, countries and comparing the countries.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:
                Intent intentHome = new Intent(CountrySelectionActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(CountrySelectionActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
            case R.id.action_Markets:
                Intent intentMarkets = new Intent(CountrySelectionActivity.this, ExchangeRatesActivity.class);
                this.startActivity(intentMarkets);
                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(CountrySelectionActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
            case R.id.action_More:
                Intent intentMore = new Intent(CountrySelectionActivity.this, ComparisonActivity.class);
                this.startActivity(intentMore);
                break;

            // this case is for searching with new menu
            case R.id.search:
                MenuItem searchItem = item;
                SearchView searchView = (SearchView) searchItem.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Log.i(LOG_TAG, "reached query text changed" + newText);
                        mCountryAdapter.getFilter().filter(newText);
                        mCountryAdapter.notifyDataSetChanged();
                        return false;
                    }
                });
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getApplicationContext(),
                CountryEntry.CONTENT_URI,
                COUNTRY_COLUMNS,
                null,
                null,
                SORT_ORDER
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
