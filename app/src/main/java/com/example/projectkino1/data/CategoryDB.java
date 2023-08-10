package com.example.projectkino1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projectkino1.CategoryClass;
import com.example.projectkino1.ClassFilms;

import java.util.ArrayList;

public class CategoryDB {
    private static final String DATABASE_NAME = "viewed.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Categories";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FILMNAME = "FilmName";

    private static final int NUM_COLUMN_ID = 0;

    private static final int NUM_COLUMN_FILMNAME = 1;

    private SQLiteDatabase vDataBase;

    public CategoryDB(Context context) {
        CategoryDB.OpenHelper mOpenHelper = new CategoryDB.OpenHelper(context);
        vDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String name) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FILMNAME, name);
        return vDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(CategoryClass categoryClass) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FILMNAME, categoryClass.getName());
        return vDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(categoryClass.getId())});
    }

    public void deleteAll() {
        vDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        vDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public CategoryClass select(long id) {
        Cursor mCursor = vDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String FilmName = mCursor.getString(NUM_COLUMN_FILMNAME);
        return new CategoryClass(id, FilmName);
    }

    public ArrayList<String> selectAll() {
        Cursor mCursor =  vDataBase.rawQuery("SELECT * FROM `" + TABLE_NAME
                + "` WHERE `id` > '0' ORDER BY `FilmName` ASC, `id` DESC", null);

        ArrayList<String> arr = new ArrayList<String>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                String FilmName = mCursor.getString(NUM_COLUMN_FILMNAME);
                arr.add(FilmName);
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FILMNAME + " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
