package com.example.dbm.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    OnClickRecipeListener mCallback;

    public interface OnClickRecipeListener {
        void onRecipeClicked(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnClickRecipeListener) context;
            mListSteps = getActivity().getIntent().getParcelableArrayListExtra(getString(R.string.extra_recipe_steps));
            mListIngredients = getActivity().getIntent().getParcelableArrayListExtra(getString(R.string.extra_recipe_ingredients));

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnClickRecipeListener");
        }
    }

    public MasterListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list,container,false);

        RecyclerView recyclerViewRecipeIngredients = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_steps);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerViewRecipeIngredients.setLayoutManager(linearLayoutManager);
            recyclerViewRecipeIngredients.setHasFixedSize(true);

            MasterListAdapter mAdapter = new MasterListAdapter(mListSteps, mListSteps.size() + 1, this, getContext(), mListIngredients);

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


}

