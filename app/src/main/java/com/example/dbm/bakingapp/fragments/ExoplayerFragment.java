package com.example.dbm.bakingapp.fragments;

import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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

import java.util.ArrayList;
import java.util.List;

public class ExoplayerFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private static final String STEPS_LIST = "list";

    private static final String STEPS_LIST_INDEX = "index";

    private static final String PLAYER_POSITION = "player_position";

    //private static final String TEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";

    private int mListStepIndex;

    private List<RecipeStep> mListSteps;

    private String media;

    private long mPosition;

    private ImageView emptyImageView;
    private TextView noVideoTextView;

    private static final String LOG = ExoplayerFragment.class.getSimpleName();

    public ExoplayerFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exoplayer, container, false);

        noVideoTextView = (TextView) rootView.findViewById(R.id.no_video_text_view);
        noVideoTextView.setText("THERE IS NO VIDEO FOR THIS STEP, PLEASE FOLLOW INSTRUCTIONS BELOW");
        noVideoTextView.setVisibility(View.GONE);

        emptyImageView = (ImageView) rootView.findViewById(R.id.empty_image_view);
        emptyImageView.setVisibility(View.GONE);

        if(savedInstanceState != null){
            mPosition = savedInstanceState.getLong(PLAYER_POSITION);
        }

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        if(mListSteps != null) {
            media = mListSteps.get(mListStepIndex).getmStepVideoUrl();
        } else{
            mListSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mListStepIndex = savedInstanceState.getInt(STEPS_LIST_INDEX);
            media = mListSteps.get(mListStepIndex).getmStepVideoUrl();
        }

        if (media != null && !(media.equals(""))){
            initializePlayer(Uri.parse(media));
        } else{
            if(mListSteps.get(mListStepIndex).getmStepThumbnailUrl().equals("")) {
                emptyImageView.setVisibility(View.VISIBLE);
                noVideoTextView.setVisibility(View.VISIBLE);
            } else{

                noVideoTextView.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    public void setStepsList(List<RecipeStep> list){
        mListSteps = list;
    }

    public void setStepIndex(int index){
        mListStepIndex = index;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
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
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            if (mPosition != C.TIME_UNSET && mExoPlayer != null) {
                mExoPlayer.seekTo(mPosition);
            }
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
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
        //mNotificationManager.cancelAll();
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        //mMediaSession.setActive(false);
    }

    public void updatePlayer() {
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<RecipeStep>) mListSteps);
        currentState.putInt(STEPS_LIST_INDEX, mListStepIndex);
        if(mExoPlayer != null) {
            currentState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
        }
    }


}
