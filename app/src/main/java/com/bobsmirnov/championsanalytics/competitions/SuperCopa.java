package com.bobsmirnov.championsanalytics.competitions;

import com.bobsmirnov.championsanalytics.db.DBHelper;

/**
 * Created by bobsmirnov on 31.07.15.
 */
public class SuperCopa extends Competition {

    public SuperCopa() {
        super("UEFA Super Cup", DBHelper.CLUB_SUPERCOPA_CUPS_COUNT, 4);
    }
}
