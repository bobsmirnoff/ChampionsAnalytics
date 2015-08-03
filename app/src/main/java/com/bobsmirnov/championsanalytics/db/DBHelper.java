package com.bobsmirnov.championsanalytics.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "football_analytics";
    public static final int DB_VERSION = 1;

    // clubs table
    public static final String CLUBS_TABLE = "clubs_table";

    public static final String CLUB_ID = "_id";
    public static final String CLUB_NAME = "name";
    public static final String CLUB_NATION = "nation";
    public static final String CLUB_EMBLEM_PATH = "image_path";

    public static final String CLUB_NATIONAL_LEAGUES_COUNT = "national_count";
    public static final String CLUB_CHAMPIONS_LEAGUE_CUPS_COUNT = "cl_count";
    public static final String CLUB_UEFA_CUPS_COUNT = "uefa_count";
    public static final String CLUB_SUPERCOPA_CUPS_COUNT = "supercopa_count";

    private static final String INIT_CLUBS =
            "create table "
                    + CLUBS_TABLE + "("
                    + CLUB_ID + " integer primary key autoincrement not null, "
                    + CLUB_NAME + " text, "
                    + CLUB_NATION + " text, "
                    + CLUB_NATIONAL_LEAGUES_COUNT + " integer, "
                    + CLUB_CHAMPIONS_LEAGUE_CUPS_COUNT + " integer, "
                    + CLUB_UEFA_CUPS_COUNT + " integer, "
                    + CLUB_SUPERCOPA_CUPS_COUNT + " integer, "
                    + CLUB_EMBLEM_PATH + " text" + ");";

    // legends table
    public static final String LEGENDS_TABLE = "legends_table";
    public static final String LEGEND_ID = "_id";
    public static final String LEGEND_NAME = "name";
    public static final String LEGEND_CLUB_PLAYED = "club";

    private static final String INIT_LEGENDS =
            "create table "
                    + LEGENDS_TABLE + "("
                    + LEGEND_ID + " integer z autoincrement not null, "
                    + LEGEND_NAME + " text, "
                    + LEGEND_CLUB_PLAYED + " integer" + ");";

    // trophies table
    public static final String TROPHIES_TABLE = "trophies_table";
    public static final String NATION = "nation";
    public static final String NATIONAL_CUP_NAME = "national_cup";

    private static final String INIT_TROPHIES =
            "create table "
                    + TROPHIES_TABLE + "("
                    + NATION + " text, "
                    + NATIONAL_CUP_NAME + " text" + ");";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_CLUBS);
        db.execSQL(INIT_LEGENDS);
        db.execSQL(INIT_TROPHIES);

        ContentValues cv = new ContentValues();
        cv.put(LEGEND_NAME, "Jon Snow");
        cv.put(LEGEND_CLUB_PLAYED, "2");
        db.insert(LEGENDS_TABLE, null, cv);

        cv = new ContentValues();
        cv.put(LEGEND_NAME, "Luis Figo");
        cv.put(LEGEND_CLUB_PLAYED, "1");
        db.insert(LEGENDS_TABLE, null, cv);

        //TODO: fill trophies names for nations
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
