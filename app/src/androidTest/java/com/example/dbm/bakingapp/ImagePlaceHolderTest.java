package com.example.dbm.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ImagePlaceHolderTest {

    private static final int ITEM_BROWNIES = 1;

    private static final int POSITION_FOR_TESTING = 9;

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
   public void imagePlaceholderTest(){
    onView(withId(R.id.recycler_view_main))
             .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BROWNIES,
                     click()));

    onView(withId(R.id.recycler_view_recipe_steps))
            .perform(RecyclerViewActions.scrollToPosition(POSITION_FOR_TESTING));

     onView(withText(mActivityTestRule.getActivity().getResources().getString(R.string.text_for_testing_placeholder)))
             .perform(click());

     onView(withId(R.id.no_video_text_view))
             .check(matches(withText(mActivityTestRule.getActivity().getResources().getString(R.string.no_video_or_image_message))));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }
}
