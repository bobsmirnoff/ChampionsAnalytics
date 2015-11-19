package com.bobsmirnov.championsanalytics.view;

/**
 * Created by bobsmirnov on 12.10.15
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bobsmirnov.championsanalytics.R;
import com.bobsmirnov.championsanalytics.controller.ClubController;
import com.bobsmirnov.championsanalytics.controller.GridViewAdapter;
import com.bobsmirnov.championsanalytics.controller.ScoreBoardController;
import com.bobsmirnov.championsanalytics.model.Club;
import com.bobsmirnov.championsanalytics.model.ScoreState;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Listener implements View.OnClickListener {

    private Context context;
    private ScoreBoardController scoreBoardController;
    private ClubController clubController;
    private ScoreState state;
    private TreeMap<String, Long> data;
    private String excluded;

    public Listener(Context context,
                    ScoreBoardController scoreBoardController,
                    ClubController clubController,
                    ScoreState state, TreeMap<String, Long> dbData) {
        this.context = context;
        this.scoreBoardController = scoreBoardController;
        this.clubController = clubController;
        this.state = state;
        this.data = dbData;
    }

    @Override
    public void onClick(View v) {
        excluded = state.getOtherPositionClub(clubController.position).getName();
        final String[] items = new String[data.keySet().size()];
        int i = 0;
        for (final String name : data.keySet()) {
            if (!name.equals(excluded)) items[i++] = name;
        }

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.grid);
        GridView gridView = (GridView) dialog.findViewById(R.id.grid);
        GridViewAdapter adapter = new GridViewAdapter(context, R.layout.grid_item, getData());
        gridView.setAdapter(adapter);
        dialog.setCancelable(true);
        dialog.setTitle("Choose " + (clubController.position.equals(ScoreBoardPosition.LEFT) ? "home" : "away") + " team");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clubController.visualize(new Club(data.get(items[position]), context));
                state.put(clubController.position, new Club(data.get(items[position]), context));
                scoreBoardController.updateScore(state);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private List<GridItem> getData() {
        List<GridItem> list = new ArrayList<>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        for (String n : data.keySet()) {
            if (!n.equals(excluded)) {
                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                        context.getResources().getIdentifier(Club.getLogoName(n), "drawable", context.getPackageName()),
                        options);
                list.add(new GridItem(Bitmap.createScaledBitmap(bitmap, 250, 250, false), n));
            }
        }
        return list;
    }
}
