package com.team1_k.project.seg.dataviz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.model.DataPoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by alexstoick on 12/4/14.
 */
public class DataPointArrayAdapter extends ArrayAdapter<DataPoint> {

    private Context mContext;

    static class ViewHolder {
        TextView mYearTextView;
        TextView mValueTextView;
        TextView mMetricDescriptionTextView;
        TextView mMetricNameTextView;

        public ViewHolder(View view) {
            this.mYearTextView = (TextView) view.findViewById(R.id.dataPointYear);
            this.mValueTextView = (TextView) view.findViewById(R.id.dataPointValue);
            this.mMetricDescriptionTextView = (TextView) view
                    .findViewById(R.id.dataPointMetricDescription);
            this.mMetricNameTextView = (TextView) view.findViewById(R.id.dataPointMetricName);
        }
    }

    private ArrayList<DataPoint> mDataPoints = new ArrayList<DataPoint>();

    public DataPointArrayAdapter(Context context, int resource, DataPoint[] objects) {
        super(context, resource, objects);
        mDataPoints.addAll(Arrays.asList(objects));
        mContext = context ;
    }

    @Override
    public void clear() {
        mDataPoints.clear();
    }

    @Override
    public void addAll(Collection<? extends DataPoint> collection) {
        mDataPoints.addAll(collection);
    }

    @Override
    public int getCount() {
        return mDataPoints.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if ( row == null ) {
            row = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_row_country_detail_data_point,
                            parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        DataPoint dataPoint = mDataPoints.get(position);

        int year = dataPoint.getYear();
        Double value = dataPoint.getValue();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        holder.mValueTextView.setText("Value: " + decimalFormat.format(value));
        holder.mYearTextView.setText("Latest year:" + decimalFormat.format(year));
        holder.mMetricDescriptionTextView.setText(dataPoint.getMetric().getDescription());
        holder.mMetricNameTextView.setText(dataPoint.getMetric().getName());
        return row ;
    }
}
