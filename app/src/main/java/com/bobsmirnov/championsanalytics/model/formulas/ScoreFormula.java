package com.bobsmirnov.championsanalytics.model.formulas;

import android.util.Pair;

import com.bobsmirnov.championsanalytics.model.ScoreState;

/**
 * Created by bobsmirnov on 02.10.15.
 */

public interface ScoreFormula {
    public Pair<Integer, Integer> getScore(ScoreState state);

    public String getLog();
}
