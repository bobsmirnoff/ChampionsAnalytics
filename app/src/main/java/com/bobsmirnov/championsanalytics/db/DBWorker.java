package com.bobsmirnov.championsanalytics.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.TreeMap;

public class DBWorker {

    private final Context context;

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBWorker(Context cxt) {
        context = cxt;
    }

    public void open() throws IOException {
        helper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        helper.createDataBase();
        helper.openDataBase();
        db = helper.getWritableDatabase();
    }

    public void close() {
        if (helper != null) helper.close();
    }


    //---------------------- clubs table methods -----------------------------------

    public TreeMap<String, Long> getAllClubsNames() {
        final Cursor cursor = db.query(DBHelper.CLUBS_TABLE,
                new String[]{DBHelper.CLUB_NAME, DBHelper.CLUB_ID},
                null, null, null, null,
                DBHelper.CLUB_NAME + " ASC");
        final TreeMap<String, Long> map = new TreeMap<>();
        if (cursor == null) return null;
        if (cursor.moveToFirst()) {
            do {
                map.put(cursor.getString(cursor.getColumnIndex(DBHelper.CLUB_NAME)),
                        cursor.getLong(cursor.getColumnIndex(DBHelper.CLUB_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return map;
    }

    public Cursor getClubById(long clubId) {
        return db.query(DBHelper.CLUBS_TABLE,
                null,
                DBHelper.CLUB_ID + " = ?",
                new String[]{"" + clubId},
                null, null, null);
    }

    public int getNationalCups(long clubId) {
        return getCups(clubId, DBHelper.CLUB_NATIONAL_LEAGUES_COUNT);
    }

    public int getChampionsLeagueCups(long clubId) {
        return getCups(clubId, DBHelper.CLUB_CHAMPIONS_LEAGUE_CUPS_COUNT);
    }

    public int getUEFACups(long clubId) {
        return getCups(clubId, DBHelper.CLUB_UEFA_CUPS_COUNT);
    }

    public int getSupercopaCups(long clubId) {
        return getCups(clubId, DBHelper.CLUB_SUPERCOPA_CUPS_COUNT);
    }

    private int getCups(long clubId, String competitionColumn) {
        int count = -1;
        final Cursor cursor = db.query(DBHelper.CLUBS_TABLE,
                new String[]{competitionColumn},
                DBHelper.CLUB_ID + " = " + clubId,
                null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex(competitionColumn));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return count;
    }

    //---------------------- legends table methods -----------------------------------

    public Cursor getLegendsForClub(long clubId) {
        return db.query(DBHelper.LEGENDS_TABLE,
                null,
                DBHelper.LEGEND_CLUB_PLAYED + " = ?",
                new String[]{"" + clubId},
                null, null, null);
    }

    public Cursor getAllLegends() {
        return db.query(DBHelper.LEGENDS_TABLE, null, null, null, DBHelper.LEGEND_NAME, null, DBHelper.LEGEND_NAME + " ASC");
    }

    public void addLegend(long clubId, String legendName) {
        db.execSQL("INSERT INTO " + DBHelper.LEGENDS_TABLE + " ("
                + DBHelper.LEGEND_CLUB_PLAYED + ", "
                + DBHelper.LEGEND_NAME + ") " +
                "VALUES ('" + clubId + "', '" + legendName + "')");
    }

    public String getNationalCupName(String nation) {
        String name = "";
        final Cursor cursor = db.query(
                DBHelper.TROPHIES_TABLE,
                new String[]{DBHelper.NATIONAL_CUP_NAME},
                DBHelper.NATION + " = ?",
                new String[]{nation},
                null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    name = cursor.getString(cursor.getColumnIndex(DBHelper.NATIONAL_CUP_NAME));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return name;
    }
}