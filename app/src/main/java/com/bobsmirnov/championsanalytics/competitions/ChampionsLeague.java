package com.bobsmirnov.championsanalytics.competitions;

import com.bobsmirnov.championsanalytics.db.DBHelper;

/**
 * Created by bobsmirnov on 31.07.15.
 */
public class ChampionsLeague extends Competition {

    public ChampionsLeague() {
        super("Champions League", DBHelper.CLUB_CHAMPIONS_LEAGUE_CUPS_COUNT, 1);
    }
}
