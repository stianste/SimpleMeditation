package com.stian.simplemeditation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{
    public final static String DATABASE_NAME = "sessions.db";
    public final static String TABLE_NAME = "sessions_table";
    public final static String COL_1 = "ID";
    public final static String COL_1_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT";
    public final static String COL_2 = "Minutes";
    public final static String COL_2_TYPE = " INTEGER";
    public final static String COL_3 =  "Seconds";
    public final static String COL_3_TYPE = " INTEGER";
    public final static String COL_4 = "Date";
    public final static String COL_4_TYPE = " DATE";
    public final static String COL_5 = "Rating";
    public final static String COL_5_TYPE = " INTEGER";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "( " + COL_1 + COL_1_TYPE + ", " + COL_2 + COL_2_TYPE + ", " + COL_3 + COL_3_TYPE + ", "
                + COL_4 + COL_4_TYPE + ", " + COL_5 + COL_5_TYPE + ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(int minutes, int seconds){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, minutes);
        contentValues.put(COL_3, seconds);
        long res = db.insert(TABLE_NAME, null, contentValues);
        return res == -1 ? false : true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getTotalMinutes(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(" + COL_2 + ") FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }
    public Cursor getTotalSeconds(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(" + COL_3 + ") FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }
}
