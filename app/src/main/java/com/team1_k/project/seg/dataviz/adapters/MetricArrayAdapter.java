package com.team1_k.project.seg.dataviz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.model.Metric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by alexstoick on 12/4/14.
 */
public class MetricArrayAdapter extends ArrayAdapter<Metric> {

    private Context mContext;

    static class ViewHolder {
        TextView mYearTextView;
        TextView mValueTextView;
        TextView mMetricDescriptionTextView;
        TextView mMetricNameTextView;

        public ViewHolder(View view) {
            this.mMetricDescriptionTextView = (TextView) view
                    .findViewById(R.id.dataPointMetricDescription);
            this.mMetricNameTextView = (TextView) view.findViewById(R.id.dataPointMetricName);
        }
    }

    private ArrayList<Metric> mMetrics = new ArrayList<Metric>();

    public MetricArrayAdapter(Context context, int resource, Metric[] objects) {
        super(context, resource, objects);
        mMetrics.addAll(Arrays.asList(objects));
        mContext = context ;
    }

    @Override
    public void clear() {
        mMetrics.clear();
    }

    @Override
    public void addAll(Collection<? extends Metric> collection) {
        mMetrics.addAll(collection);
    }

    @Override
    public int getCount() {
        return mMetrics.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if ( row == null ) {
            row = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_row_comparison,
                            parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Metric metric = mMetrics.get(position);

        holder.mMetricDescriptionTextView.setText(metric.getDescription());
        holder.mMetricNameTextView.setText(metric.getName());
        return row ;
    }
}

