package com.example.grocerylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.grocerylist.Model.Grocery;
import com.example.grocerylist.Util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;
    public DatabaseHandler(Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY," + Util.KEY_GROCERY_NAME + "TEXT "
                + Util.KEY_GROCERY_QTY + "TEXT, " + Util.KEY_DATE_ADDED + "LONG);" ;

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);

        onCreate(db);
    }


    // Add grocery items
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_GROCERY_NAME, grocery.getName());
        values.put(Util.KEY_GROCERY_QTY, grocery.getQuantity());
        values.put(Util.KEY_DATE_ADDED, grocery.getDateItemAdded());

        db.insert(Util.TABLE_NAME, null, values);

        Log.d("Saved", "Item saved");
    }

    // Get one grocery item
    public Grocery getGrocery(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID, Util.KEY_GROCERY_NAME, Util.KEY_GROCERY_QTY, Util.KEY_DATE_ADDED},
                Util.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        Grocery grocery = new Grocery();
        grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
        grocery.setName(cursor.getString(cursor.getColumnIndex(Util.KEY_GROCERY_NAME)));
        grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Util.KEY_GROCERY_QTY)));

        // converting the time to a date format
        java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
        String initialDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE_ADDED)))
        .getTime());

        grocery.setDateItemAdded(initialDate);

        cursor.close();
        return grocery;
    }

    // Get all groceries
    public List<Grocery> getAllGrocery(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Grocery> groceries = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{
                Util.KEY_ID, Util.KEY_GROCERY_NAME, Util.KEY_GROCERY_QTY, Util.KEY_DATE_ADDED
        }, null, null, null, null, Util.KEY_GROCERY_NAME + " DESC");

        if(cursor.moveToFirst()){
            do{
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Util.KEY_GROCERY_NAME)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Util.KEY_GROCERY_NAME)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String initialDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE_ADDED)))
                .getTime());

                grocery.setDateItemAdded(initialDate);


                groceries.add(grocery);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return groceries;
    }

    // Update grocery item
    public int updateGrocery(Grocery grocery){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_GROCERY_NAME, grocery.getName());
        values.put(Util.KEY_GROCERY_QTY, grocery.getQuantity());
        values.put(Util.KEY_DATE_ADDED, java.lang.System.currentTimeMillis()); // to get the system time

        return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
                new String[]{String.valueOf(grocery.getId())});
    }

    // Delete grocery item
    public void deleteGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    // Get grocery count
    public int totalGrocery(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME, null);

        return cursor.getCount();
    }
}
