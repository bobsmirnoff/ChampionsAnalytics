package com.bobsmirnov.championsanalytics.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBWorker {

    private final Context context;

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBWorker(Context cxt) {
        context = cxt;
    }

    public void open() {
        helper = new DBHelper(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        db = helper.getWritableDatabase();
    }

    public void close() {
        if (helper != null) helper.close();
    }

    public Cursor getAllClubs() {
        return db.query(DBHelper.CLUBS_TABLE, null, null, null, null, null, DBHelper.CLUB_NAME + " ASC");
    }

    public Cursor getClubById(long clubId) {
        return db.query(DBHelper.CLUBS_TABLE,
                null,
                DBHelper.CLUB_ID + " = " + clubId,
                null, null, null, null);
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
                DBHelper.LEGEND_CLUB_PLAYED + " = " + clubId,
                null, null, null, null);
    }

    public Cursor getAllLegends() {
        return db.query(DBHelper.LEGENDS_TABLE, null, null, null, DBHelper.LEGEND_NAME, null, DBHelper.LEGEND_NAME + " ASC");
    }

    public void addLegend(long clubId, String legendName) {
        //somehow db.insert does not work
        db.execSQL("INSERT INTO " + DBHelper.LEGENDS_TABLE + " ("
                + DBHelper.LEGEND_CLUB_PLAYED + ", "
                + DBHelper.LEGEND_NAME + ") " +
                "VALUES ('" + clubId + "', '" + legendName + ")");
    }

    public String getNationalCupName(String nation) {
        String name = "";
        final Cursor cursor = db.query(DBHelper.TROPHIES_TABLE,
                new String[]{DBHelper.NATIONAL_CUP_NAME},
                DBHelper.NATION + " = " + nation,
                null, null, null, null);
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