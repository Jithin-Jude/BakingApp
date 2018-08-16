package com.mountzoft.bakingapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeDetailsActivityTablet extends AppCompatActivity {

    String RECIPE_POSITION = "RECIPE_POSITION";

    LinearLayoutManager mLinearLayoutManager;
    RecipeStepRecyclerViewAdapter adapter;

    RecyclerView mRecipeRecyclerView;

    static int recipePosition;
    static int stepPosition;
    List<Ingredient> mIngredients;


    TextView mTextView;
    static TextView descriptionTextView;
    static ImageView mImageView;
    static ProgressBar mProgressbar;

    private static SimpleExoPlayerView simpleExoPlayerView;
    private static SimpleExoPlayer player;
    static BandwidthMeter bandwidthMeter;
    static TrackSelection.Factory videoTrackSelectionFactory;
    static TrackSelector trackSelector;
    static LoadControl loadControl;
    static Uri mp4VideoUri;
    static DefaultBandwidthMeter bandwidthMeterA;
    static DefaultDataSourceFactory dataSourceFactory;
    static ExtractorsFactory extractorsFactory;
    static MediaSource videoSource;

    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details_tablet);
        mTextView = findViewById(R.id.tv_ingridents);
        mRecipeRecyclerView = findViewById(R.id.rv_recipe_step_tablet);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);

        mContext = getApplicationContext();

        descriptionTextView = findViewById(R.id.tv_step_detials);


        mProgressbar = findViewById(R.id.progressBar);
        mImageView = findViewById(R.id.no_video);

        mProgressbar.setVisibility(View.GONE);
        descriptionTextView.setVisibility(View.GONE);
        //simpleExoPlayerView.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);

        recipePosition = getIntent().getExtras().getInt(RECIPE_POSITION);
        setTitle(RecipeActivity.recipeDataList.get(recipePosition).getName());

        mIngredients = RecipeActivity.recipeDataList.get(recipePosition).getIngredients();
        for(int i =0; i<mIngredients.size(); i++){
            mTextView.append("\u2022 "+ mIngredients.get(i).getIngredient()+"\n");
            mTextView.append(getString(R.string.quantity)+mIngredients.get(i).getQuantity().toString()+"\n");
            mTextView.append(getString(R.string.measure)+mIngredients.get(i).getMeasure()+"\n\n");
        }

        descriptionTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(stepPosition).getDescription());

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecipeRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new RecipeStepRecyclerViewAdapter(this, recipePosition);
        mRecipeRecyclerView.setAdapter(adapter);
    }

    public static void stepUpdate(Context mContext){
        if(player != null)
            player.stop();

        descriptionTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(stepPosition).getDescription());

        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(stepPosition).getVideoURL() == ""){
            mImageView.setVisibility(View.VISIBLE);
            descriptionTextView.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
            return;
        }else {
            bandwidthMeter = new DefaultBandwidthMeter();
            videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);

            simpleExoPlayerView.setUseController(true);
            simpleExoPlayerView.requestFocus();

            simpleExoPlayerView.setPlayer(player);

            mp4VideoUri = Uri.parse(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(stepPosition).getVideoURL());

            bandwidthMeterA = new DefaultBandwidthMeter();
            dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "exoplayer2example"), bandwidthMeterA);
            extractorsFactory = new DefaultExtractorsFactory();

            videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);
            final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

            player.prepare(loopingSource);

            player.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int x) {
                    //Log.v(TAG, "Listener-onTimelineChanged...");
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    //Log.v(TAG, "Listener-onTracksChanged...");
                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    //Log.v(TAG, "Listener-onLoadingChanged...isLoading:"+isLoading);
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    //Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);
                    if (playbackState == ExoPlayer.STATE_BUFFERING){
                        mProgressbar.setVisibility(View.VISIBLE);
                    } else {
                        mProgressbar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    //Log.v(TAG, "Listener-onRepeatModeChanged...");
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    //Log.v(TAG, "Listener-onPlayerError...");
                }

                @Override
                public void onPositionDiscontinuity(int x) {
                    //Log.v(TAG, "Listener-onPositionDiscontinuity...");
                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                    //Log.v(TAG, "Listener-onPlaybackParametersChanged...");
                }
                @Override
                public void onSeekProcessed() {
                    //Log.v(TAG, "Listener-onPlaybackParametersChanged...");
                }
                @Override
                public void onShuffleModeEnabledChanged(boolean x) {
                    //Log.v(TAG, "Listener-onPlaybackParametersChanged...");
                }
            });

            player.setPlayWhenReady(true);
        }
    }
}
