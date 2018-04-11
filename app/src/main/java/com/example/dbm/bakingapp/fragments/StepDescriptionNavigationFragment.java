package com.example.dbm.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.classes.RecipeStep;

import java.util.ArrayList;
import java.util.List;

public class StepDescriptionNavigationFragment extends Fragment {

    private static final String LOG = StepDescriptionNavigationFragment.class.getSimpleName();

    private static final String STEPS_LIST = "steps_list";

    private static final String LIST_INDEX = "list_index";

    private int mListStepIndex;

    private List<RecipeStep> listSteps;

    private Button previousStepButton;

    private Button nextStepButton;

    private TextView stepDescriptionTV;

    public StepDescriptionNavigationFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_description_navigation, container, false);

        stepDescriptionTV = (TextView) rootView.findViewById(R.id.step_description);
        previousStepButton = (Button) rootView.findViewById(R.id.previous_step_button);
        nextStepButton = (Button) rootView.findViewById(R.id.next_step_button);

        previousStepButton.setText("Previous Step");
        nextStepButton.setText("Next Step");

        if(listSteps != null) {

            getStepDataFromIndex();

            previousStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListStepIndex <= listSteps.size() - 1 && mListStepIndex != 0) {
                        mListStepIndex--;
                    }
                    getStepDataFromIndex();
                    //stepDescriptionTV.setText(listSteps.get(mListStepIndex).getmStepDescription());
                    //setStepTitle();
                }
            });

            nextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListStepIndex < listSteps.size() - 1 || mListStepIndex == 0) {
                        mListStepIndex++;
                    }
                    getStepDataFromIndex();
                    //stepDescriptionTV.setText(listSteps.get(mListStepIndex).getmStepDescription());
                    //setStepTitle();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<RecipeStep>) listSteps);
        currentState.putInt(LIST_INDEX, mListStepIndex);
    }

    public void setStepIndex(int index) {
        mListStepIndex = index;
    }

    public void setStepsList(List<RecipeStep> list){
        listSteps = list;
    }

    public void getStepDataFromIndex(){
        if(mListStepIndex == 0){
            previousStepButton.setVisibility(View.GONE);
        } else if (mListStepIndex == listSteps.size()-1){
            nextStepButton.setVisibility(View.GONE);
        } else{
            previousStepButton.setVisibility(View.VISIBLE);
            nextStepButton.setVisibility(View.VISIBLE);
        }
        if(listSteps.get(mListStepIndex).getmStepDescription().contains("�")) {
            stepDescriptionTV.setText(listSteps.get(mListStepIndex).getmStepDescription().replace("�","\u00b0"));
        } else{
            stepDescriptionTV.setText(listSteps.get(mListStepIndex).getmStepDescription());
        }
        setStepTitle();
    }

    public void setStepTitle(){
        String title = listSteps.get(mListStepIndex).getmStepShortDescription();
        if(title.contains(".")){
            getActivity().setTitle(title.substring(0,title.length() - 1));
        } else{
        getActivity().setTitle(title);
        }
    }
}
