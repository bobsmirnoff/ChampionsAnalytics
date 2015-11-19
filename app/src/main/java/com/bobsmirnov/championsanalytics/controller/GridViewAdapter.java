package com.bobsmirnov.championsanalytics.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobsmirnov.championsanalytics.R;
import com.bobsmirnov.championsanalytics.view.GridItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobsmirnov on 14.11.15
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List data = new ArrayList();

    public GridViewAdapter(Context context, int resource, List data) {
        super(context, resource, data);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        ViewHolder holder;
        if (cell == null) {
            cell = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) cell.findViewById(R.id.text);
            holder.image = (ImageView) cell.findViewById(R.id.image);
            cell.setTag(holder);
        } else holder = (ViewHolder) cell.getTag();
        GridItem item = (GridItem) data.get(position);
        holder.title.setText(item.getTitle());
        holder.image.setImageBitmap(item.getBitmap());
        return cell;
    }

    static class ViewHolder {
        TextView title;
        ImageView image;
    }
}
