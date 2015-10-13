package com.bobsmirnov.championsanalytics.model.competitions;

import com.bobsmirnov.championsanalytics.db.DBHelper;

/**
 * Created by bobsmirnov on 31.07.15.
 */
public class UEFACup extends Competition {

    public UEFACup() {
        super("UEFA Cup", DBHelper.CLUB_UEFA_CUPS_COUNT, 3);
    }
}
