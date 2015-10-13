package com.bobsmirnov.championsanalytics.model.competitions;

import com.bobsmirnov.championsanalytics.db.DBHelper;

/**
 * Created by bobsmirnov on 31.07.15
 */

public class NationalLeague extends Competition {

    public NationalLeague(String viewName) {
        super(viewName, DBHelper.CLUB_NATIONAL_LEAGUES_COUNT, 2);
    }
}
