package com.emrehmrc.argememory.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {


    static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Bildirimler";//database adı

    private static final String TABLE_NAME = "Görev";
    private static String FIRST_COLUMN = "GörevSayısı";
    private static String SECOND_COLUMN = "MemberID";
    private static String ID = "id";

    public  DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY ,"
                + FIRST_COLUMN + " TEXT, "
                + SECOND_COLUMN + " TEXT " + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (db.getVersion() > 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void Insert(String s,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIRST_COLUMN, s);
        values.put(SECOND_COLUMN, id);
        values.put(ID,1);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void DeleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public List<String> ListAll() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sqlQuery, null);
        while (cursor.moveToNext()) {
            String s = new String();
            cursor.getInt(0);
            s = cursor.getString(1);
            list.add(s);
        }

        return list;
    }

    public String SelectById(int id) {

        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.
        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //map.put("x","300"); mesala burda anahtar x değeri 300.

        String data = "";
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            data = cursor.getString(1);
        }
        cursor.close();
        db.close();
        return data;


    }
    public String SelectByIdMemberId(int id) {

        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.
        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //map.put("x","300"); mesala burda anahtar x değeri 300.

        String data = "";
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            data = cursor.getString(2);
        }
        cursor.close();
        db.close();
        return data;


    }

    public void DeleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();

    }

    public int GetRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }

    public void Update(int id, String s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIRST_COLUMN, s);

        // updating row
        db.update(TABLE_NAME, values, ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public void UpdateMemberId(int id, String memberid) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SECOND_COLUMN, memberid);

        // updating row
        db.update(TABLE_NAME, values, ID + " = ?",
                new String[]{String.valueOf(id)});

    }
}
