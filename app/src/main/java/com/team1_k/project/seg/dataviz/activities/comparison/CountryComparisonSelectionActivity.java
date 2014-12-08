package com.team1_k.project.seg.dataviz.activities.comparison;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.activities.base.MainViewActivity;
import com.team1_k.project.seg.dataviz.activities.country.CountrySelectionActivity;
import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract;
import com.team1_k.project.seg.dataviz.data.DataVizDbHelper;
import com.team1_k.project.seg.dataviz.exchange_rate.ExchangeRatesActivity;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.Metric;
import com.team1_k.project.seg.dataviz.news.NewsActivity;

import java.util.ArrayList;


public class CountryComparisonSelectionActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG_METRIC_DATABASE_ID = "metric_database_id";
    public static final String TAG_METRIC_API_ID = "metric_api_id";
    private static final String SORT_ORDER =
            DataVizContract.CountryEntry.TABLE_NAME + "."
                    + DataVizContract.CountryEntry.COLUMN_NAME
                    + " ASC";
    private static final String LOG_TAG = "ui.comparison.country_selection";
    private static final int COUNTRY_LOADER = 0;
    protected EditText editText;
    private CursorAdapter mCountryAdapter;
    private long mMetricDatabaseId;
    private String mMetricApiId;
    private ArrayList<Long> mSelectedIds = new ArrayList<Long>();

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getApplicationContext(),
                DataVizContract.CountryEntry.CONTENT_URI,
                DataVizContract.CountryEntry.COLUMNS,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(COUNTRY_LOADER, null, this);
        setContentView(R.layout.activity_country_comparison_selection);
        final ListView listView = (ListView) findViewById(R.id.countryListWithCheckbox);
        final QueryBuilder queryBuilder = new QueryBuilder(this);

        mCountryAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice,
                null,
                new String[]{
                        DataVizContract.CountryEntry.COLUMN_NAME
                },
                new int[]{
                        android.R.id.text1
                },
                0
        );

        listView.setItemsCanFocus(false);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(mCountryAdapter);
        Button goButton = (Button) findViewById(R.id.comparisonGoButton);
        Intent intent = getIntent();
        mMetricDatabaseId = intent.getLongExtra(TAG_METRIC_DATABASE_ID, 0);
        mMetricApiId = intent.getStringExtra(TAG_METRIC_API_ID);
        mSelectedIds = new ArrayList<Long>();
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d ( LOG_TAG, "clicked on item at " + i ) ;
                Cursor currentValue = (Cursor)mCountryAdapter.getItem(i);

                String mCountryApiId= currentValue.getString(
                        DataVizContract.CountryEntry.INDEX_COLUMN_API_ID
                );
                long mCountryDatabaseId = currentValue.getLong(
                        DataVizContract.CountryEntry.INDEX_COLUMN_ID
                );
                if (mSelectedIds.contains(mCountryDatabaseId)) {
                    Log.w(LOG_TAG, "removing country form list");
                    mSelectedIds.remove(mCountryDatabaseId);
                } else {
                    Log.w(LOG_TAG, "adding country to list");
                    mSelectedIds.add(mCountryDatabaseId);
                }
                queryBuilder.fetchDataForCountryAndMetric(
                        new Country(mCountryApiId, mCountryDatabaseId),
                        new Metric(mMetricApiId, mMetricDatabaseId)
                );
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long[] array = new long[mSelectedIds.size()];
                for ( int i = 0 ; i < mSelectedIds.size(); ++ i ) {
                    array[i] = mSelectedIds.get(i);
                }
                if (mSelectedIds.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No country selection", Toast.LENGTH_LONG
                    ).show();
                } else {
                    Intent intent = new Intent(
                            getApplicationContext(),
                            CountryComparisonDetailActivity.class
                    ).putExtra(
                            CountryComparisonDetailActivity.TAG_COUNTRY_DATABASE_IDS,
                            array
                    ).putExtra(
                            CountryComparisonDetailActivity.TAG_METRIC_DATABASE_ID,
                            mMetricDatabaseId
                    );
                    startActivity(intent);
                }
            }
        });

        mCountryAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                String constraint = charSequence.toString();
                DataVizDbHelper mDbHelper = new DataVizDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                Log.i(LOG_TAG, "cnstr: " + constraint);
                if (constraint.equals("")) {
                    mSelectedIds = new ArrayList<Long>();
                }
                return db.query(
                        DataVizContract.CountryEntry.TABLE_NAME,
                        DataVizContract.CountryEntry.COLUMNS,
                        DataVizContract.CountryEntry.COLUMN_NAME + " LIKE ?",
                        new String[]{constraint + "%"},
                        null,
                        null,
                        SORT_ORDER
                );
            }
        });

        editText = (EditText) findViewById(R.id.search_globalDetail);
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
    protected void onResume() {
        super.onResume();
        //mSelectedIds = new ArrayList<Long>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_view, menu);
        return true;
    }

    /**
     * Inside the menu the user can easily change the activities by selecting the menu items.
     * There are five cases inside the switch statement.
     * The user can go to the main page (home), to see the news, exchange rate, countries and comparing the countries.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:
                Intent intentHome = new Intent(CountryComparisonSelectionActivity.this, MainViewActivity.class);
                this.startActivity(intentHome);
                break;
            case R.id.action_News:
                Intent intentNews = new Intent(CountryComparisonSelectionActivity.this, NewsActivity.class);
                this.startActivity(intentNews);
                break;
            case R.id.action_Markets:
                Intent intentMarkets = new Intent(CountryComparisonSelectionActivity.this, ExchangeRatesActivity.class);
                this.startActivity(intentMarkets);
                break;
            case R.id.action_Countries:
                Intent intentCountries = new Intent(CountryComparisonSelectionActivity.this, CountrySelectionActivity.class);
                this.startActivity(intentCountries);
                break;
            case R.id.action_More:
                Intent intentMore = new Intent(CountryComparisonSelectionActivity.this, ComparisonActivity.class);
                this.startActivity(intentMore);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
