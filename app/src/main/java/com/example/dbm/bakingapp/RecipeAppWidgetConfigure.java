package com.example.dbm.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dbm.bakingapp.adapters.RecipeAdapter;
import com.example.dbm.bakingapp.classes.Recipe;
import com.example.dbm.bakingapp.classes.RecipeIngredient;
import com.example.dbm.bakingapp.classes.RecipeStep;
import com.example.dbm.bakingapp.data.RecipeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeAppWidgetConfigure extends AppCompatActivity {

    private int mAppWidgetId;


    private TextView nutellaPieTV;
    private TextView browniesTV;
    private TextView yellowCake;
    private TextView cheesecake;

    private static final int NUTELLA_PIE_CHOICE = 0;
    private static final int BROWNIES_CHOICE = 1;
    private static final int YELLOW_CAKE_CHOICE = 2;
    private static final int CHEESECAKE_CHOICE = 3;

    private static final String LOG = MainActivity.class.getSimpleName();

    private static final String QUERY_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static final String TAG = "MyTag";

    private  RequestQueue mRequestQueue;

    private List<Recipe> listOfRecipes;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_app_widget_configure);

        setTitle(getString(R.string.configuration_title));

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        mRequestQueue = Volley.newRequestQueue(this);
        mUri = RecipeContract.RecipeEntry.CONTENT_URI;
        listOfRecipes = new ArrayList<>();
        getRecipesData();

        nutellaPieTV = (TextView) findViewById(R.id.nutella_pie_tv);
        nutellaPieTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipeInDatabase(NUTELLA_PIE_CHOICE);
                endConfiguration(NUTELLA_PIE_CHOICE);
            }
        });
        nutellaPieTV.setVisibility(View.GONE);

        browniesTV = (TextView) findViewById(R.id.brownies_tv);
        browniesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipeInDatabase(BROWNIES_CHOICE);
                endConfiguration(BROWNIES_CHOICE);
            }
        });
        browniesTV.setVisibility(View.GONE);

        yellowCake = (TextView) findViewById(R.id.yellow_cake_tv);
        yellowCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipeInDatabase(YELLOW_CAKE_CHOICE);
                endConfiguration(YELLOW_CAKE_CHOICE);
            }
        });
        yellowCake.setVisibility(View.GONE);

        cheesecake = (TextView) findViewById(R.id.cheesecake_tv);
        cheesecake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipeInDatabase(CHEESECAKE_CHOICE);
                endConfiguration(CHEESECAKE_CHOICE);
            }
        });
        cheesecake.setVisibility(View.GONE);


    }

    public void saveRecipeInDatabase(int recipeChoice){
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_WIDGET_ID, mAppWidgetId);
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_CHOICE, recipeChoice);
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, listOfRecipes.get(recipeChoice).getmRecipeName());
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS,
                getIngredientsDetails(listOfRecipes.get(recipeChoice).getmRecipeIngredients()));

        getContentResolver().insert(mUri,values);
    }

    public void endConfiguration(int recipeChoice){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        //Intent intentForPending = new Intent(this, RecipeDetailActivity.class);
        //intentForPending.putExtra(getString(R.string.extra_recipe_ingredients),(ArrayList<RecipeIngredient>) listOfRecipes.get(recipeChoice).getmRecipeIngredients());
        //intentForPending.putExtra(getString(R.string.extra_recipe_steps),(ArrayList<RecipeStep>) listOfRecipes.get(recipeChoice).getmRecipeSteps());
        //intentForPending.putExtra(getString(R.string.extra_recipe),listOfRecipes.get(recipeChoice));
        //intentForPending.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, recipeChoice, intentForPending, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(this.getPackageName(),
                R.layout.recipe_widget);
        views.setTextViewText(R.id.widget_recipe_ingredients,getIngredientsDetails(listOfRecipes.get(recipeChoice).getmRecipeIngredients()));
        views.setTextViewText(R.id.widget_recipe_title,listOfRecipes.get(recipeChoice).getmRecipeName());
        //views.setOnClickPendingIntent(R.id.widget_recipe_title,pendingIntent);
        //views.setOnClickPendingIntent(R.id.widget_recipe_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    public void getRecipesData(){
        URL queryUrl;

        try {
            queryUrl = new URL(QUERY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            queryUrl = null;
        }

        listOfRecipes.clear();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, queryUrl.toString(), null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++) {
                                JSONObject recipe = response.getJSONObject(i);
                                int recipeId = recipe.getInt("id");
                                String recipeName = recipe.getString("name");
                                int recipeServings = recipe.getInt("servings");
                                String recipeImage = recipe.getString("image");

                                JSONArray recipeIngredients = recipe.getJSONArray("ingredients");
                                JSONArray recipeSteps = recipe.getJSONArray("steps");

                                List<RecipeIngredient> ingredientsList = new ArrayList<>();
                                List<RecipeStep> stepsList = new ArrayList<>();

                                for(int j=0;j<recipeIngredients.length();j++){
                                    JSONObject ingredient = recipeIngredients.getJSONObject(j);
                                    double ingredientQuantity = ingredient.getDouble("quantity");
                                    String ingredientMeasure = ingredient.getString("measure");
                                    String ingredientName = ingredient.getString("ingredient");

                                    ingredientsList.add(new RecipeIngredient(ingredientQuantity,ingredientMeasure,ingredientName));
                                }

                                for(int k=0;k<recipeSteps.length();k++){
                                    JSONObject step = recipeSteps.getJSONObject(k);
                                    int stepId = step.getInt("id");
                                    String stepShortDescription = step.getString("shortDescription");
                                    String stepDescription = step.getString("description");
                                    String stepVideoUrl = step.getString("videoURL");
                                    String stepThumbnailUrl = step.getString("thumbnailURL");

                                    stepsList.add(new RecipeStep(stepId,stepShortDescription,stepDescription,stepVideoUrl,stepThumbnailUrl));
                                }

                                listOfRecipes.add(new Recipe(recipeId,recipeName,ingredientsList,stepsList,recipeServings,recipeImage));

                            }
                            nutellaPieTV.setVisibility(View.VISIBLE);
                            browniesTV.setVisibility(View.VISIBLE);
                            yellowCake.setVisibility(View.VISIBLE);
                            cheesecake.setVisibility(View.VISIBLE);
                            //return listOfRecipes;

                        } catch( JSONException e){
                            Log.e(LOG,e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG,error.getMessage());
                    }
                });

        jsonArrayRequest.setTag(TAG);

        mRequestQueue.add(jsonArrayRequest);
    }

    public String getIngredientsDetails(List<RecipeIngredient> listOfIngredients){

        StringBuilder builder = new StringBuilder();
        builder.append("INGREDIENTS: " + "\n\n");

        for(int i=0;i<listOfIngredients.size();i++){
            if(listOfIngredients.get(i).getmIngredientMeasure().equals("UNIT")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("TSP")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " teaspoons of "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("TBLSP")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " tablespoons of "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("CUPS")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("OZ")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " ounces of "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("K")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " kilograms of "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("G")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " grams of "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("CUP")){
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                        + listOfIngredients.get(i).getmIngredientName() + "\n");
            } else{
                builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                        + listOfIngredients.get(i).getmIngredientMeasure() + " of " + listOfIngredients.get(i).getmIngredientName() + "\n");
            }
        }

        return builder.toString();

    }

    public String getCorrectValue(double input){
        if(input % 1 == 0){
            return String.valueOf((int) input);
        }
        return String.valueOf(input);
    }

    public static void updateWidgets(Context context){

        String[] projection = {RecipeContract.RecipeEntry.COLUMN_RECIPE_WIDGET_ID,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_CHOICE,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS};

        Uri wantedUri = null;

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));

        for(int i=0;i<appWidgetIds.length;i++) {
            wantedUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI,appWidgetIds[i]);
            Cursor cursor = context.getContentResolver().query(wantedUri, projection, null, null, null);

            if(cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();

                    RemoteViews views = new RemoteViews(context.getPackageName(),
                            R.layout.recipe_widget);
                    views.setTextViewText(R.id.widget_recipe_ingredients,cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS)));
                    views.setTextViewText(R.id.widget_recipe_title,cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME)));

                    manager.updateAppWidget(appWidgetIds[i], views);

                }
            }
        }
    }

    public static void deleteWidgets(Context context, int[] widgetsDeleted){

        Uri wantedUri = null;

        for(int i=0;i<widgetsDeleted.length;i++){
            wantedUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI,widgetsDeleted[i]);
            context.getContentResolver().delete(wantedUri,null,null);
        }
    }
}
