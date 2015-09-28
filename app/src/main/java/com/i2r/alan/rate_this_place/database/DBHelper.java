package com.i2r.alan.rate_this_place.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.FeedEntry.TABLE_NAME + " (" +
                    DBContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.FeedEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    DBContract.FeedEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    DBContract.FeedEntry.COLUMN_LOCATION_NAME + TEXT_TYPE + COMMA_SEP +
                    DBContract.FeedEntry.COLUMN_LOCATION_LATITUDE + REAL_TYPE + COMMA_SEP +
                    DBContract.FeedEntry.COLUMN_LOCATION_LONGITUDE + REAL_TYPE  + COMMA_SEP +
                    DBContract.FeedEntry.COLUMN_RATING_STATUS + TEXT_TYPE +" )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.FeedEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }



}