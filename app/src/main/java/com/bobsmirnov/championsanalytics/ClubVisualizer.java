package com.bobsmirnov.championsanalytics;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bobsmirnov.championsanalytics.competitions.Competition;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bobsmirnov on 29.07.15.
 */
public class ClubVisualizer {

    public Context context;
    public TextView nameFrame;
    //    public ImageView img;
    public TableLayout table;
    private final TableRow.LayoutParams centeredHeaderParams =
            new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

    public ClubVisualizer(Context context, TextView name, ImageView img, TableLayout table) {
        this.context = context;
        this.nameFrame = name;
//        this.img = img;
        this.table = table;
    }

    public void visualize(Club club) {
        nameFrame.setOnClickListener(new Listener());
//        img.setOnClickListener(new Listener());
        nameFrame.setText(club.getName());
//        img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));

        table.addView(drawHeaderRow("Trophies"));

        final HashMap<Competition, Integer> trophies = club.getTrophies();
        int i = 1;
        for (Competition cup : trophies.keySet()) {
            ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.trophey_row, table);
            ((TextView) ((TableRow) table.getChildAt(i)).getChildAt(0)).setText(cup.viewName);
            ((TextView) ((TableRow) table.getChildAt(i)).getChildAt(1)).setText(trophies.get(cup) + "");
            i++;
        }

        table.addView(drawHeaderRow("Legends"));

//        final ArrayList<String> legends = club.getLegends();
//        for (String l : legends) {
//            final TextView legView = new TextView(context);
//            legView.setText(l);
//            legView.setLayoutParams(centeredHeaderParams);
//            final TableRow tr = new TableRow(context);
//            tr.addView(legView);
//            table.addView(tr);
//        }
    }

    private TableRow drawHeaderRow(String bannerText) {
        centeredHeaderParams.gravity = Gravity.CENTER;
        centeredHeaderParams.weight = 1;
        final TextView header1 = new TextView(context);
        header1.setText(bannerText);
        header1.setGravity(Gravity.CENTER);
        header1.setLayoutParams(centeredHeaderParams);
        final TableRow tr1 = new TableRow(context);
        tr1.setGravity(Gravity.CENTER);
        tr1.addView(header1);
        return tr1;
    }

    private class Listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO: call dialog
        }
    }
}

