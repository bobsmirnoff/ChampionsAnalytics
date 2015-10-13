package com.bobsmirnov.championsanalytics.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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
                    + CLUB_EMBLEM_PATH + " text"
                    + ");";
    // legends table
    public static final String LEGENDS_TABLE = "legends_table";
    public static final String LEGEND_ID = "_id";
    public static final String LEGEND_NAME = "name";
    public static final String LEGEND_CLUB_PLAYED = "club";
    private static final String INIT_LEGENDS =
            "create table "
                    + LEGENDS_TABLE + "("
                    + LEGEND_ID + " integer primary key autoincrement not null, "
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
    private static final String DB_PATH = "/data/data/com.bobsmirnov.championsanalytics/databases/";
    private final Context context;
    private SQLiteDatabase db;


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream input = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    @Override
    public synchronized void close() {
        if (db != null) db.close();
        super.close();
    }
}
