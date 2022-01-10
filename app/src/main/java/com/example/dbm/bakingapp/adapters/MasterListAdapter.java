package com.example.dbm.bakingapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.classes.RecipeIngredient;
import com.example.dbm.bakingapp.classes.RecipeStep;

import java.util.List;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.StepViewHolder> {

    private static final String LOG = MasterListAdapter.class.getSimpleName();

    private final ListItemDetailClickListener mOnClickListener;

    private int mNumberOfSteps;

    private List<RecipeStep> listOfSteps;
    private List<RecipeIngredient> listOfIngredients;

    private Context mContext;

    public interface ListItemDetailClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_step_item_view, parent, false);

        StepViewHolder viewHolder = new StepViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberOfSteps;
    }

    public MasterListAdapter(List<RecipeStep> steps, int numberOfItems, ListItemDetailClickListener listener, Context context,
                             List<RecipeIngredient> ingredients) {

        //Pass both the ingredients list and the steps list
        listOfSteps = steps;
        mNumberOfSteps = numberOfItems;
        mOnClickListener = listener;
        mContext = context;
        listOfIngredients = ingredients;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepTitleTextView;

        TextView stepNumberTextView;

        ImageView videoPresentImageView;

        FrameLayout imageViewContainer;

        private StepViewHolder(View itemView) {
            super(itemView);

            stepTitleTextView = (TextView) itemView.findViewById(R.id.text_item_tv);
            stepNumberTextView = (TextView) itemView.findViewById(R.id.number_item_tv);
            videoPresentImageView = (ImageView) itemView.findViewById(R.id.iv_video_present);
            imageViewContainer = (FrameLayout) itemView.findViewById(R.id.image_view_container);

            itemView.setOnClickListener(this);
        }

        private void bind(int listIndex) {

            if (listIndex == 0) {
                //Use the ingredients list to generate a String and this will be the item with index 0 in the adapter
                StringBuilder builder = new StringBuilder();
                builder.append(mContext.getString(R.string.ingredients_title) + "\n\n");

                for (int i = 0; i < listOfIngredients.size(); i++) {
                    if (listOfIngredients.get(i).getmIngredientMeasure().equals("UNIT")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if (listOfIngredients.get(i).getmIngredientMeasure().equals("TSP")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " teaspoons of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if (listOfIngredients.get(i).getmIngredientMeasure().equals("TBLSP")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " tablespoons of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if (listOfIngredients.get(i).getmIngredientMeasure().equals("CUPS")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if (listOfIngredients.get(i).getmIngredientMeasure().equals("OZ")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " ounces of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if (listOfIngredients.get(i).getmIngredientMeasure().equals("K")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " kilograms of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if (listOfIngredients.get(i).getmIngredientMeasure().equals("G")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " grams of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if (listOfIngredients.get(i).getmIngredientMeasure().equals("CUP")) {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else {
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                                + listOfIngredients.get(i).getmIngredientMeasure() + " of " + listOfIngredients.get(i).getmIngredientName() + "\n");
                    }
                }

                String ingredientsList = builder.toString();

                //Set the ingredients list as a String without the last character --> "\n"
                stepTitleTextView.setText(ingredientsList.substring(0,ingredientsList.length() - 1));
                stepTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                stepTitleTextView.setGravity(Gravity.CENTER_VERTICAL);
                stepTitleTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecipeIngredients));
                stepTitleTextView.setVisibility(View.VISIBLE);
                stepNumberTextView.setVisibility(View.GONE);
                imageViewContainer.setVisibility(View.GONE);
            } else {
                stepTitleTextView.setVisibility(View.VISIBLE);
                stepNumberTextView.setVisibility(View.VISIBLE);
                stepTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                stepTitleTextView.setGravity(Gravity.CENTER_VERTICAL);
                //Subtract 1 because we include the ingredients list as item with index 0 in the adapter
                //therefore item 1 in adapter will be passed as item 0, item 2 will be passed as item 1 and so on
                //this is done to identify the correct position in the steps list
                setStep(listIndex - 1);
                stepTitleTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecipeSteps));
                stepNumberTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorStepNumber));
            }

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        private void setStep(int index) {
            String title = listOfSteps.get(index).getmStepShortDescription();
            String urlVideo = listOfSteps.get(index).getmStepVideoUrl();

            if (title.contains(".")) {
                stepTitleTextView.setText(title.substring(0, title.length() - 1));
            } else {
                stepTitleTextView.setText(title);
            }

            if(index != 0) {
                stepNumberTextView.setText(String.valueOf(index));
                stepNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            } else{
                stepNumberTextView.setText("∙");
                stepNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            }


            if(urlVideo != null && !(urlVideo.equals(""))){
                videoPresentImageView.setVisibility(View.VISIBLE);
            } else{
                videoPresentImageView.setVisibility(View.INVISIBLE);
            }
            imageViewContainer.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecipeSteps));
            imageViewContainer.setVisibility(View.VISIBLE);
        }
    }

    //Return an int or double value depending upon the input
    private String getCorrectValue(double input) {
        if (input % 1 == 0) {
            return String.valueOf((int) input);
        }
        return String.valueOf(input);
    }


}
