package com.example.dbm.bakingapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.classes.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private static final String LOG = RecipeAdapter.class.getSimpleName();

    private final ListItemClickListener mOnClickListener;

    private int mNumberOfRecipes;

    private List<Recipe> listOfRecipes;

    private Context mContext;

    private boolean mTabletModeEnabled;

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_view,parent,false);

        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberOfRecipes;
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(List<Recipe> recipes, int numberOfItems, ListItemClickListener listener, Context context, boolean tabletMode){
        listOfRecipes = recipes;
        mNumberOfRecipes = numberOfItems;
        mOnClickListener = listener;
        mContext = context;
        mTabletModeEnabled = tabletMode;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeNameTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            recipeNameTextView = (TextView) itemView.findViewById(R.id.item_tv) ;

            itemView.setOnClickListener(this);
        }

        public void bind(int listIndex){

            recipeNameTextView.setText(listOfRecipes.get(listIndex).getmRecipeName());
            recipeNameTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorRecipe));

            if(mTabletModeEnabled) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                recipeNameTextView.setHeight(height / 2 - 164);
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
