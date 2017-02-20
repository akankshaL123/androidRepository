package com.codepath.akanksha.postitapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appDB1";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_POSTS = "postApp";

    private static final String KEY_POSTS_ID = "ID";
    private static final String KEY_POSTS_VALUE = "values";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Database connection is being configured.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //Only called on first time table creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("create" , "database");
        String CREATE_TABLE_POSTS = "CREATE TABLE " + TABLE_POSTS + "(" + KEY_POSTS_ID + "INTEGER PRIMARY KEY," + KEY_POSTS_VALUE+ ")";
        db.execSQL(CREATE_TABLE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void onUpgrade(SQLiteDatabase db, String oldValue, String newValue) {
        String QUERY = String.format("SELECT * FROM %s WHERE %s=%s", TABLE_POSTS, KEY_POSTS_VALUE, oldValue);
        int existingValueId = -1;
        if(oldValue != newValue){
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(QUERY , null);
            if(0 != cursor.getCount()){
                cursor.moveToFirst();
                existingValueId = cursor.getColumnIndex(oldValue);
                Log.d("column", String.valueOf(existingValueId));
                //existingValueId = cursor.getString(cursor.getColumnIndex(KEY_POSTS_ID));
            }
            cursor.close();
            if(existingValueId != -1) {
                db = getWritableDatabase();

                ContentValues newValues = new ContentValues();
                newValues.put(KEY_POSTS_VALUE, newValue);

                db.update(TABLE_POSTS, newValues, KEY_POSTS_ID+"=", new String[]{ String.valueOf(existingValueId)});
            }
        }
    }


    public ArrayList<String> readExistingValues(){
        ArrayList <String> existingValues = new ArrayList<>();
        String QUERY = String.format("SELECT * FROM %s", TABLE_POSTS);
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(" before cursor ", null);
        Cursor cursor = db.rawQuery(QUERY , null);
        Log.d(" after cursor ", cursor.toString());
        if(null != cursor) {
            if(cursor.moveToFirst()) {
                do {
                    String newValueId = cursor.getString(cursor.getColumnIndex(KEY_POSTS_ID));
                    Log.d("index ", String.valueOf(newValueId));
                    String newValue = cursor.getString(cursor.getColumnIndex(KEY_POSTS_VALUE));
                    existingValues.add(newValue);
                } while (cursor.moveToNext());
            }
        }
        Log.d(" returning values ", existingValues.toString());
        return existingValues;
    }

    public boolean updateValues(String oldVal, String newVal){
        SQLiteDatabase db = this.getReadableDatabase();
        onUpgrade(db, oldVal, newVal);
        return true;
    }

    public void addValues(String newValue){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_POSTS_VALUE, newValue);

        db.insert(TABLE_POSTS, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteValues(String deletedValue){

        String QUERY = String.format("DELETE FROM %s WHERE %s=%s", TABLE_POSTS, KEY_POSTS_VALUE, deletedValue);
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        Log.d("delete " , QUERY);
        db.delete(TABLE_POSTS, KEY_POSTS_VALUE, new String[]{String.valueOf(deletedValue)});
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}