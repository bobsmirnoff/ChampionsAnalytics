package com.bobsmirnov.championsanalytics.model;

import com.bobsmirnov.championsanalytics.view.ScoreBoardPosition;

import java.util.HashMap;

/**
 * Created by bobsmirnov on 13.10.15.
 */
public class ScoreState {
    private HashMap<ScoreBoardPosition, Club> state;

    public ScoreState(Club left, Club right) {
        this.state = new HashMap<>();
        put(ScoreBoardPosition.LEFT, left);
        put(ScoreBoardPosition.RIGHT, right);
    }

    public ScoreState(HashMap<ScoreBoardPosition, Club> map) {
        this.state = map;
    }

    public Club getOtherPositionClub(ScoreBoardPosition pos) {
        return pos.equals(ScoreBoardPosition.LEFT) ? state.get(ScoreBoardPosition.RIGHT) : state.get(ScoreBoardPosition.LEFT);
    }

    public void put(ScoreBoardPosition position, Club club) {
        state.put(position, club);
    }

    public Club get(ScoreBoardPosition position) {
        return state.get(position);
    }

    public void swap() {
        final Club left = get(ScoreBoardPosition.LEFT);
        put(ScoreBoardPosition.LEFT, get(ScoreBoardPosition.RIGHT));
        put(ScoreBoardPosition.RIGHT, left);
    }
}
