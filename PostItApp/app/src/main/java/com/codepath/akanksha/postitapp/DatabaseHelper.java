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
    private static final String KEY_POSTS_VALUE = "names";

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
        String CREATE_TABLE_POSTS = "CREATE TABLE " + TABLE_POSTS + "(" + KEY_POSTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_POSTS_VALUE+ " TEXT)";
        Log.d("Create Query :", CREATE_TABLE_POSTS);
        db.execSQL(CREATE_TABLE_POSTS);
        Log.d("Success, Table created", TABLE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void onUpgrade(SQLiteDatabase db, String oldValue, String newValue) {
        String QUERY = String.format("SELECT * FROM "+ TABLE_POSTS + " WHERE " + KEY_POSTS_VALUE +" = '"+ oldValue +"'");
        Log.d("Existing value query:", QUERY);
        String existingValueId = null;
        if(oldValue != newValue){
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(QUERY , null);
            if(0 != cursor.getCount()){
                Log.d("Query results count:", String.valueOf(cursor.getCount()));
                cursor.moveToFirst();
                existingValueId = cursor.getString(cursor.getColumnIndex(KEY_POSTS_ID));
                Log.d("ID returned:", existingValueId);
            }
            cursor.close();
            if(existingValueId != null) {
                db = getWritableDatabase();

                Log.d("Value to update", newValue+" for ID : "+String.valueOf(existingValueId));
                ContentValues newValues = new ContentValues();
                newValues.put(KEY_POSTS_VALUE, newValue);

                db.update(TABLE_POSTS, newValues, KEY_POSTS_ID+" = ?", new String[]{ String.valueOf(existingValueId)});
                Log.d("Update successful", newValue);
            }
        }
    }


    public ArrayList<String> readExistingValues(){
        ArrayList <String> existingValues = new ArrayList<>();
        String QUERY = String.format("SELECT * FROM %s", TABLE_POSTS);
        Log.d("Read values query ", QUERY);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY , null);
        if(null != cursor) {
            if(cursor.moveToFirst()) {
                do {
                    //String newValueId = cursor.getString(cursor.getColumnIndex(KEY_POSTS_ID));
                    String newValue = cursor.getString(cursor.getColumnIndex(KEY_POSTS_VALUE));
                    Log.d("Existing value to add", newValue);
                    existingValues.add(newValue);
                } while (cursor.moveToNext());
            }
        }
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
        Log.d("Database: Add method", "Addition successful");
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteValues(String deletedValue){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = KEY_POSTS_VALUE+" = ?";

        db.beginTransaction();

        db.delete(TABLE_POSTS, whereClause, new String[]{deletedValue});
        Log.d("Database: Delete method", "Deletion successful");
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}