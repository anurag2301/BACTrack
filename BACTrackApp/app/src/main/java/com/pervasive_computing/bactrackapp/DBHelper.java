package com.pervasive_computing.bactrackapp;

/*
  Created by Pratik on 11/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BACTrack.db";
    private static final String CONTACTS_TABLE_NAME = "contacts";
    private static final String CONTACTS_COLUMN_NAME = "name";
    private static final String CONTACTS_COLUMN_PHONE = "phone";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "(" +
                        CONTACTS_COLUMN_NAME + " text," +
                        CONTACTS_COLUMN_PHONE + " text primary key" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    void insertContact(String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        db.insertWithOnConflict(CONTACTS_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getName(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_NAME + " from " + CONTACTS_TABLE_NAME + " where " + CONTACTS_COLUMN_PHONE + "=" + phone + "", null);
        String name = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME));
        res.close();
        return name;
    }

    public int numberOfContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
    }

    public boolean updateContact(String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        db.update(CONTACTS_TABLE_NAME, contentValues, CONTACTS_COLUMN_PHONE + " = ? ", new String[]{phone});
        return true;
    }

    Integer deleteContact(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delVal = db.delete(CONTACTS_TABLE_NAME,
                CONTACTS_COLUMN_PHONE + " = ? ",
                new String[]{phone});
        db.close();
        return delVal;

    }

    HashMap<String, String> getAllContacts() {
        HashMap<String, String> map = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            map.put(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE)), res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return map;
    }

}