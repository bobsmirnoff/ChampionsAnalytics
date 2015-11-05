package com.bobsmirnov.championsanalytics.model;

import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.util.HashMap;

/**
 * Created by bobsmirnov on 13.10.15.
 */
public class ScoreState {
    private HashMap<ScoreBoardPosition, Club> state;

    public ScoreState() {
        this.state = new HashMap<>();
    }

    public boolean namesAreSame() {
        return state.get(ScoreBoardPosition.LEFT).getName()
                .equals(state.get(ScoreBoardPosition.RIGHT).getName());
    }

    public void put(ScoreBoardPosition position, Club club) {
        state.put(position, club);
    }

    public Club get(ScoreBoardPosition position) {
        return state.get(position);
    }
}
