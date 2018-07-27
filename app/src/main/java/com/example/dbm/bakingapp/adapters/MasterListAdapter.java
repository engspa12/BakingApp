package com.example.dbm.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private boolean mTabletModeEnabled;

    public interface ListItemDetailClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.item_view,parent,shouldAttachToParentImmediately);

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
                             boolean tabletMode,List<RecipeIngredient> ingredients){
        listOfSteps = steps;
        mNumberOfSteps = numberOfItems;
        mOnClickListener = listener;
        mContext = context;
        mTabletModeEnabled = tabletMode;
        listOfIngredients = ingredients;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView stepTextView;

        public StepViewHolder(View itemView) {
            super(itemView);

            stepTextView = (TextView) itemView.findViewById(R.id.item_tv) ;

            itemView.setOnClickListener(this);
        }

        public void bind(int listIndex){

            if(listIndex == 0){
                StringBuilder builder = new StringBuilder();
                builder.append("INGREDIENTS: " + "\n\n");

                for(int i=0;i<listOfIngredients.size();i++){
                    if(listOfIngredients.get(i).getmIngredientMeasure().equals("UNIT")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("TSP")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " teaspoons of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("TBLSP")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " tablespoons of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("CUPS")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("OZ")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " ounces of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("K")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " kilograms of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("G")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " grams of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else if(listOfIngredients.get(i).getmIngredientMeasure().equals("CUP")){
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " cups of "
                                + listOfIngredients.get(i).getmIngredientName() + "\n");
                    } else{
                        builder.append("- " + getCorrectValue(listOfIngredients.get(i).getmIngredientQuantity()) + " "
                                + listOfIngredients.get(i).getmIngredientMeasure() + " of " + listOfIngredients.get(i).getmIngredientName() + "\n");
                    }
                }

                stepTextView.setText(builder.toString());
                stepTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                stepTextView.setGravity(Gravity.START);
                stepTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecipeIngredients));
            } else {
                //if(listIndex - 1 < listOfSteps.size()) {
                    stepTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                    stepTextView.setGravity(Gravity.CENTER);
                    setStepTitle(listIndex - 1);
                    stepTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecipeSteps));
                //}
            }

            //if(mTabletModeEnabled) {
                //DisplayMetrics displayMetrics = new DisplayMetrics();
                //((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                //int height = displayMetrics.heightPixels;
                //recipeNameTextView.setHeight(height / 2 - 164);
            //}
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        public void setStepTitle(int index){
            String title = listOfSteps.get(index).getmStepShortDescription();
            if(title.contains(".")){
                stepTextView.setText(title.substring(0,title.length() - 1));
            } else{
                stepTextView.setText(title);
            }
        }
    }

    public String getCorrectValue(double input){
        if(input % 1 == 0){
            return String.valueOf((int) input);
        }
        return String.valueOf(input);
    }



}
