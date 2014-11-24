package com.team1_k.project.seg.dataviz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.*;
import android.widget.*;
import com.team1_k.project.seg.dataviz.data_news.RssAdapter;
import com.team1_k.project.seg.dataviz.data_news.RssItem;
import com.team1_k.project.seg.dataviz.data_news.RssService;

import java.util.List;


/**
 * Activity to show RSS Feed of News (possibly from Bloomberg)
 */

public class NewsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent=getIntent();

        Intent x = new Intent();
        RssFragment trial = new RssFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, trial).commit();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   @SuppressLint("ValidFragment")
   public class RssFragment extends Fragment implements AdapterView.OnItemClickListener {

        private ProgressBar progressBar;
        private ListView listView;
        private View view;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (view == null) {
                view = inflater.inflate(R.layout.fragment_news, container, false);
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                listView = (ListView) view.findViewById(R.id.listView);
                listView.setOnItemClickListener(this);
                startService();
            } else {
                // If we are returning from a configuration change:
                // "view" is still attached to the previous view hierarchy
                // so we need to remove it and re-attach it to the current one
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
            }
            return view;
        }

        private void startService() {
            Intent intent = new Intent(getActivity(), RssService.class);
            intent.putExtra(RssService.RECEIVER, resultReceiver);
            getActivity().startService(intent);
        }

        /**
         * Once the {@link RssService} finishes its task, the result is sent to this ResultReceiver.
         */
        private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
            @SuppressWarnings("unchecked")
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                List<RssItem> items = (List<RssItem>) resultData.getSerializable(RssService.ITEMS);
                if (items != null) {
                    RssAdapter adapter = new RssAdapter(getActivity(), items);
                    listView.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), "An error occured while downloading the rss feed.",
                            Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            };
        };

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RssAdapter adapter = (RssAdapter) parent.getAdapter();
            RssItem item = (RssItem) adapter.getItem(position);
            Uri uri = Uri.parse(item.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    static class ViewHolder {
        TextView itemTitle;
    }
}
