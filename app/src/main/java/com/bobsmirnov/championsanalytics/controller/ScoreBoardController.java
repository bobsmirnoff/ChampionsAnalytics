package com.bobsmirnov.championsanalytics.controller;

import android.content.Context;
import android.util.Pair;
import android.widget.TextView;

import com.bobsmirnov.championsanalytics.model.Club;
import com.bobsmirnov.championsanalytics.model.ScoreState;
import com.bobsmirnov.championsanalytics.model.formulas.ScoreFormula;
import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

/**
 * Created by bobsmirnov on 29.07.15.
 */
public class ScoreBoardController {
    public Context context;
    public TextView log;
    private TextView leftScore;
    private TextView rightScore;
    private ScoreFormula formula;
    private ScoreState state;

    public ScoreBoardController(Context context, TextView leftScore, TextView rightScore, ScoreFormula formula) {
        this.context = context;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
        this.formula = formula;
        this.state = new ScoreState();
    }

    public void updateScore(ScoreBoardPosition position, Club club) {
        state.put(position, club);
        final Pair<Integer, Integer> formulaResult = formula.getScore(state);
        log.setText(formula.getLog());
        leftScore.setText("" + formulaResult.first);
        rightScore.setText("" + formulaResult.second);
    }
}
