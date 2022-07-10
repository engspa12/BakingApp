package com.example.dbm.bakingapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.domain.Recipe;
import com.example.dbm.bakingapp.domain.RecipeStep;
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

    private FrameLayout exoplayerFrameLayout;
    private FrameLayout navigationFrameLayout;

    private boolean screenRotated;

    private boolean firstLaunch;

    private Recipe mRecipe;

    private TextView emptyTextViewStepDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_step);

        emptyTextViewStepDetail = (TextView) findViewById(R.id.empty_text_view_step_detail);

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

    public void buildFragments() {

        if (findViewById(R.id.step_description_navigation_container) != null) {


            ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);

            //Step text and navigation
            stepDescriptionNavigationFragment = new StepDescriptionNavigationFragment();

            stepDescriptionNavigationFragment.setStepsList(mListSteps);
            stepDescriptionNavigationFragment.setStepIndex(mStepListIndex);

            fragmentManager.beginTransaction()
                    .replace(R.id.step_description_navigation_container, stepDescriptionNavigationFragment)
                    .commit();

            if (firstLaunch) {
                //Exoplayer
                exoplayerFragment = new ExoplayerFragment();

                exoplayerFragment.setStepsList(mListSteps);
                exoplayerFragment.setStepIndex(mStepListIndex);

                fragmentManager.beginTransaction()
                        .replace(R.id.exoplayer_container, exoplayerFragment)
                        .commit();

                firstLaunch = false;
            }


        } else {
            //Only Exoplayer is shown
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
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            screenRotated = false;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenRotated = true;
        }

        exoplayerFrameLayout = (FrameLayout) findViewById(R.id.exoplayer_container);


        if (findViewById(R.id.step_description_navigation_container) != null) {
            navigationFrameLayout = (FrameLayout) findViewById(R.id.step_description_navigation_container);
            if (isOnline()) {
                exoplayerFrameLayout.setVisibility(View.VISIBLE);
                navigationFrameLayout.setVisibility(View.VISIBLE);
                emptyTextViewStepDetail.setVisibility(View.GONE);
                checkFragments();
            } else {
                exoplayerFrameLayout.setVisibility(View.GONE);
                navigationFrameLayout.setVisibility(View.GONE);
                emptyTextViewStepDetail.setVisibility(View.VISIBLE);
                emptyTextViewStepDetail.setText(getString(R.string.no_internet_connection_message));
            }
        } else {
            if (isOnline()) {
                exoplayerFrameLayout.setVisibility(View.VISIBLE);
                emptyTextViewStepDetail.setVisibility(View.GONE);
                checkFragments();
            } else {
                exoplayerFrameLayout.setVisibility(View.GONE);
                emptyTextViewStepDetail.setVisibility(View.VISIBLE);
                emptyTextViewStepDetail.setText(getString(R.string.no_internet_connection_message));
            }
        }
    }

    @Override
    public void onButtonClicked(int index) {

        if (isOnline()) {
            navigationFrameLayout.setVisibility(View.VISIBLE);
            exoplayerFrameLayout.setVisibility(View.VISIBLE);
            exoplayerFragment = new ExoplayerFragment();
            emptyTextViewStepDetail.setVisibility(View.GONE);

            exoplayerFragment.setStepsList(mListSteps);
            exoplayerFragment.setStepIndex(index);

            fragmentManager.beginTransaction()
                    .replace(R.id.exoplayer_container, exoplayerFragment)
                    .commit();

            mStepListIndex = index;
        } else {
            emptyTextViewStepDetail.setText(getString(R.string.no_internet_connection_message));
            emptyTextViewStepDetail.setVisibility(View.VISIBLE);
            navigationFrameLayout.setVisibility(View.GONE);
            exoplayerFrameLayout.setVisibility(View.GONE);
        }

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
        intent.putExtra(STEP_INDEX, mStepListIndex);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
