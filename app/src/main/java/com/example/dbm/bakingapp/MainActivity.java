package com.example.dbm.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    private static final String LOG = MainActivity.class.getSimpleName();

    private static final String QUERY_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static final String TAG = "MyTag";

    private boolean mTabletMode;
    private RecyclerView recipesList;
    private RecipeAdapter mAdapter;

    private RequestQueue mRequestQueue;

    private List<Recipe> listOfRecipes;

    private TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(findViewById(R.id.recycler_view_tablet) == null){
            mTabletMode = false;
            recipesList = (RecyclerView) findViewById(R.id.recycler_view_phone);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recipesList.setLayoutManager(linearLayoutManager);


        } else{
            mTabletMode = true;
            recipesList = (RecyclerView) findViewById(R.id.recycler_view_tablet);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            recipesList.setLayoutManager(gridLayoutManager);

        }

        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        recipesList.setHasFixedSize(true);

        mRequestQueue = Volley.newRequestQueue(this);
        listOfRecipes = new ArrayList<>();

        startDataExtraction();

    }

    public void getDataFromHttpUrlUsingJSON(String url){


        listOfRecipes.clear();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

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

                            mAdapter = new RecipeAdapter(listOfRecipes,listOfRecipes.size(),MainActivity.this,MainActivity.this,mTabletMode);
                            recipesList.setAdapter(mAdapter);
                            emptyTextView.setVisibility(View.GONE);


                        } catch( JSONException e){
                            Log.e(LOG,e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        emptyTextView.setText(getString(R.string.error_get_data));
                    }
                });

        jsonArrayRequest.setTag(TAG);

        mRequestQueue.add(jsonArrayRequest);
    }

    public URL buildUrl(){
        try {
            URL queryUrl = new URL(QUERY_URL);
            return queryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startDataExtraction(){
        URL url = buildUrl();
        getDataFromHttpUrlUsingJSON(url.toString());
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MainActivity.this,RecipeDetailActivity.class);
        //ArrayList<RecipeIngredient> ingredients = (ArrayList<RecipeIngredient>) listOfRecipes.get(clickedItemIndex).getmRecipeIngredients();
        //ArrayList<RecipeStep> steps = (ArrayList<RecipeStep>) listOfRecipes.get(clickedItemIndex).getmRecipeSteps();
        //Parcelable recipe = listOfRecipes.get(clickedItemIndex);
        intent.putExtra(getString(R.string.extra_recipe_ingredients),(ArrayList<RecipeIngredient>) listOfRecipes.get(clickedItemIndex).getmRecipeIngredients());
        intent.putExtra(getString(R.string.extra_recipe_steps),(ArrayList<RecipeStep>) listOfRecipes.get(clickedItemIndex).getmRecipeSteps());
        intent.putExtra(getString(R.string.extra_recipe),listOfRecipes.get(clickedItemIndex));
        startActivity(intent);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}
