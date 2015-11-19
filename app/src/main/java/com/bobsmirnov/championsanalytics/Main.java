package com.bobsmirnov.championsanalytics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bobsmirnov.championsanalytics.controller.ClubController;
import com.bobsmirnov.championsanalytics.controller.ScoreBoardController;
import com.bobsmirnov.championsanalytics.db.DBWorker;
import com.bobsmirnov.championsanalytics.model.Club;
import com.bobsmirnov.championsanalytics.model.ScoreState;
import com.bobsmirnov.championsanalytics.model.formulas.KomarovoFormula;
import com.bobsmirnov.championsanalytics.view.Listener;
import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;


public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final HashMap<ScoreBoardPosition, Club> clubs = new HashMap<>();
        final Random r = new Random(System.currentTimeMillis());
        final DBWorker db = new DBWorker(this);
        try {
            db.open();
        } catch (IOException e) {
        }
        final TreeMap<String, Long> dbData = db.getAllClubsNames();
        final int CLUBS_LENGTH = dbData.size() - 1;
        db.close();

        long firstClub = r.nextInt(CLUBS_LENGTH) + 1;
        long secondClub;
        do secondClub = r.nextInt(CLUBS_LENGTH) + 1; while (secondClub == firstClub);
        clubs.put(ScoreBoardPosition.LEFT, new Club(firstClub, this));
        clubs.put(ScoreBoardPosition.RIGHT, new Club(secondClub, this));
        final ScoreState state = new ScoreState(clubs);

        final ScoreBoardController scoreBoardController = new ScoreBoardController(this,
                (TextView) findViewById(R.id.score_left),
                (TextView) findViewById(R.id.score_right),
                new KomarovoFormula());
//        scoreBoardController.log = (TextView) findViewById(R.id.textView4);

        final ClubController[] clubControllers = new ClubController[]{
                new ClubController(this,
                        (ImageView) findViewById(R.id.logo_team_left),
                        (TextView) findViewById(R.id.title_team_left),
                        (TableLayout) findViewById(R.id.table_left),
                        ScoreBoardPosition.LEFT),
                new ClubController(this,
                        (ImageView) findViewById(R.id.logo_team_right),
                        (TextView) findViewById(R.id.title_team_right),
                        (TableLayout) findViewById(R.id.table_right),
                        ScoreBoardPosition.RIGHT)
        };

        final ImageView swapButton = (ImageView) findViewById(R.id.swap);
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.swap();
                clubControllers[0].visualize(state.get(ScoreBoardPosition.LEFT));
                clubControllers[1].visualize(state.get(ScoreBoardPosition.RIGHT));
                scoreBoardController.updateScore(state);
            }
        });

        findViewById(R.id.logo_team_left).setOnClickListener(new Listener(this, scoreBoardController,
                clubControllers[0], state, dbData));
        findViewById(R.id.logo_team_right).setOnClickListener(new Listener(this, scoreBoardController,
                clubControllers[1], state, dbData));

        for (ClubController viewer : clubControllers) {
            viewer.visualize(clubs.get(viewer.position));
            state.put(viewer.position, clubs.get(viewer.position));
        }
        scoreBoardController.updateScore(state);

//        Button newpair = (Button) findViewById(R.id.newpair);
//        newpair.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clubs.put(ScoreBoardPosition.LEFT, new Club(r.nextInt(CLUBS_LENGTH) + 1, getApplicationContext()));
//                clubs.put(ScoreBoardPosition.RIGHT, new Club(r.nextInt(CLUBS_LENGTH) + 1, getApplicationContext()));
//                for (ClubController viewer : clubControllers) {
//                    viewer.visualize(clubs.get(viewer.position));
//                    state.put(viewer.position, clubs.get(viewer.position));
//                }
//                scoreBoardController.updateScore(state);
//            }
//        });

//        Button retry = (Button) findViewById(R.id.retry);
//        retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scoreBoardController.updateScore(state);
//            }
//        });


        ImageButton fab = (ImageButton) findViewById(R.id.fabButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image = saveScreenshot();
                Uri screenshotUri = Uri.fromFile(image);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });
    }

    public File saveScreenshot() {
        View rootView = getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        findViewById(R.id.fabButton).setVisibility(View.INVISIBLE);
        final Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        findViewById(R.id.fabButton).setVisibility(View.VISIBLE);
        rootView.setDrawingCacheEnabled(false);

        final File image = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)) + "/score.png");
        FileOutputStream output;
        try {
            output = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
            return image;
        } catch (IOException e) {
            return null;
        }
    }
}
