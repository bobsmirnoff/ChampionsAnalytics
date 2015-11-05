package com.bobsmirnov.championsanalytics.model.formulas;

import android.util.Pair;

import com.bobsmirnov.championsanalytics.model.ScoreState;
import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.round;

/**
 * Created by bobsmirnov on 11.10.15.
 */
public class KomarovoFormula implements ScoreFormula {

    private final short SCALE_COEFF_NUMERATOR = 5;
    private final double DOMINATION_POWER = 0.8;
    private final short MAX_SCORE = 10;
    String log = "";

    @Override
    public Pair<Integer, Integer> getScore(ScoreState state) {
        log = "";
        int score1 = state.get(ScoreBoardPosition.LEFT);
        int score2 = state.get(ScoreBoardPosition.RIGHT);
        log += "score1: " + score1 + " score2: " + score2 + "\n";
        double domination = (double) Math.max(score1, score2) / Math.min(score1, score2);
        log += "domination: " + domination + "\n";
        final Random random = new Random();

        double goals1 = random.nextDouble() * Math.pow(domination, DOMINATION_POWER);
        double goals2 = random.nextDouble();

        log += "goals1: " + goals1 + " goals2: " + goals2 + "\n";

        double scaleCoeff = SCALE_COEFF_NUMERATOR / (Math.log(score1 + score2) + 1);
        log += "scaleCoeff: " + scaleCoeff + "\n";

        goals1 += random.nextDouble() * scaleCoeff;
        goals2 += random.nextDouble() * scaleCoeff;

        log += "goals1: " + goals1 + " goals2: " + goals2 + "\n";

        if (state.namesAreSame()) {
            final int draw = (short) round((goals1 + goals2) / 2);
            return new Pair<Integer, Integer>(draw, draw);
        }

        final Pair<Double, Double> p = score2 > score1 ? new Pair<>(goals2, goals1) : new Pair<>(goals1, goals2);
        final double homeAdvantage = score1 >= score2 ? Math.exp(-0.01 * (score1 - score2)) + 0.3 : 1.3;
        log += "home advantage = " + homeAdvantage;
        return new Pair<Integer, Integer>((int) round(asymptoteToMaxScore(p.first * homeAdvantage)),
                (int) round(asymptoteToMaxScore(p.second)));
    }

    private double asymptoteToMaxScore(double oldScore) {
        return round(MAX_SCORE / 2 * (cos(exp(1 - 0.1 * oldScore)) + 1));
    }

    @Override
    public String getLog() {
        return log;
    }
}
