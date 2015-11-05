package com.bobsmirnov.championsanalytics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
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
        final int CLUBS_LENGTH = db.getAllClubsNames().size() - 1;
        db.close();

        clubs.put(ScoreBoardPosition.LEFT, new Club(r.nextInt(CLUBS_LENGTH) + 1, this));
        clubs.put(ScoreBoardPosition.RIGHT, new Club(r.nextInt(CLUBS_LENGTH) + 1, this));

        final ScoreBoardController scoreBoardController = new ScoreBoardController(this,
                (TextView) findViewById(R.id.score_left),
                (TextView) findViewById(R.id.score_right),
                new KomarovoFormula());
        scoreBoardController.log = (TextView) findViewById(R.id.textView4);

        final ClubController[] clubControllers = new ClubController[]{
                new ClubController(this,
                        (ImageView) findViewById(R.id.logo_team_left),
                        (TableLayout) findViewById(R.id.table_left),
                        ScoreBoardPosition.LEFT),
                new ClubController(this,
                        (ImageView) findViewById(R.id.logo_team_right),
                        (TableLayout) findViewById(R.id.table_right),
                        ScoreBoardPosition.RIGHT)
        };

        final ScoreState state = new ScoreState();
        findViewById(R.id.logo_team_left).setOnClickListener(new Listener(this, scoreBoardController, clubControllers[0], state));
        findViewById(R.id.logo_team_right).setOnClickListener(new Listener(this, scoreBoardController, clubControllers[1], state));

        for (ClubController viewer : clubControllers) {
            viewer.visualize(clubs.get(viewer.position));
            state.put(viewer.position, clubs.get(viewer.position));
        }
        scoreBoardController.updateScore(state);

        Button newpair = (Button) findViewById(R.id.newpair);
        newpair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubs.put(ScoreBoardPosition.LEFT, new Club(r.nextInt(CLUBS_LENGTH) + 1, getApplicationContext()));
                clubs.put(ScoreBoardPosition.RIGHT, new Club(r.nextInt(CLUBS_LENGTH) + 1, getApplicationContext()));
                for (ClubController viewer : clubControllers) {
                    viewer.visualize(clubs.get(viewer.position));
                    state.put(viewer.position, clubs.get(viewer.position));
                }
                scoreBoardController.updateScore(state);
            }
        });

        Button retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreBoardController.updateScore(state);
            }
        });


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
        final Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
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
