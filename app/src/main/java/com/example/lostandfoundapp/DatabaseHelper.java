package com.example.lostandfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "lost_found.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "adverts";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "post_type TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "location TEXT NOT NULL, " +
                "image_path TEXT NOT NULL, " +
                "created_at TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertAdvert(String postType, String name, String phone, String description,
                             String category, String location, String imagePath, String createdAt) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("post_type", postType);
        values.put("name", name);
        values.put("phone", phone);
        values.put("description", description);
        values.put("category", category);
        values.put("location", location);
        values.put("image_path", imagePath);
        values.put("created_at", createdAt);
        return db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<LostFoundItem> getAllAdverts() {
        return getAdvertsByCategory("All Categories");
    }

    public ArrayList<LostFoundItem> getAdvertsByCategory(String category) {
        ArrayList<LostFoundItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if (category == null || category.equals("All Categories")) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE category = ? ORDER BY id DESC",
                    new String[]{category});
        }

        if (cursor.moveToFirst()) {
            do {
                items.add(new LostFoundItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("post_type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image_path")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public LostFoundItem getAdvertById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(id)});
        LostFoundItem item = null;
        if (cursor.moveToFirst()) {
            item = new LostFoundItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("post_type")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("location")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_path")),
                    cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
            );
        }
        cursor.close();
        return item;
    }

    public void deleteAdvert(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }
}
