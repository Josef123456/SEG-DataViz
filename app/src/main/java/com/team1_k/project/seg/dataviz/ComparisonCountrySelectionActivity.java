package com.team1_k.project.seg.dataviz;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class ComparisonCountrySelectionActivity extends Activity {

    private static final String LOG_TAG = "ui.comparison.country_selection" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_country_selection);
        final ListView listView = (ListView) findViewById(R.id.countryListWithCheckbox);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                new String[] { "test", "test2","test", "test2","test", "test2","test", "test2","test", "test2","test", "test2"});
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        Button goButton = (Button) findViewById(R.id.comparisonGoButton);

        goButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray array = listView.getCheckedItemPositions();
                for ( int i = 0 ; i < array.size() ; ++ i ) {
                    Log.d(LOG_TAG, String.valueOf(array.keyAt(i)));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comparison_country_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
