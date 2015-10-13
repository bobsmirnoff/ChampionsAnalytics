package com.bobsmirnov.championsanalytics.controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bobsmirnov.championsanalytics.R;
import com.bobsmirnov.championsanalytics.model.Club;
import com.bobsmirnov.championsanalytics.model.competitions.Competition;
import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bobsmirnov on 29.07.15.
 */
public class ClubController {

    private final TableRow.LayoutParams centeredHeaderParams =
            new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
    public Context context;
    public TextView name;
    public ImageView img;
    public TableLayout table;
    public ScoreBoardPosition position;

    public ClubController(Context context, TextView name, ImageView img, TableLayout table, ScoreBoardPosition position) {
        this.context = context;
        this.name = name;
        this.img = img;
        this.table = table;
        this.position = position;
    }

    public void visualize(Club club) {
        name.setText(club.getName());
        img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));

        table.removeAllViews();
        drawHeaderRow("Trophies");
        drawTrophyRows(club.getTrophies());
        drawHeaderRow("Legends");
        drawLegendRows(club.getLegends());
    }

    private void drawTrophyRows(HashMap<Competition, Integer> trophies) {
        int i = 1;

        int j = 0;
        int priorities[] = new int[trophies.size()];
        for (final Competition cup : trophies.keySet()) {
            priorities[j++] = cup.priority;
        }

        Arrays.sort(priorities);
        List<Competition> orderedCups = new ArrayList<>(trophies.size());
        for (int p : priorities) {
            for (final Competition cup : trophies.keySet()) {
                if (cup.priority == p)
                    orderedCups.add(cup);
            }
        }

        for (Competition cup : orderedCups) {
            ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.trophey_row, table);
            ((TextView) ((TableRow) table.getChildAt(i)).getChildAt(0)).setText(cup.viewName);
            ((TextView) ((TableRow) table.getChildAt(i++)).getChildAt(1)).setText(trophies.get(cup) + "");
        }
    }

    private void drawLegendRows(ArrayList<String> legends) {
        Collections.shuffle(legends);
        for (final String l : legends) {
            final TextView legendText = new TextView(context);
            legendText.setText(l);
            legendText.setGravity(Gravity.CENTER);
            legendText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            final TableRow tr = new TableRow(context);
            tr.setGravity(Gravity.CENTER);
            tr.addView(legendText);
            table.addView(tr);
        }
    }

    private void drawHeaderRow(String bannerText) {
        centeredHeaderParams.gravity = Gravity.CENTER;
        centeredHeaderParams.weight = 1;
        centeredHeaderParams.setMargins(0, 50, 0, 30);
        final TextView header1 = new TextView(context);
        header1.setText(bannerText);
        header1.setTypeface(header1.getTypeface(), Typeface.BOLD);
        header1.setGravity(Gravity.CENTER);
        header1.setLayoutParams(centeredHeaderParams);
        final TableRow tr = new TableRow(context);
        tr.setGravity(Gravity.CENTER);
        tr.addView(header1);
        table.addView(tr);
    }
}
