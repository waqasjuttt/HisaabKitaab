package com.example.waqas.hisaabkitaab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "milkdb.db";
    private static Context ctx;
    int Grand_Total = 0;
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

    // Add New Data into the Database
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

    ////////////////////////////////////////// Check Date Already Exist or Not //////////////////////////////////////////////////////////////////
    public boolean checkDate(String date) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_DATE_N_TIME + " LIKE '" + date + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    ////////////////////////////////////////// Getting All Milk Data in Table of Database ///////////////////////////////////////////////////////
    public ArrayList<Milk_Items> getAllData() {
        ArrayList<Milk_Items> milk_items = new ArrayList<>();
        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + KEY_DATE_N_TIME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                Milk_Items milk_items1 = new Milk_Items(null, null, 0);
                milk_items1.setID(Integer.parseInt(cursor.getString(0)));
                milk_items1.setMilk_Quantity(cursor.getString(1));
                milk_items1.setTotal_Milk(cursor.getInt(2));
                if (cursor.getString(3).toString().startsWith("0")) {
                    String s = cursor.getString(3).toString().replaceFirst("0", "");
                    milk_items1.setDateNTime(s);
                } else {
                    milk_items1.setDateNTime(cursor.getString(3));
                }

                Grand_Total = Integer.parseInt(cursor.getString(2)) + Grand_Total;
                // Adding milk_items1 to list
                milk_items.add(milk_items1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return milk_items;
    }

    public String[] getDates() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + KEY_DATE_N_TIME + " from " + TABLE_NAME + " ;", null);
        ArrayList<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_N_TIME));
            list.add(date.toString().substring(2, 6));
        }

        Object[] st = list.toArray();
        for (Object s : st) {
            if (list.indexOf(s) != list.lastIndexOf(s)) {
                list.remove(list.lastIndexOf(s));
            }
        }

        String[] str = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().contains("-01-")) {
                str[i] = list.get(i).toString().replace("-01-", "1");
            } else if (list.get(i).toString().contains("-02-")) {
                str[i] = list.get(i).toString().replace("-02-", "2");
            } else if (list.get(i).toString().contains("-03-")) {
                str[i] = list.get(i).toString().replace("-03-", "3");
            } else if (list.get(i).toString().contains("-04-")) {
                str[i] = list.get(i).toString().replace("-04-", "4");
            } else if (list.get(i).toString().contains("-05-")) {
                str[i] = list.get(i).toString().replace("-05-", "5");
            } else if (list.get(i).toString().contains("-06-")) {
                str[i] = list.get(i).toString().replace("-06-", "6");
            } else if (list.get(i).toString().contains("-07-")) {
                str[i] = list.get(i).toString().replace("-07-", "7");
            } else if (list.get(i).toString().contains("-08-")) {
                str[i] = list.get(i).toString().replace("-08-", "8");
            } else if (list.get(i).toString().contains("-09-")) {
                str[i] = list.get(i).toString().replace("-09-", "9");
            } else if (list.get(i).toString().contains("-10-")) {
                str[i] = list.get(i).toString().replace("-10-", "10");
            } else if (list.get(i).toString().contains("-11-")) {
                str[i] = list.get(i).toString().replace("-11-", "11");
            } else if (list.get(i).toString().contains("-12-")) {
                str[i] = list.get(i).toString().replace("-12-", "12");
            }
        }

        int[] a = new int[str.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = Integer.parseInt(str[i]);
        }
        Arrays.sort(a);

        for (int i = 0; i < a.length; i++) {
            str[i] = String.valueOf(a[i]);
        }

//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).toString().contains("-01-")) {
//                str[i] = list.get(i).toString().replace("-01-", "Jan-");
//            } else if (list.get(i).toString().contains("-02-")) {
//                str[i] = list.get(i).toString().replace("-02-", "Feb-");
//            } else if (list.get(i).toString().contains("-03-")) {
//                str[i] = list.get(i).toString().replace("-03-", "Mar-");
//            } else if (list.get(i).toString().contains("-04-")) {
//                str[i] = list.get(i).toString().replace("-04-", "Apr-");
//            } else if (list.get(i).toString().contains("-05-")) {
//                str[i] = list.get(i).toString().replace("-05-", "May-");
//            } else if (list.get(i).toString().contains("-06-")) {
//                str[i] = list.get(i).toString().replace("-06-", "Jun-");
//            } else if (list.get(i).toString().contains("-07-")) {
//                str[i] = list.get(i).toString().replace("-07-", "Jul-");
//            } else if (list.get(i).toString().contains("-08-")) {
//                str[i] = list.get(i).toString().replace("-08-", "Aug-");
//            } else if (list.get(i).toString().contains("-09-")) {
//                str[i] = list.get(i).toString().replace("-09-", "Sep-");
//            } else if (list.get(i).toString().contains("-10-")) {
//                str[i] = list.get(i).toString().replace("-10-", "Oct-");
//            } else if (list.get(i).toString().contains("-11-")) {
//                str[i] = list.get(i).toString().replace("-11-", "Nov-");
//            } else if (list.get(i).toString().contains("-12-")) {
//                str[i] = list.get(i).toString().replace("-12-", "Dec-");
//            }
//        }

        return str;
    }

    ////////////////////////////////////////// Deleting a record in database table //////////////////////////////////////////////////////////////

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