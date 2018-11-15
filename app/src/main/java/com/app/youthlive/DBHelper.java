package com.app.youthlive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "yl_db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(gift.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + gift.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }


    public long insertGift(String gid , String name , String file , String price , byte[] fl) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(gift.COLUMN_GID, gid);
        values.put(gift.COLUMN_NAME, name);
        values.put(gift.COLUMN_FILE, file);
        values.put(gift.COLUMN_PRICE, price);
        values.put(gift.COLUMN_FL, fl);

        // insert row
        long id = db.insert(gift.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public List<gift> getAllGifts() {
        List<gift> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + gift.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                gift note = new gift();
                note.setId(cursor.getInt(cursor.getColumnIndex(gift.COLUMN_ID)));
                note.setGid(cursor.getString(cursor.getColumnIndex(gift.COLUMN_GID)));
                note.setName(cursor.getString(cursor.getColumnIndex(gift.COLUMN_NAME)));
                note.setFile(cursor.getString(cursor.getColumnIndex(gift.COLUMN_FILE)));
                note.setPrice(cursor.getString(cursor.getColumnIndex(gift.COLUMN_PRICE)));
                note.setFl(cursor.getBlob(cursor.getColumnIndex(gift.COLUMN_FL)));

                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // close db connection
        db.close();

        // return notes list
        return notes;
    }


}
