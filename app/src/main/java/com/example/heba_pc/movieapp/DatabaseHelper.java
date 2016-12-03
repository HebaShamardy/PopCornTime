package com.example.heba_pc.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HEBA-PC on 12/3/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favourite.db";
    private static final String TABLE_NAME = "favourite_table";
    private static final String MovieID_COL = "id";
    private static final String MovieName_COL = "name";
    private static final String MovieImage_COL = "image";
    private static final String MovieDate_COL = "date";
    private static final String MovieRate_COL = "rate";
    private static final String MoviePlot_COL = "plot";
    private static final String MovieTrailersJson_COL = "trailer";
    private static final String MovieReviewsJson_COL = "review";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME + "(" + MovieID_COL+" INTEGER PRIMARY KEY AUTOINCREMENT ," +MovieName_COL+ " TEXT UNIQUE,"
                 + MovieImage_COL + " TEXT, "+ MovieDate_COL + " TEXT," + MovieRate_COL + " TEXT," + MoviePlot_COL+" TEXT,"
                + MovieTrailersJson_COL+ " TEXT," + MovieReviewsJson_COL+" TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertMovie(String name , String image,  String date , String rate , String plot, String trailer, String review){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(MovieName_COL , name);
        content.put(MovieImage_COL , image);
        content.put(MovieDate_COL , date);
        content.put(MovieRate_COL , rate);
        content.put(MoviePlot_COL , plot);
        content.put(MovieTrailersJson_COL , trailer);
        content.put(MovieReviewsJson_COL , review);
        long result = db.insert(TABLE_NAME ,null , content );
        if(result==-1)
            return false;
        else
            return true;

    }

    public Cursor getAllFavourites()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME , null);
        return result;
    }

    public Integer deleteMovie(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME , "name = ?" , new String[] {name});

    }
}
