package com.bobsmirnov.championsanalytics.controller;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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
import com.bobsmirnov.championsanalytics.view.ClubColors;
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
    public ImageView img;
    public TextView title;
    public TableLayout table;
    public ScoreBoardPosition position;

    public ClubController(Context context, ImageView img, TextView title, TableLayout table, ScoreBoardPosition position) {
        this.context = context;
        this.img = img;
        this.title = title;
        this.table = table;
        this.position = position;
    }

    public void visualize(Club club) {
        img.setImageResource(context.getResources().getIdentifier(club.getLogoName(), "drawable", context.getPackageName()));
        final String clubName = club.getName();
        title.setText(clubName);
        table.removeAllViews();
        drawHeaderRow("Trophies");
        drawTrophyRows(club.getTrophies());
        drawHeaderRow("Legends");
        drawLegendRows(club.getLegends(), clubName);
        final int[] clubColors = club.getColors();
        final int length = clubColors.length;
        if (length > 0) {
            int[] gradientColors = new int[2 * (length + 1)];
            gradientColors[0] = ClubColors.getColor("white");
            gradientColors[1] = ClubColors.getColor("white");
            System.arraycopy(duplicate(clubColors), 0, gradientColors, 2, 2 * length);
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    gradientColors);
            gd.setAlpha(60);
            table.setBackgroundDrawable(gd);
        } else table.setBackgroundResource(R.drawable.abc_item_background_holo_light);
    }

    private int[] duplicate(int[] src) {
        final int initialLength = src.length;
        final int[] res = new int[initialLength * 2];
        int j = 0;
        for (int s : src) {
            res[j++] = s;
            res[j++] = s;
        }
        return res;
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
        for (final Competition cup : orderedCups) {
            ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.trophey_row, table);
            ((TextView) ((TableRow) table.getChildAt(i)).getChildAt(0)).setText(cup.viewName);
            ((TextView) ((TableRow) table.getChildAt(i++)).getChildAt(1)).setText(trophies.get(cup) + "");
        }
    }

    private void drawLegendRows(ArrayList<String> legends, String clubName) {
        if (legends.size() == 0) {
            final TextView legendText = new TextView(context);
            legendText.setText("Sorry, not a single player has proved to be a memorable " + clubName + " legend so far");
            legendText.setLines(5);
            legendText.setTypeface(Typeface.create("sans-serif", Typeface.ITALIC));
            legendText.setTextColor(context.getResources().getColor(R.color.button_material_dark));
            legendText.setGravity(Gravity.CENTER);
            legendText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            table.addView(legendText);
        } else {
            Collections.shuffle(legends);
            for (final String l : legends) {
                final TextView legendText = new TextView(context);
                legendText.setText(l);
                legendText.setGravity(Gravity.CENTER);
                legendText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                final TableRow tr = new TableRow(context);
                tr.setGravity(Gravity.CENTER);
                tr.addView(legendText);
                table.addView(tr);
            }
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
