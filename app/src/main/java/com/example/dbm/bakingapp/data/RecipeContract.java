package com.example.dbm.bakingapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.dbm.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPES = "recipes";

    private RecipeContract(){}

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_RECIPES);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public final static String TABLE_NAME = "recipes";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_RECIPE_WIDGET_ID = "recipe_widget_id";
        public final static String COLUMN_RECIPE_CHOICE = "recipe_choice";
        public final static String COLUMN_RECIPE_NAME = "recipe_name";
        public final static String COLUMN_RECIPE_INGREDIENTS = "recipe_ingredients";

    }
}
