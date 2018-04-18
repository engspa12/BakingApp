package com.example.dbm.bakingapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.dbm.bakingapp.classes.Recipe;
import com.example.dbm.bakingapp.classes.RecipeIngredient;
import com.example.dbm.bakingapp.classes.RecipeStep;
import com.example.dbm.bakingapp.fragments.ExoplayerFragment;
import com.example.dbm.bakingapp.fragments.MasterListFragment;
import com.example.dbm.bakingapp.fragments.StepDescriptionNavigationFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements MasterListFragment.OnClickRecipeListener,
        StepDescriptionNavigationFragment.ButtonChangeListener{

    private static final String LOG = RecipeDetailActivity.class.getSimpleName();

    private Recipe mRecipe;
    private List<RecipeStep> mListSteps;

    private static final int REQUEST_CODE = 123;

    private int mIndex;

    private static final String STEP_INDEX = "step_index";

    private boolean mTabletMode;

    private List<RecipeIngredient> mListIngredients;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.extra_recipe))){
            mRecipe = (Recipe) intent.getParcelableExtra(getString(R.string.extra_recipe));
            mListSteps = intent.getParcelableArrayListExtra(getString(R.string.extra_recipe_steps));
            mListIngredients = intent.getParcelableArrayListExtra(getString(R.string.extra_recipe_ingredients));
            setTitle(mRecipe.getmRecipeName());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            mIndex = data.getIntExtra(STEP_INDEX, 0);
        }
    }

    @Override
    public void onRecipeClicked(int position) {
        mIndex = position - 1;
        if(!mTabletMode) {
            if (position > 0) {
                Intent intent = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
                intent.putExtra(getString(R.string.extra_step_index), position - 1);
                intent.putExtra(getString(R.string.extra_recipe_steps), (ArrayList<RecipeStep>) mListSteps);
                intent.putExtra(getString(R.string.extra_recipe),mRecipe);
                startActivityForResult(intent,REQUEST_CODE);
            }
        } else{
            generateFragments(position - 1);
        }
    }

    public void generateFragments(int index){

        StepDescriptionNavigationFragment stepDescriptionNavigationFragment = new StepDescriptionNavigationFragment();

        stepDescriptionNavigationFragment.setStepsList(mListSteps);
        stepDescriptionNavigationFragment.setStepIndex(index);

        fragmentManager.beginTransaction()
                .replace(R.id.step_description_navigation_container, stepDescriptionNavigationFragment)
                .commit();

        ExoplayerFragment exoplayerFragment = new ExoplayerFragment();

        exoplayerFragment.setStepsList(mListSteps);
        exoplayerFragment.setStepIndex(index);

        fragmentManager.beginTransaction()
                .replace(R.id.exoplayer_container, exoplayerFragment)
                .commit();

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
        if(findViewById(R.id.step_detail_linear_layout) != null){
            mTabletMode = true;
            generateFragments(mIndex);
        } else{
            mTabletMode = false;
            mIndex = 0;
        }
    }

    @Override
    public void onButtonClicked(int index) {
        mIndex = index;
        ExoplayerFragment exoplayerFragment = new ExoplayerFragment();

        exoplayerFragment.setStepsList(mListSteps);
        exoplayerFragment.setStepIndex(index);

        fragmentManager.beginTransaction()
                .replace(R.id.exoplayer_container, exoplayerFragment)
                .commit();
    }

    //@Override
   // protected void onSaveInstanceState(Bundle outState) {
   //     super.onSaveInstanceState(outState);
   //     outState.putInt(STEP_INDEX,mIndex);
   // }

    //@Override
    //protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //    super.onRestoreInstanceState(savedInstanceState);
    //    mIndex = savedInstanceState.getInt(STEP_INDEX);
    //}

}
