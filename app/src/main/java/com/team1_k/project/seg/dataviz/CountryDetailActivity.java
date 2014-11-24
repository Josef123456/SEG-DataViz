package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract;
import com.team1_k.project.seg.dataviz.data.DataVizContract.DataPointEntry;
import com.team1_k.project.seg.dataviz.model.Client;
import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.Metric;

import java.text.DecimalFormat;


public class CountryDetailActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "ui.country.detail" ;

    protected static final String TAG_COUNTRY_ID = "COUNTRY_ID" ;

    private static final int DATA_POINT_LOADER = 0 ;
    private static final String[] DATA_POINT_COLUMNS = {
            DataPointEntry.COLUMN_YEAR,
            DataPointEntry.COLUMN_VALUE
    } ;

    private QueryBuilder mQueryBuilder;
    private Intent mIntent ;
    private Country mCountry ;
    private Client mClient ;
    private Metric[] mMetrics;
    private CursorAdapter mDataPointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        mIntent = getIntent();
        mQueryBuilder = new QueryBuilder(getApplicationContext());
        fetchClient();
        fetchCountry();
        fetchMetrics();
        fetchStats();
        getLoaderManager().initLoader(DATA_POINT_LOADER, null, this);
    }

    private void fetchStats() {
        for ( Metric metric: mMetrics) {
            mQueryBuilder.fetchDataForCountryAndMetric(mCountry, metric);
        }

        mDataPointAdapter = new DataPointAdapter(getApplicationContext(), null, 0);

        ListView listView = (ListView) findViewById(R.id.countryDataPointListView);
        listView.setAdapter(mDataPointAdapter);
    }

    private void fetchMetrics() {
        String[] metric_api_ids = mClient.getType().getInterests() ;
        int length = metric_api_ids.length ;
        mMetrics = new Metric[length];
        try {
            for (int i = 0; i < length; ++i) {
                mMetrics[i] = Metric.getMetricWithApiId(getApplicationContext(), metric_api_ids[i]);
            }
        } catch ( Exception e ) {
            Log.e( LOG_TAG , e.toString());
            e.printStackTrace();
        }
    }

    private void fetchCountry() {
        String country_api_id = mIntent.getStringExtra(TAG_COUNTRY_ID);
        try {
            mCountry = Country.getCountryWithApiId(getApplicationContext(), country_api_id);
        } catch ( Exception e ) {
            Log.e(LOG_TAG, e.toString() );
            e.printStackTrace();
        }
    }


    private void fetchClient() {
        //TODO: get this from somewhere in the app
        mClient = new Client(Client.Type.INVESTOR);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = DataPointEntry.TABLE_NAME + "." + DataPointEntry.COLUMN_YEAR + " DESC";
        return new CursorLoader(
                getApplicationContext(),
                DataVizContract.CountryEntry.buildCountryWithMetricUriWithId(mCountry.getDatabaseId()),
                DataVizContract.MetricEntry.COLUMNS_FOR_METRIC_QUERY,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {



        mDataPointAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mDataPointAdapter.swapCursor(null);
    }


    public static class DataPointAdapter extends CursorAdapter {

        private Context mContext;

        static class ViewHolder {
            TextView mYearTextView;
            TextView mValueTextView;

            public ViewHolder(View view) {
                this.mYearTextView = (TextView) view.findViewById(R.id.dataPointYear);
                this.mValueTextView = (TextView) view.findViewById(R.id.dataPointValue);
            }
        }

        public DataPointAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            mContext = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.list_row_country_detail_data_point,
                            viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            Double year = cursor.getDouble(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_YEAR
            );
            Double value = cursor.getDouble(
                    DataVizContract.MetricEntry.INDEX_METRIC_QUERY_COLUMN_VALUE
            );
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            viewHolder.mValueTextView.setText(decimalFormat.format(value));
            viewHolder.mYearTextView.setText(decimalFormat.format(year));
        }
    }

}
