package com.example.dbm.bakingapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.dbm.bakingapp.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private static final int ITEM_BROWNIES = 1;

    private static final int POSITION_FOR_TESTING = 2;

    private IdlingResource mIdlingResource;

    private IdlingRegistry mIdlingRegistry;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingRegistry.register(mIdlingResource);
    }


    @Test
    public void verifyRecipeContent(){
        onView(ViewMatchers.withId(R.id.recycler_view_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BROWNIES,
                        click()));

        onView(withId(R.id.recycler_view_recipe_steps))
                .perform(RecyclerViewActions.scrollToPosition(POSITION_FOR_TESTING));

        onView(withText(mActivityTestRule.getActivity().getResources().getString(R.string.text_for_testing_content)))
                .check(matches(isDisplayed()));
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }
}
