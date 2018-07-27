package com.example.dbm.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.dbm.bakingapp.classes.RecipeStep;
import com.example.dbm.bakingapp.fragments.ExoplayerFragment;
import com.example.dbm.bakingapp.fragments.StepDescriptionNavigationFragment;

import java.util.List;

public class StepDetailActivity extends AppCompatActivity {

    private int mStepListIndex;
    private List<RecipeStep> mListSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_step);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.hasExtra("extra_step_index")){
            mStepListIndex = intent.getIntExtra("extra_step_index",0);
            mListSteps = intent.getParcelableArrayListExtra("extra_recipe_steps");
            //setTitle(mListSteps.get(mStepListIndex).getmStepShortDescription());
        }

        if(savedInstanceState == null) {
            generateFragments();
        }
    }

    public void generateFragments(){

        ExoplayerFragment exoplayerFragment = new ExoplayerFragment();

        exoplayerFragment.setStepsList(mListSteps);
        exoplayerFragment.setStepIndex(mStepListIndex);

        StepDescriptionNavigationFragment stepDescriptionNavigationFragment = new StepDescriptionNavigationFragment();

        stepDescriptionNavigationFragment.setStepsList(mListSteps);
        stepDescriptionNavigationFragment.setStepIndex(mStepListIndex);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.exoplayer_container, exoplayerFragment)
                .commit();


        fragmentManager.beginTransaction()
                .add(R.id.step_instruction_navigation_container, stepDescriptionNavigationFragment)
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
    }
}
