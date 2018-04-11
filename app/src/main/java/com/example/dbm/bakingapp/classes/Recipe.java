package com.example.dbm.bakingapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {

    private int mRecipeId;
    private String mRecipeName;
    private List<RecipeIngredient> mRecipeIngredients;
    private List<RecipeStep> mRecipeSteps;
    private int mRecipeServings;
    private String mRecipeImage;

    public Recipe(int id, String name, List<RecipeIngredient> ingredients,List<RecipeStep> steps, int servings, String image){
        mRecipeId = id;
        mRecipeName = name;
        mRecipeIngredients = ingredients;
        mRecipeSteps = steps;
        mRecipeServings = servings;
        mRecipeImage = image;
    }

    private Recipe(Parcel in) {
        mRecipeId = in.readInt();
        mRecipeName = in.readString();
    //    in.readTypedList(mRecipeIngredients, RecipeIngredient.CREATOR);
    //    in.readTypedList(mRecipeSteps, RecipeStep.CREATOR);
        mRecipeServings = in.readInt();
        mRecipeImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mRecipeId);
        out.writeString(mRecipeName);
     //   out.writeTypedList(mRecipeIngredients);
    //   out.writeTypedList(mRecipeSteps);
        out.writeInt(mRecipeServings);
        out.writeString(mRecipeImage);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getmRecipeId(){
        return mRecipeId;
    }

    public String getmRecipeName(){
        return mRecipeName;
    }

    public List<RecipeStep> getmRecipeSteps(){return mRecipeSteps;}

    public List<RecipeIngredient> getmRecipeIngredients(){return mRecipeIngredients;}

    public int getmRecipeServings(){
        return mRecipeServings;
    }

    public String getmRecipeImage(){
        return mRecipeImage;
    }
}
