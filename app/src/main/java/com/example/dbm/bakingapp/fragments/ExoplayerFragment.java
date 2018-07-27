package com.example.dbm.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dbm.bakingapp.R;
import com.example.dbm.bakingapp.classes.RecipeStep;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExoplayerFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private static final String STEPS_LIST = "list";

    private static final String STEPS_LIST_INDEX = "index";

    private static final String PLAYER_POSITION = "player_position";
    private static final String PLAYER_STATE = "player_state";


    private int mListStepIndex;

    private List<RecipeStep> mListSteps;

    private boolean mPlayerState;

    private String media;

    private long mPosition;

    private ImageView emptyImageView;
    private TextView noVideoTextView;


    private static final String LOG = ExoplayerFragment.class.getSimpleName();

    public ExoplayerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exoplayer, container, false);


        noVideoTextView = (TextView) rootView.findViewById(R.id.no_video_text_view);
        emptyImageView = (ImageView) rootView.findViewById(R.id.empty_image_view);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);


        noVideoTextView.setText(getString(R.string.no_video_or_image_message));


        noVideoTextView.setVisibility(View.GONE);
        emptyImageView.setVisibility(View.GONE);


        mPlayerState = false;

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getLong(PLAYER_POSITION);
            mPlayerState = savedInstanceState.getBoolean(PLAYER_STATE);
        }

        if (mListSteps != null) {
            media = mListSteps.get(mListStepIndex).getmStepVideoUrl();
        } else {
            mListSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mListStepIndex = savedInstanceState.getInt(STEPS_LIST_INDEX);
            media = mListSteps.get(mListStepIndex).getmStepVideoUrl();
        }

        return rootView;
    }

    public void checkVideoResources(){
        if (media != null && !(media.equals(""))) {
            initializePlayer(Uri.parse(media));
        } else {
            if (mListSteps.get(mListStepIndex).getmStepThumbnailUrl().equals("")) {
                emptyImageView.setVisibility(View.VISIBLE);
                emptyImageView.setImageResource(R.drawable.read_below_image);
                noVideoTextView.setVisibility(View.VISIBLE);
            } else {
                Picasso.get()
                        .load(mListSteps.get(mListStepIndex).getmStepThumbnailUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.read_below_image)
                        .into(emptyImageView);
                emptyImageView.setVisibility(View.VISIBLE);
                noVideoTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setStepsList(List<RecipeStep> list) {
        mListSteps = list;
    }

    public void setStepIndex(int index) {
        mListStepIndex = index;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            checkVideoResources();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            checkVideoResources();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null) {
            mPosition = mExoPlayer.getCurrentPosition();
            mPlayerState = mExoPlayer.getPlayWhenReady();
        }
    }

   @Override
   public void onStop() {
       super.onStop();
            releasePlayer();
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            if (mPosition != C.TIME_UNSET && mExoPlayer != null) {
                mExoPlayer.seekTo(mPosition);
            }
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mPlayerState);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<RecipeStep>) mListSteps);
        currentState.putInt(STEPS_LIST_INDEX, mListStepIndex);
        if (mExoPlayer != null) {
            currentState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
            currentState.putBoolean(PLAYER_STATE, mExoPlayer.getPlayWhenReady());
        }
    }


}
