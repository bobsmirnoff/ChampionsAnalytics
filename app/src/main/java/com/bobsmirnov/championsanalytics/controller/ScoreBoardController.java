package com.bobsmirnov.championsanalytics.controller;

import android.content.Context;
import android.util.Pair;
import android.widget.TextView;

import com.bobsmirnov.championsanalytics.model.ScoreState;
import com.bobsmirnov.championsanalytics.model.formulas.ScoreFormula;

/**
 * Created by bobsmirnov on 29.07.15.
 */
public class ScoreBoardController {
    public Context context;
    public TextView log;
    private TextView leftScore;
    private TextView rightScore;
    private ScoreFormula formula;

    public ScoreBoardController(Context context, TextView leftScore, TextView rightScore, ScoreFormula formula) {
        this.context = context;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
        this.formula = formula;
    }

    public void updateScore(ScoreState state) {
        final Pair<Integer, Integer> formulaResult = formula.getScore(state);
        log.setText(formula.getLog());
        leftScore.setText("" + formulaResult.first);
        rightScore.setText("" + formulaResult.second);
    }
}
