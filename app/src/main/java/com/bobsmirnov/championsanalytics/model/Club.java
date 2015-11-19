package com.bobsmirnov.championsanalytics.model;

import android.content.Context;
import android.database.Cursor;

import com.bobsmirnov.championsanalytics.db.DBHelper;
import com.bobsmirnov.championsanalytics.db.DBWorker;
import com.bobsmirnov.championsanalytics.model.competitions.ChampionsLeague;
import com.bobsmirnov.championsanalytics.model.competitions.Competition;
import com.bobsmirnov.championsanalytics.model.competitions.NationalLeague;
import com.bobsmirnov.championsanalytics.model.competitions.SuperCopa;
import com.bobsmirnov.championsanalytics.model.competitions.UEFACup;
import com.bobsmirnov.championsanalytics.view.ClubColors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by bobsmirnov on 29.07.15.
 */
public class Club {
    long id;
    private HashMap<Competition, Integer> trophies;
    private String name;
    private String nation;
    private ArrayList<String> legends;
    private int[] colors;

    public Club(long id, Context context) {
        final DBWorker db = new DBWorker(context);
        try {
            db.open();
        } catch (IOException e) {
        }
        Cursor cursor = db.getClubById(id);
        this.trophies = new HashMap<>(4);
        if (cursor.moveToFirst()) {
            do {
                this.id = id;
                this.name = cursor.getString(cursor.getColumnIndex(DBHelper.CLUB_NAME));
                this.nation = cursor.getString(cursor.getColumnIndex(DBHelper.CLUB_NATION));
                this.colors = extractColors(cursor.getString(cursor.getColumnIndex(DBHelper.CLUB_COLORS)));

                trophies.put(new NationalLeague(db.getNationalCupName(nation)),
                        cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_NATIONAL_LEAGUES_COUNT)));
                trophies.put(new ChampionsLeague(), cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_CHAMPIONS_LEAGUE_CUPS_COUNT)));
                trophies.put(new UEFACup(), cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_UEFA_CUPS_COUNT)));
                trophies.put(new SuperCopa(), cursor.getInt(cursor.getColumnIndex(DBHelper.CLUB_SUPERCOPA_CUPS_COUNT)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        this.legends = new ArrayList<>();
        cursor = db.getLegendsForClub(id);
        if (cursor.moveToFirst()) {
            do {
                this.legends.add(cursor.getString(cursor.getColumnIndex(DBHelper.LEGEND_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public static String getLogoName(String s) {
        return s.toLowerCase().replace(" ", "_").replace("-", "_");
    }

    private int[] extractColors(String array) {
        if (array.equals("")) return new int[0];
        final String[] split = array.split(", ");
        ArrayList<Integer> list = new ArrayList<>(split.length);
        for (String col : split)
            if (Arrays.asList(ClubColors.colors).contains(col)) list.add(ClubColors.getColor(col));
        final int[] colors = new int[list.size()];
        for (int i = 0; i < list.size(); i++) colors[i] = list.get(i);
        return colors;
    }

    public HashMap<Competition, Integer> getTrophies() {
        return trophies;
    }

    public String getName() {
        if (name == null) System.err.println(id);
        return name;
    }

    public ArrayList<String> getLegends() {
        return legends;
    }

    public int[] getColors() {
        return colors;
    }

    public String getLogoName() {
        return name.toLowerCase().replace(" ", "_").replace("-", "_");
    }
}
