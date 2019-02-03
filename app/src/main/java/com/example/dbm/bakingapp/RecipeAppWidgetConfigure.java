package com.example.dbm.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dbm.bakingapp.activities.MainActivity;
import com.example.dbm.bakingapp.classes.Recipe;
import com.example.dbm.bakingapp.classes.RecipeIngredient;
import com.example.dbm.bakingapp.classes.RecipeStep;
import com.example.dbm.bakingapp.data.RecipeContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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

    private TextView emptyTextViewWidgetConfiguration;

    private FrameLayout widgetConfigurationContainer;
    
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

        emptyTextViewWidgetConfiguration = (TextView) findViewById(R.id.empty_text_view_widget_configuration);
        widgetConfigurationContainer = (FrameLayout) findViewById(R.id.widget_configuration_container);

        if(isOnline()) {
            emptyTextViewWidgetConfiguration.setVisibility(View.GONE);
            widgetConfigurationContainer.setVisibility(View.VISIBLE);
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
        } else {
            widgetConfigurationContainer.setVisibility(View.GONE);
            emptyTextViewWidgetConfiguration.setVisibility(View.VISIBLE);
            emptyTextViewWidgetConfiguration.setText(getString(R.string.no_internet_connection_message));
        }
    }

    //Save recipe information in database via a Content Provider
    public void saveRecipeInDatabase(int recipeChoice){
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_WIDGET_ID, mAppWidgetId);
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_CHOICE, recipeChoice);
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, listOfRecipes.get(recipeChoice).getmRecipeName());
        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS,
               getIngredientsStringToStore((ArrayList<String>) getIngredientsStringList(listOfRecipes.get(recipeChoice).getmRecipeIngredients())));

        getContentResolver().insert(mUri,values);
    }

    //Finish configuration for widget
    public void endConfiguration(int recipeChoice){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(),
                R.layout.recipe_widget);

        remoteViews.setTextViewText(R.id.widget_recipe_title,listOfRecipes.get(recipeChoice).getmRecipeName());

        Intent svcIntent = new Intent(this, WidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        svcIntent.putExtra("Ingredients_List",
                (ArrayList<String>) getIngredientsStringList(listOfRecipes.get(recipeChoice).getmRecipeIngredients()));

        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter(mAppWidgetId, R.id.listViewWidget,
                svcIntent);

        appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    //Get recipes data from Api
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


    //Get list of a recipe's ingredients
    public List<String> getIngredientsStringList(List<RecipeIngredient> listOfIngredients){

        List<String> list = new ArrayList<>();

        for(int i=0;i<listOfIngredients.size();i++){
            if(listOfIngredients.get(i).getmIngredientMeasure().equals("UNIT")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                        + listOfIngredients.get(i).getmIngredientName());
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("TSP")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " teaspoons of "
                        + listOfIngredients.get(i).getmIngredientName());
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("TBLSP")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " tablespoons of "
                        + listOfIngredients.get(i).getmIngredientName());
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("CUPS")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                        + listOfIngredients.get(i).getmIngredientName());
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("OZ")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " ounces of "
                        + listOfIngredients.get(i).getmIngredientName());
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("K")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " kilograms of "
                        + listOfIngredients.get(i).getmIngredientName());
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("G")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " grams of "
                        + listOfIngredients.get(i).getmIngredientName());
            } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("CUP")){
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                        + listOfIngredients.get(i).getmIngredientName());
            } else{
                list.add("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                        + listOfIngredients.get(i).getmIngredientMeasure() + " of " + listOfIngredients.get(i).getmIngredientName() + "\n");
            }
        }

        return list;

    }

    //Return an String from an ArrayList<String>
    public String getIngredientsStringToStore(ArrayList<String> ingredientsList){

        //Convert ArrayList<String> to a String using Gson library
        //Each ingredient is a String in the list
        Gson gson = new Gson();
        return gson.toJson(ingredientsList);

    }

    //Return int or double value depending on the input
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

        Uri wantedUri;

        Gson gson = new Gson();

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));

        for(int i=0;i<appWidgetIds.length;i++) {
            wantedUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI,appWidgetIds[i]);
            Cursor cursor = context.getContentResolver().query(wantedUri, projection, null, null, null);

            if(cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();

                    //Retrieve the ArrayList<String> of ingredients
                    Type type = new TypeToken<ArrayList<String>>() {}.getType();
                    String output = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS));
                    ArrayList<String> finalOutputString = gson.fromJson(output, type);

                    RemoteViews views = new RemoteViews(context.getPackageName(),
                            R.layout.recipe_widget);
                    //Set Recipe name in Widget
                    views.setTextViewText(R.id.widget_recipe_title,cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME)));

                    Intent svcIntent = new Intent(context, WidgetService.class);

                    svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
                    svcIntent.putExtra("Ingredients_List",finalOutputString);

                    svcIntent.setData(Uri.parse(
                            svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

                    //Set RemoteAdapter for ListView
                    views.setRemoteAdapter(appWidgetIds[i], R.id.listViewWidget,
                            svcIntent);

                    manager.updateAppWidget(appWidgetIds[i], views);

                }
            }
            cursor.close();
        }
    }

    public static void deleteWidgets(Context context, int[] widgetsDeleted){

        Uri wantedUri;

        if(widgetsDeleted.length != 0) {

            for (int i = 0; i < widgetsDeleted.length; i++) {
                //Delete widget from database
                wantedUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, widgetsDeleted[i]);
                context.getContentResolver().delete(wantedUri, null, null);
            }
        }
    }

    //Check Internet connectivity
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
