package com.example.dbm.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.classes.RecipeStep;

import java.util.List;

public class ExoplayerFragment extends Fragment {

    private int mListStepIndex;

    private List<RecipeStep> mListSteps;

    private static final String LOG = ExoplayerFragment.class.getSimpleName();

    public ExoplayerFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exoplayer, container, false);

        TextView exoplayertextView = (TextView) rootView.findViewById(R.id.exoplayer_tv);

        exoplayertextView.setText("EXOPLAYER PLACEHOLDER");

        return rootView;
    }

    public void setStepsList(List<RecipeStep> list){
        mListSteps = list;
    }

    public void setStepIndex(int index){
        mListStepIndex = index;
    }
}
