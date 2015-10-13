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
import android.widget.Toast;

import com.bobsmirnov.championsanalytics.controller.ClubController;
import com.bobsmirnov.championsanalytics.controller.ScoreController;
import com.bobsmirnov.championsanalytics.model.Club;
import com.bobsmirnov.championsanalytics.model.formulas.KomarovoFormula;
import com.bobsmirnov.championsanalytics.view.Listener;
import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final HashMap<ScoreBoardPosition, Club> clubs = new HashMap<>();
        clubs.put(ScoreBoardPosition.LEFT, new Club(12, this));        // TODO: Generate randomly
        clubs.put(ScoreBoardPosition.RIGHT, new Club(21, this));

        final ScoreController scoreController = new ScoreController(this,
                (TextView) findViewById(R.id.score_left),
                (TextView) findViewById(R.id.score_right),
                new KomarovoFormula());
        scoreController.log = (TextView) findViewById(R.id.textView4);

        final ClubController[] clubControllers = new ClubController[]{
                new ClubController(this,
                        (TextView) findViewById(R.id.name_team_left),
                        (ImageView) findViewById(R.id.logo_team_left),
                        (TableLayout) findViewById(R.id.table_left),
                        ScoreBoardPosition.LEFT),
                new ClubController(this,
                        (TextView) findViewById(R.id.name_team_right),
                        (ImageView) findViewById(R.id.logo_team_right),
                        (TableLayout) findViewById(R.id.table_right),
                        ScoreBoardPosition.RIGHT)
        };

        findViewById(R.id.name_team_left).setOnClickListener(new Listener(this, scoreController, clubControllers[0]));
        findViewById(R.id.logo_team_left).setOnClickListener(new Listener(this, scoreController, clubControllers[0]));
        findViewById(R.id.name_team_right).setOnClickListener(new Listener(this, scoreController, clubControllers[1]));
        findViewById(R.id.logo_team_right).setOnClickListener(new Listener(this, scoreController, clubControllers[1]));

        for (ClubController viewer : clubControllers) {
            viewer.visualize(clubs.get(viewer.position));
            scoreController.updateScore(viewer.position, clubs.get(viewer.position));
        }

        ImageButton fab = (ImageButton) findViewById(R.id.fabButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image = saveScreenshot();
                if (image == null)
                    Toast.makeText(getApplicationContext(), "OLOLO", Toast.LENGTH_LONG).show();

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
