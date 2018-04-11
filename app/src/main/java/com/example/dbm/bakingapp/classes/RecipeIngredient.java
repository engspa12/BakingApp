package com.example.dbm.bakingapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeIngredient implements Parcelable {

    private double mIngredientQuantity;
    private String mIngredientMeasure;
    private String mIngredientName;

    public RecipeIngredient(double quantity, String measure, String name){
        mIngredientQuantity = quantity;
        mIngredientMeasure = measure;
        mIngredientName = name;
    }

    private RecipeIngredient(Parcel in) {
        mIngredientQuantity = in.readDouble();
        mIngredientMeasure = in.readString();
        mIngredientName = in.readString();
    }

    public static final Parcelable.Creator<RecipeIngredient> CREATOR = new Parcelable.Creator<RecipeIngredient>() {
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(mIngredientQuantity);
        out.writeString(mIngredientMeasure);
        out.writeString(mIngredientName);
    }

    public double getmIngredientQuantity(){return mIngredientQuantity;}

    public String getmIngredientMeasure(){return mIngredientMeasure;}

    public String getmIngredientName(){return mIngredientName;}
}
