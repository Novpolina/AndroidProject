package com.example.projectkino1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projectkino1.ClassFilms;

import java.util.ArrayList;

public class DBReadBooks {
    private static final String DATABASE_NAME = "read_books.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "RBooks";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FILMNAME = "FilmName";
    private static final String COLUMN_COMMENT = "Comment";
    private static final String COLUMN_AUTHOR = "Author";
    private static final String COLUMN_CATEGORY = "Category";
    private static final String COLUMN_REVIEW = "Review";
    private static final String COLUMN_RANG = "Rang";

    private static final int NUM_COLUMN_ID = 0;

    private static final int NUM_COLUMN_FILMNAME = 1;
    private static final int NUM_COLUMN_COMMENT = 2;
    private static final int NUM_COLUMN_AUTHOR = 3;
    private static final int NUM_COLUMN_CATEGORY = 4;
    private static final int NUM_COLUMN_REVIEW = 5;
    private static final int NUM_COLUMN_RANG = 6;

    public String TYPE_CATEGORY = "";
    public String TYPE_NEW = "";

    private SQLiteDatabase mDataBase;

    public DBReadBooks(Context context) {
        DBReadBooks.OpenHelper mOpenHelper = new DBReadBooks.OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String name, String author, String comment, String category, int review, int rang) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FILMNAME, name);
        cv.put(COLUMN_COMMENT, comment);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_REVIEW, review);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_RANG, rang);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(ClassFilms vf) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FILMNAME, vf.getName());
        cv.put(COLUMN_COMMENT, vf.getComment());
        cv.put(COLUMN_AUTHOR, vf.getAuthor());
        cv.put(COLUMN_REVIEW, vf.getReview());
        cv.put(COLUMN_CATEGORY, vf.getCategory());
        cv.put(COLUMN_RANG, vf.getRang());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(vf.getId())});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public ClassFilms select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String FilmName = mCursor.getString(NUM_COLUMN_FILMNAME);
        String Comment = mCursor.getString(NUM_COLUMN_COMMENT);
        String Auth = mCursor.getString(NUM_COLUMN_AUTHOR);
        String Category = mCursor.getString(NUM_COLUMN_CATEGORY);
        int review = mCursor.getInt(NUM_COLUMN_REVIEW);
        int rang = mCursor.getInt(NUM_COLUMN_RANG);
        return new ClassFilms(id, FilmName, Comment, Auth, Category, review, rang);
    }
    public ArrayList<String> selectCategories(){
        Cursor userCursor =  mDataBase.rawQuery("SELECT * FROM `" + TABLE_NAME
                + "` WHERE `id` > '0' AND `Rang` = '0' ORDER BY `Category`", null);

        ArrayList<String> arr = new ArrayList<String>();
        userCursor.moveToFirst();
        if (!userCursor.isAfterLast()) {
            do {
                String Category = userCursor.getString(NUM_COLUMN_CATEGORY);
                arr.add(Category);
            } while (userCursor.moveToNext());
        }
        return arr;

    }



    public ArrayList<ClassFilms> selectAll() {
        Cursor userCursor =  mDataBase.rawQuery("SELECT * FROM `" + TABLE_NAME
                + "` WHERE `id` > '0' ORDER BY `Category`, `Rang`", null);

        ArrayList<ClassFilms> arr = new ArrayList<ClassFilms>();
        userCursor.moveToFirst();
        if (!userCursor.isAfterLast()) {
            do {
                long id = userCursor.getLong(NUM_COLUMN_ID);
                String FilmName =userCursor.getString(NUM_COLUMN_FILMNAME);
                String Comment = userCursor.getString(NUM_COLUMN_COMMENT);
                String Auth = userCursor.getString(NUM_COLUMN_AUTHOR);
                String Category = userCursor.getString(NUM_COLUMN_CATEGORY);
                int review = userCursor.getInt(NUM_COLUMN_REVIEW);
                int rang = userCursor.getInt(NUM_COLUMN_RANG);
                arr.add( new ClassFilms(id, FilmName, Comment, Auth, Category, review, rang));
            } while (userCursor.moveToNext());
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
                    COLUMN_FILMNAME+ " TEXT, " +
                    COLUMN_COMMENT + " TEXT, " +
                    COLUMN_AUTHOR + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_REVIEW + " INTEGER, " +
                    COLUMN_RANG + " INTEGER);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
