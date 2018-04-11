package com.example.dbm.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dbm.bakingapp.adapters.MasterListAdapter;
import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.classes.RecipeIngredient;
import com.example.dbm.bakingapp.classes.RecipeStep;

import java.util.List;

public class MasterListFragment extends Fragment implements MasterListAdapter.ListItemDetailClickListener {

    private static final String LOG = MasterListFragment.class.getSimpleName();

    private List<RecipeStep> mListSteps;

    private List<RecipeIngredient> mListIngredients;

    private boolean tabletModeEnabled;

    //private Recipe mRecipe;

    OnClickRecipeListener mCallback;

    public interface OnClickRecipeListener {
        void onRecipeClicked(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnClickRecipeListener) context;
            //tabletModeEnabled = getActivity().getIntent().getBooleanExtra("boolean_test",true);
            //mRecipe = (Recipe) getActivity().getIntent().getParcelableExtra("extra_recipe");
            mListSteps = getActivity().getIntent().getParcelableArrayListExtra("extra_recipe_steps");
            mListIngredients = getActivity().getIntent().getParcelableArrayListExtra("extra_recipe_ingredients");
            Log.v(LOG,"list was OKAY");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public MasterListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list,container,false);

        RecyclerView recyclerViewRecipeIngredients = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_steps);

        //TextView ingredientsTextView = (TextView) rootView.findViewById(R.id.recipe_ingredients_tv);
        //ingredientsTextView.setText(mListIngredients.toString());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewRecipeIngredients.setLayoutManager(linearLayoutManager);
        recyclerViewRecipeIngredients.setHasFixedSize(true);
        //recyclerViewRecipeIngredients.setNestedScrollingEnabled(false);

        MasterListAdapter mAdapter = new MasterListAdapter(mListSteps,mListSteps.size() + 1,this,getContext(),false,mListIngredients);

        recyclerViewRecipeIngredients.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mCallback.onRecipeClicked(clickedItemIndex);
    }

    public void setmListSteps(List<RecipeStep> list){
        mListSteps = list;
    }

    public void setTabletModeEnabled(boolean tabletMode){
        tabletModeEnabled = tabletMode;
    }

}

