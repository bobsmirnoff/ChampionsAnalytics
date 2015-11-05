package com.bobsmirnov.championsanalytics.model;

import android.util.Pair;

import com.bobsmirnov.championsanalytics.model.competitions.Competition;
import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.util.HashMap;

/**
 * Created by bobsmirnov on 13.10.15.
 */
public class ScoreState {
    private HashMap<ScoreBoardPosition, Pair<String, Integer>> state;

    public ScoreState() {
        this.state = new HashMap<>();
        state.put(ScoreBoardPosition.LEFT, new Pair<>("", 0));
        state.put(ScoreBoardPosition.RIGHT, new Pair<>("", 0));
    }

    public boolean namesAreSame() {
        return state.get(ScoreBoardPosition.LEFT).first
                .equals(state.get(ScoreBoardPosition.RIGHT).first);
    }

    public void put(ScoreBoardPosition position, Club club) {
        int score = club.getLegends().size();
        for (Competition c : club.getTrophies().keySet()) {
            if (club.getTrophies().get(c) > 0)
                score += club.getTrophies().get(c);
        }
        state.put(position, new Pair<>(club.getName(), score));
    }

    public Integer get(ScoreBoardPosition position) {
        return state.get(position).second;
    }
}
