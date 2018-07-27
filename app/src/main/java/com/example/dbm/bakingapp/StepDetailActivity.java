package com.example.dbm.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.dbm.bakingapp.classes.Recipe;
import com.example.dbm.bakingapp.classes.RecipeStep;
import com.example.dbm.bakingapp.fragments.ExoplayerFragment;
import com.example.dbm.bakingapp.fragments.StepDescriptionNavigationFragment;

import java.util.List;

public class StepDetailActivity extends AppCompatActivity implements StepDescriptionNavigationFragment.ButtonChangeListener {

    private int mStepListIndex;
    private List<RecipeStep> mListSteps;

    private ActionBar ab;

    private static final String STEP_INDEX = "step_index";

    private ExoplayerFragment exoplayerFragment;
    private StepDescriptionNavigationFragment stepDescriptionNavigationFragment;

    private FragmentManager fragmentManager;

    private boolean screenRotated;

    private boolean firstLaunch;

    private Recipe mRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_step);

        firstLaunch = true;

        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.extra_step_index))) {
            mStepListIndex = intent.getIntExtra(getString(R.string.extra_step_index), 0);
            mListSteps = intent.getParcelableArrayListExtra(getString(R.string.extra_recipe_steps));
            mRecipe = intent.getParcelableExtra(getString(R.string.extra_recipe));
            setTitle(mRecipe.getmRecipeName());
        }

    }

    public void checkFragments() {

            if (screenRotated) {
                if (!firstLaunch) {
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                    ab = getSupportActionBar();
                    ab.hide();
                } else {
                    buildFragments();
                    firstLaunch = false;
                }
            } else {
                buildFragments();
                firstLaunch = false;
            }

    }

    public void buildFragments(){

        if (findViewById(R.id.step_description_navigation_container) != null) {


            ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);

            stepDescriptionNavigationFragment = new StepDescriptionNavigationFragment();

            stepDescriptionNavigationFragment.setStepsList(mListSteps);
            stepDescriptionNavigationFragment.setStepIndex(mStepListIndex);

            fragmentManager.beginTransaction()
                    .replace(R.id.step_description_navigation_container, stepDescriptionNavigationFragment)
                    .commit();

            if (firstLaunch) {

                exoplayerFragment = new ExoplayerFragment();

                exoplayerFragment.setStepsList(mListSteps);
                exoplayerFragment.setStepIndex(mStepListIndex);

                fragmentManager.beginTransaction()
                        .replace(R.id.exoplayer_container, exoplayerFragment)
                        .commit();

                firstLaunch = false;
            }


        } else {

            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            ab = getSupportActionBar();
            ab.hide();

            exoplayerFragment = new ExoplayerFragment();

            exoplayerFragment.setStepsList(mListSteps);
            exoplayerFragment.setStepIndex(mStepListIndex);

            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.exoplayer_container, exoplayerFragment)
                    .commit();
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

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            screenRotated = false;
        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            screenRotated = true;
        }
        checkFragments();
        // THE FRAGMENT IS GENERATED TWICE, you are calling generateFragments() after onCreateView in ExoplayerFragment
    }

    @Override
    public void onButtonClicked(int index) {

        exoplayerFragment = new ExoplayerFragment();
        exoplayerFragment.setStepsList(mListSteps);
        exoplayerFragment.setStepIndex(index);

        fragmentManager.beginTransaction()
                .replace(R.id.exoplayer_container, exoplayerFragment)
                .commit();

        mStepListIndex = index;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("saved_index", mStepListIndex);
        outState.putBoolean("rotation", screenRotated);
        outState.putBoolean("first_launch", firstLaunch);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStepListIndex = savedInstanceState.getInt("saved_index");
        screenRotated = savedInstanceState.getBoolean("rotation");
        firstLaunch = savedInstanceState.getBoolean("first_launch");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(STEP_INDEX,mStepListIndex);
        setResult(Activity.RESULT_OK,intent);
        super.onBackPressed();
    }
}
