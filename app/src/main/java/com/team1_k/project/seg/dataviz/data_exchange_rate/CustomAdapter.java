package com.team1_k.project.seg.dataviz.data_exchange_rate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.team1_k.project.seg.dataviz.R;

import java.util.List;

/**
 * Created by dbrisingr on 01/12/14.
 */
public class CustomAdapter extends BaseAdapter {

    private final List<ExchangeItem> items;
    private final Context context;

    public CustomAdapter(Context context, List<ExchangeItem> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.exchange_item, null);
            holder = new ViewHolder();
            holder.textLabel = (TextView) convertView.findViewById(R.id.textLabel);
            holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.itemDifference = (TextView) convertView.findViewById(R.id.itemDifference);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textLabel.setText(items.get(position).getLabel());
        holder.itemTitle.setText(items.get(position).getTitle());
        holder.itemDifference.setText(items.get(position).getDifference());
        System.out.println(items.get(position).getDifference());
        return convertView;
    }

    static class ViewHolder {
        TextView textLabel;
        TextView itemTitle;
        TextView itemDifference;
    }
}