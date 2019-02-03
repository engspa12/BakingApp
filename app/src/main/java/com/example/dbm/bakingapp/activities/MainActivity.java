package com.example.dbm.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dbm.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.dbm.bakingapp.R;
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

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.ListItemClickListener {


    public static final String TAG = "MyTag";

    private static final String LOG = MainActivity.class.getSimpleName();


    private static final String QUERY_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

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

        getIdlingResource();

        mIdlingResource.setIdleState(false);

        recipesList = (RecyclerView) findViewById(R.id.recycler_view_main);

            if (findViewById(R.id.empty_text_view_tablet) == null) {
                mTabletMode = false;

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recipesList.setLayoutManager(linearLayoutManager);
                emptyTextView = (TextView) findViewById(R.id.empty_text_view_phone);

            } else {
                mTabletMode = true;
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                recipesList.setLayoutManager(gridLayoutManager);
                emptyTextView = (TextView) findViewById(R.id.empty_text_view_tablet);
            }

        recipesList.setHasFixedSize(true);

        mRequestQueue = Volley.newRequestQueue(this);
        listOfRecipes = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOnline()) {
            startDataExtraction();
        } else{
            emptyTextView.setText(getString(R.string.no_internet_connection_message));
        }

    }

    //Make Api call to get recipes
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
                            mIdlingResource.setIdleState(true);

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
        intent.putExtra(getString(R.string.extra_recipe_ingredients),(ArrayList<RecipeIngredient>) listOfRecipes.get(clickedItemIndex).getmRecipeIngredients());
        intent.putExtra(getString(R.string.extra_recipe_steps),(ArrayList<RecipeStep>) listOfRecipes.get(clickedItemIndex).getmRecipeSteps());
        intent.putExtra(getString(R.string.extra_recipe),listOfRecipes.get(clickedItemIndex));
        startActivity(intent);
    }


    @VisibleForTesting
    @NonNull
    //Only used for testing with Espresso, so we can wait to proceed
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    //Check Internet connectivity
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
