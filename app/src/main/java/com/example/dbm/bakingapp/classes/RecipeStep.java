package com.example.dbm.bakingapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeStep implements Parcelable {

    private int mStepId;
    private String mStepShortDescription;
    private String mStepDescription;
    private String mStepVideoUrl;
    private String mStepThumbnailUrl;

    public RecipeStep(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl){
        mStepId = id;
        mStepShortDescription = shortDescription;
        mStepDescription = description;
        mStepVideoUrl = videoUrl;
        mStepThumbnailUrl = thumbnailUrl;
    }

    private RecipeStep(Parcel in) {
        mStepId = in.readInt();
        mStepShortDescription = in.readString();
        mStepDescription = in.readString();
        mStepVideoUrl = in.readString();
        mStepThumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mStepId);
        out.writeString(mStepShortDescription);
        out.writeString(mStepDescription);
        out.writeString(mStepVideoUrl);
        out.writeString(mStepThumbnailUrl);
    }

    public int getmStepId(){return mStepId;}

    public String getmStepShortDescription(){return mStepShortDescription;}

    public String getmStepDescription(){return mStepDescription;}

    public String getmStepVideoUrl(){return mStepVideoUrl;}

    public String getmStepThumbnailUrl(){return mStepThumbnailUrl;}
}
