package com.bobsmirnov.championsanalytics.model.formulas;

import android.util.Pair;

import com.bobsmirnov.championsanalytics.model.ScoreState;
import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.util.Random;

/**
 * Created by bobsmirnov on 11.10.15.
 */
public class KomarovoFormula implements ScoreFormula {

    String log = "";

    @Override
    public Pair<Integer, Integer> getScore(ScoreState state) {
        log = "";
        int score1 = state.get(ScoreBoardPosition.LEFT);
        int score2 = state.get(ScoreBoardPosition.RIGHT);
        log += "score1: " + score1 + " score2: " + score2 + "\n";
        double coeff = (double) Math.max(score1, score2) / Math.min(score1, score2);
        log += "coeff: " + coeff + "\n";
        final Random random = new Random();

        double goals1 = random.nextDouble() * Math.pow(coeff, 0.8);
        double goals2 = random.nextDouble();

        log += "goals1: " + goals1 + " goals2: " + goals2 + "\n";

        double randCoeff = 5 / (Math.log(score1 + score2) + 1);
        log += "randCoeff: " + randCoeff + "\n";

        goals1 += random.nextDouble() * randCoeff;
        goals2 += random.nextDouble() * randCoeff;

        log += "goals1: " + goals1 + " goals2: " + goals2 + "\n";

        if (state.namesAreSame()) {
            final int drawScore = (int) (Math.round((goals1 + goals2) / 2));
            return new Pair<>(((int) Math.round((goals1 + goals2) / 2)), drawScore);
        }

        final Pair<Double, Double> p = score2 > score1 ? new Pair<>(goals2, goals1) : new Pair<>(goals1, goals2);
        return new Pair<>((int) Math.round(p.first * 1.5), (int) Math.round(p.second));
    }

    @Override
    public String getLog() {
        return log;
    }
}
