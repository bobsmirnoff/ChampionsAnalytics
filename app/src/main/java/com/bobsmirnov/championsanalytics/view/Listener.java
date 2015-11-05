package com.bobsmirnov.championsanalytics.view;

/**
 * Created by bobsmirnov on 12.10.15
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.bobsmirnov.championsanalytics.controller.ClubController;
import com.bobsmirnov.championsanalytics.controller.ScoreBoardController;
import com.bobsmirnov.championsanalytics.db.DBWorker;
import com.bobsmirnov.championsanalytics.model.Club;
import com.bobsmirnov.championsanalytics.model.ScoreState;

import java.io.IOException;
import java.util.TreeMap;

public class Listener implements View.OnClickListener {

    private Context context;
    private ScoreBoardController scoreBoardController;
    private ClubController clubController;
    private ScoreState state;

    public Listener(Context context, ScoreBoardController scoreBoardController, ClubController clubController, ScoreState state) {
        this.context = context;
        this.scoreBoardController = scoreBoardController;
        this.clubController = clubController;
        this.state = state;
    }

    @Override
    public void onClick(View v) {
        final DBWorker db = new DBWorker(context);
        try {
            db.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final TreeMap<String, Long> data = db.getAllClubsNames();
        db.close();
        final String[] items = new String[data.keySet().size()];
        int i = 0;
        for (final String name : data.keySet()) {
            items[i++] = name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clubController.visualize(new Club(data.get(items[which]), context));
                state.put(clubController.position, new Club(data.get(items[which]), context));
                scoreBoardController.updateScore(state);
            }
        });
        builder.show();
    }
}
