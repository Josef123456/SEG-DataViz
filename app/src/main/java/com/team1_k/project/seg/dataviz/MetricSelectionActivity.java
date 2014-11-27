package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.team1_k.project.seg.dataviz.api.QueryBuilder;
import com.team1_k.project.seg.dataviz.data.DataVizContract.MetricEntry;


public class MetricSelectionActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mMetricAdapter;

    private static final int METRIC_LOADER = 0 ;

    private static final String[] METRIC_COLUMNS = {
            MetricEntry.TABLE_NAME + "." + MetricEntry._ID,
            MetricEntry.COLUMN_NAME,
            MetricEntry.COLUMN_DESCRIPTION
    } ;

    public static final int COL_METRIC_ID = 0 ;
    public static final int COL_NAME = 1 ;
    public static final int COL_DESCRIPTION = 2 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLoaderManager().initLoader(METRIC_LOADER, null, this);

        mMetricAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.list_row_metric,
                null,
                new String[]{
                        MetricEntry.COLUMN_NAME//,MetricEntry.COLUMN_DESCRIPTION
                },
                new int[]{
                        R.id.mainText//,R.id.descriptionText
                },
                0
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metric_selection);
        ListView listView = (ListView) findViewById(R.id.metric_list_view);
        listView.setAdapter(mMetricAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String sortOrder = MetricEntry._ID + " ASC";

        return new CursorLoader(
                getApplicationContext(),
                MetricEntry.CONTENT_URI,
                METRIC_COLUMNS,
                null,
                null,
                sortOrder
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMetricAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMetricAdapter.swapCursor(null);
    }
}
