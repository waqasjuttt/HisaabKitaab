package com.example.waqas.hisaabkitaab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "milkdb.db";
    private Context ctx;
    int Grand_Total = 0;
    List<String> stringArray = new ArrayList<>();
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "milk_table";
    public static final String KEY_ID = "id";
    public static final String KEY_MILK_KG = "milk_kg";
    public static final String KEY_TOTAL_PRICE = "total_price";
    public static final String KEY_DATE_N_TIME = "date_n_time";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MILK_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MILK_KG + " TEXT,"
                + KEY_TOTAL_PRICE + " INTEGER," + KEY_DATE_N_TIME + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_MILK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add New Data in Database
    public void add_Milk(Milk_Items milk_items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MILK_KG, milk_items.getMilk_Quantity());
        values.put(KEY_TOTAL_PRICE, milk_items.getTotal_Milk());
        values.put(KEY_DATE_N_TIME, milk_items.getDateNTime());

        // inserting this record
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Milk Data in Table of Database
    public List<Milk_Items> getAllData() {
        List<Milk_Items> milk_items = new ArrayList<>();
        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                Milk_Items milk_items1 = new Milk_Items(null, null, 0);
                milk_items1.setID(Integer.parseInt(cursor.getString(0)));
                milk_items1.setMilk_Quantity(cursor.getString(1));
                milk_items1.setTotal_Milk(cursor.getInt(2));
                milk_items1.setDateNTime(cursor.getString(3));

                Grand_Total = Integer.parseInt(cursor.getString(2)) + Grand_Total;
                // Adding milk_items1 to list
                milk_items.add(milk_items1);
            } while (cursor.moveToNext());
        }
//        Toast.makeText(ctx, "Total Price: " + String.valueOf(Grand_Total), Toast.LENGTH_SHORT).show();

        return milk_items;
    }

    public int getSum() {
        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Grand_Total = Integer.parseInt(cursor.getString(2)) + Grand_Total;
            }
        }
        Toast.makeText(ctx, "Total Price: " + String.valueOf(Grand_Total), Toast.LENGTH_LONG).show();
        return Grand_Total;
    }

    // Deleting a record in database table
    public void deleteMilkData(Milk_Items milk_items) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(milk_items.getID())});
        db.close();
    }

    // Updating a record in database table
    public int updateMilkData(Milk_Items milk_items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MILK_KG, milk_items.getMilk_Quantity());
        values.put(KEY_TOTAL_PRICE, milk_items.getTotal_Milk());
        values.put(KEY_DATE_N_TIME, milk_items.getDateNTime());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(milk_items.getID())});
    }

    // getting number of records in table
    public int getContactsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataCount = db.rawQuery("select " + KEY_ID + " from " + TABLE_NAME, null);

        int count = dataCount.getCount();
        dataCount.close();
        db.close();

        return count;
    }
}