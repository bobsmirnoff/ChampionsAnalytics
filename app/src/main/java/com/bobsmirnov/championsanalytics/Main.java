package com.bobsmirnov.championsanalytics;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<Club> clubs = new ArrayList<>(2);
        clubs.add(new Club(2, this));
        clubs.add(new Club(1, this));

        ArrayList<ClubVisualizer> drawers = new ArrayList<>(2);
        drawers.add(0, new ClubVisualizer(this,
                (TextView) findViewById(R.id.name_team1),
                (ImageView) findViewById(R.id.image_team1),
                (TableLayout) findViewById(R.id.table_1)));

        drawers.add(1, new ClubVisualizer(this,
                (TextView) findViewById(R.id.name_team2),
                (ImageView) findViewById(R.id.image_team2),
                (TableLayout) findViewById(R.id.table_2)));

        for (int i = 0; i < 2; i++) {
            drawers.get(i).visualize(clubs.get(i));
        }

    }


}
