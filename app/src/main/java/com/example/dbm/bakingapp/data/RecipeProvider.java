package com.example.dbm.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class RecipeProvider extends ContentProvider {

    private static final int RECIPES = 100;
    private static final int RECIPE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{

        sUriMatcher.addURI(
                RecipeContract.CONTENT_AUTHORITY,RecipeContract.PATH_RECIPES,RECIPES);

        sUriMatcher.addURI(
                RecipeContract.CONTENT_AUTHORITY,RecipeContract.PATH_RECIPES + "/#",RECIPE_ID);
    }

    private RecipeDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new RecipeDbHelper(getContext());
        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPES:
                cursor=database.query(RecipeContract.RecipeEntry.TABLE_NAME, projection, selection, selectionArgs,null,null,sortOrder);
                break;
            case RECIPE_ID:

                selection = RecipeContract.RecipeEntry.COLUMN_RECIPE_WIDGET_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(RecipeContract.RecipeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPES:
                return RecipeContract.RecipeEntry.CONTENT_LIST_TYPE;
            case RECIPE_ID:
                return RecipeContract.RecipeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPES:
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                long newRowId = database.insert(RecipeContract.RecipeEntry.TABLE_NAME,null,contentValues);

                if (newRowId != -1){
                    Toast.makeText(getContext(), "The widget was added successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"The widget couldn't be added",Toast.LENGTH_SHORT).show();
                }

                return ContentUris.withAppendedId(uri, newRowId);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPES:
                rowsDeleted = database.delete(RecipeContract.RecipeEntry.TABLE_NAME, selection, selectionArgs);
                database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + RecipeContract.RecipeEntry.TABLE_NAME + "'");
                break;
            case RECIPE_ID:
                selection = RecipeContract.RecipeEntry.COLUMN_RECIPE_WIDGET_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(RecipeContract.RecipeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        if (rowsDeleted != 0) {
            Toast.makeText(getContext(), "Widget deleted successfully", Toast.LENGTH_SHORT).show();
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
