package com.bobsmirnov.championsanalytics.model.formulas;

import android.util.Pair;

import com.bobsmirnov.championsanalytics.model.Club;
import com.bobsmirnov.championsanalytics.model.ScoreState;
import com.bobsmirnov.championsanalytics.model.competitions.Competition;
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
        final Club club1 = state.get(ScoreBoardPosition.LEFT);
        final Club club2 = state.get(ScoreBoardPosition.RIGHT);
        int score1 = getPoints(club1);
        int score2 = getPoints(club2);
        log += "score1: " + score1 + " score2: " + score2 + "\n";
        double domination = (double) Math.max(score1, score2) / Math.min(score1, score2);
        log += "domination: " + domination + "\n";
        final Random random = new Random();

        double goals1 = random.nextDouble() * Math.pow(domination, DOMINATION_POWER);
        double goals2 = 0;

        log += "goals1: " + goals1 + " goals2: " + goals2 + "\n";

        double scaleCoeff = SCALE_COEFF_NUMERATOR / (Math.sqrt(score1 + score2) + 1);
        log += "scale coeff: " + scaleCoeff + "\n";

        goals1 += random.nextDouble() * scaleCoeff;
        goals2 += random.nextDouble() * scaleCoeff;

        log += "goals1: " + goals1 + " goals2: " + goals2 + "\n";

        final Pair<Double, Double> p = score2 > score1 ? new Pair<>(goals2, goals1) : new Pair<>(goals1, goals2);
        final double homeAdvantage = score1 >= score2 ? Math.exp(-0.01 * (score1 - score2)) + 0.3 : 1.3;
        log += "home advantage = " + homeAdvantage;
        return new Pair<>((int) round(asymptoteToMaxScore(p.first * homeAdvantage)),
                (int) round(asymptoteToMaxScore(p.second)));
    }

    private double asymptoteToMaxScore(double oldScore) {
        return round(MAX_SCORE / 2 * (cos(exp(1 - 0.1 * oldScore)) + 1));
    }

    private int getPoints(Club club) {
        int score = club.getLegends().size();
        for (Competition c : club.getTrophies().keySet()) {
            if (club.getTrophies().get(c) > 0)
                score += club.getTrophies().get(c);
        }
        return score;
    }

    @Override
    public String getLog() {
        return log;
    }
}
