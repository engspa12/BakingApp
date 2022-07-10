package com.example.dbm.bakingapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.domain.Recipe;
import com.example.dbm.bakingapp.domain.RecipeIngredient;
import com.example.dbm.bakingapp.domain.RecipeStep;
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

    private boolean firstLaunch;

    private boolean mTabletMode;

    private List<RecipeIngredient> mListIngredients;

    private FrameLayout mainFrameLayout;

    private TextView emptyTextViewRecipeDetail;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        emptyTextViewRecipeDetail = (TextView) findViewById(R.id.empty_text_view_recipe_detail);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        firstLaunch = true;

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
            if (!mTabletMode) {
                if (position > 0) {
                    Intent intent = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
                    intent.putExtra(getString(R.string.extra_step_index), position - 1);
                    intent.putExtra(getString(R.string.extra_recipe_steps), (ArrayList<RecipeStep>) mListSteps);
                    intent.putExtra(getString(R.string.extra_recipe), mRecipe);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            } else {
                if(isOnline()) {
                    generateFragments(position - 1);
                    mainFrameLayout.setVisibility(View.VISIBLE);
                    emptyTextViewRecipeDetail.setVisibility(View.GONE);
                } else{
                    mainFrameLayout.setVisibility(View.GONE);
                    emptyTextViewRecipeDetail.setVisibility(View.VISIBLE);
                    emptyTextViewRecipeDetail.setText(getString(R.string.no_internet_connection_message));
                }
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
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot()) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mainFrameLayout = (FrameLayout) findViewById(R.id.main_container);

        if(isOnline()) {
            if (findViewById(R.id.step_detail_linear_layout) != null) {
                mTabletMode = true;
                if(firstLaunch) {
                    generateFragments(mIndex);
                    firstLaunch = false;
                }
            } else {
                mTabletMode = false;
                mIndex = 0;
            }
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyTextViewRecipeDetail.setVisibility(View.GONE);
        } else{
            mainFrameLayout.setVisibility(View.GONE);
            emptyTextViewRecipeDetail.setVisibility(View.VISIBLE);
            emptyTextViewRecipeDetail.setText(getString(R.string.no_internet_connection_message));
        }
    }

    @Override
    public void onButtonClicked(int index) {
        if (isOnline()) {
            mIndex = index;
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyTextViewRecipeDetail.setVisibility(View.GONE);
            ExoplayerFragment exoplayerFragment = new ExoplayerFragment();

            exoplayerFragment.setStepsList(mListSteps);
            exoplayerFragment.setStepIndex(index);

            fragmentManager.beginTransaction()
                    .replace(R.id.exoplayer_container, exoplayerFragment)
                    .commit();
        } else{
            mainFrameLayout.setVisibility(View.GONE);
            emptyTextViewRecipeDetail.setVisibility(View.VISIBLE);
            emptyTextViewRecipeDetail.setText(getString(R.string.no_internet_connection_message));
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
