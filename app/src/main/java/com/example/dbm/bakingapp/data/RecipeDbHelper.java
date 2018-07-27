package com.example.dbm.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dbm.bakingapp.data.RecipeContract;

public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "widgets_recipes.db";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +
            RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RecipeContract.RecipeEntry.COLUMN_RECIPE_WIDGET_ID + " INTEGER NOT NULL," +
            RecipeContract.RecipeEntry.COLUMN_RECIPE_CHOICE + " INTEGER NOT NULL," +
            RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL," +
            RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS+ " TEXT NOT NULL)";


    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME;

    public RecipeDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
