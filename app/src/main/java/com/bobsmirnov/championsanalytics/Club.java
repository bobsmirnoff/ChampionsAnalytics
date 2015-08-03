package com.bobsmirnov.championsanalytics;

import android.content.Context;
import android.database.Cursor;

import com.bobsmirnov.championsanalytics.competitions.ChampionsLeague;
import com.bobsmirnov.championsanalytics.competitions.Competition;
import com.bobsmirnov.championsanalytics.competitions.NationalLeague;
import com.bobsmirnov.championsanalytics.competitions.SuperCopa;
import com.bobsmirnov.championsanalytics.competitions.UEFACup;
import com.bobsmirnov.championsanalytics.db.DBHelper;
import com.bobsmirnov.championsanalytics.db.DBWorker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bobsmirnov on 29.07.15.
 */
public class Club {
    private HashMap<Competition, Integer> trophies;
    long id;
    private String name;
    private String emblemPath;
    private String nation;
    private ArrayList<String> legends;

    public Club(long id, Context context) {
        final DBWorker db = new DBWorker(context);
        db.open();
        Cursor cursor = db.getClubById(id);

        trophies = new HashMap<>(4);
        if (cursor.moveToFirst()) {
            do {
                this.id = id;
                this.nation = cursor.getString(cursor.getColumnIndex(DBHelper.CLUB_NATION));
                this.name = cursor.getString(cursor.getColumnIndex(DBHelper.CLUB_NAME));
                this.emblemPath = cursor.getString(cursor.getColumnIndex(DBHelper.CLUB_EMBLEM_PATH));

                trophies.put(new NationalLeague(db.getNationalCupName(nation)),
                        cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_NATIONAL_LEAGUES_COUNT)));
                trophies.put(new ChampionsLeague(), cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_CHAMPIONS_LEAGUE_CUPS_COUNT)));
                trophies.put(new UEFACup(), cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_UEFA_CUPS_COUNT)));
                trophies.put(new SuperCopa(), cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_SUPERCOPA_CUPS_COUNT)));
            } while (cursor.moveToNext());
        }
        cursor.close();

//        db.addLegend(id, "Luis Figo");
//        this.legends = new ArrayList<>();
//        cursor = db.getLegendsForClub(id);
//        if (cursor.moveToFirst()) {
//            do {
//                this.legends.add(cursor.getString(cursor.getColumnIndex(DBHelper.LEGEND_NAME)));
//            } while (cursor.moveToNext());
//        }
        cursor.close();
    }

    public HashMap<Competition, Integer> getTrophies() {
        return trophies;
    }

    public String getName() {
        return name;
    }

    public String getEmblemPath() {
        return emblemPath;
    }

    public String getNation() {
        return nation;
    }

    public ArrayList<String> getLegends() {
        return legends;
    }
}
