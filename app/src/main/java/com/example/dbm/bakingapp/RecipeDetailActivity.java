package com.example.dbm.bakingapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.dbm.bakingapp.classes.Recipe;
import com.example.dbm.bakingapp.classes.RecipeIngredient;
import com.example.dbm.bakingapp.classes.RecipeStep;
import com.example.dbm.bakingapp.fragments.MasterListFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements MasterListFragment.OnClickRecipeListener {

    private Recipe mRecipe;
    private List<RecipeStep> mListSteps;

    private List<RecipeIngredient> mListIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.hasExtra("extra_recipe")){
            mRecipe = (Recipe) intent.getParcelableExtra("extra_recipe");
            mListSteps = intent.getParcelableArrayListExtra("extra_recipe_steps");
            mListIngredients = intent.getParcelableArrayListExtra("extra_recipe_ingredients");
            setTitle(mRecipe.getmRecipeName());
        }
    }

    @Override
    public void onRecipeClicked(int position) {
        if(position > 0){
            Intent intent = new Intent(RecipeDetailActivity.this,StepDetailActivity.class);
            intent.putExtra("extra_step_index",position-1);
            intent.putExtra("extra_recipe_steps",(ArrayList<RecipeStep>) mListSteps);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
