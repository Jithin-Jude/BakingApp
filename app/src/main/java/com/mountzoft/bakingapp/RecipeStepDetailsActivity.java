package com.mountzoft.bakingapp;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class RecipeStepDetailsActivity extends AppCompatActivity {

    String RECIPE_POSITION = "RECIPE_POSITION";
    static boolean continuePlayback;
    static long mResumePosition;


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

    ProgressBar mProgressbar;
    TextView mTextView;
    ActionBar mActionBar;
    Button prevBtn;
    Button nextBtn;
    ImageView mImageView;

    static int stepPosition;
    int recipePosition;
    int nextPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isLandScape()){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_recipe_step_details);

        recipePosition = getIntent().getExtras().getInt(RECIPE_POSITION);

        nextPosition = stepPosition;

        setTitle(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getShortDescription());

        prevBtn = findViewById(R.id.previous_button);
        nextBtn = findViewById(R.id.next_button);
        mTextView = findViewById(R.id.tv_step_detials);
        mImageView =findViewById(R.id.no_video);
        mActionBar = getSupportActionBar();

        if(isLandScape()){
            mActionBar.hide();
            prevBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
        }else {
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            mTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getDescription());
        }


        mProgressbar = findViewById(R.id.video_loading_bar);
        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getVideoURL() != ""){
            mProgressbar.setVisibility(View.VISIBLE);
        }else{
            mProgressbar.setVisibility(View.GONE);
        }
        if(!continuePlayback) {
            initializeExoPlayer(nextPosition);
        }
    }
//==================================================================================================
    @Override
    public void onResume() {
        super.onResume();
        if(continuePlayback) {
            initializeExoPlayer(nextPosition);
            //Toast.makeText(this, "onResume continuePlayback reached", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayerView != null && player != null) {
            mResumePosition = Math.max(0, player.getCurrentPosition());
            continuePlayback = true;
            player.stop();
            player.release();
            //Toast.makeText(this, "onPause position saved : "+mResumePosition, Toast.LENGTH_SHORT).show();
        }
    }
//==================================================================================================
    private void initializeExoPlayer(int currentPosition){

        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(currentPosition).getVideoURL() == ""){
            bandwidthMeter = new DefaultBandwidthMeter();
            videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

            return;
        }else {
            bandwidthMeter = new DefaultBandwidthMeter();
            videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            simpleExoPlayerView = new SimpleExoPlayerView(this);
            simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);

            simpleExoPlayerView.setUseController(true);
            simpleExoPlayerView.requestFocus();

            simpleExoPlayerView.setPlayer(player);

            mp4VideoUri = Uri.parse(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(currentPosition).getVideoURL());

            bandwidthMeterA = new DefaultBandwidthMeter();
            dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
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
            if(continuePlayback) {
                //Toast.makeText(this, "seek to : "+mResumePosition, Toast.LENGTH_SHORT).show();
                player.seekTo(mResumePosition);
            }
            player.setPlayWhenReady(true);
        }
    }

    public void nextBtn(View view){
        continuePlayback = false;
        if(nextPosition+1 == RecipeActivity.recipeDataList.get(recipePosition).getSteps().size()){
            Toast.makeText(this, R.string.no_next_step,
                    Toast.LENGTH_LONG).show();
            return;
        }

        player.stop();
        nextPosition = nextPosition+1;
        stepPosition = nextPosition;
        initializeExoPlayer(nextPosition);
        mTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getDescription());
        setTitle(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getShortDescription());
        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getVideoURL() == ""){
            mImageView.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
        }else{
            mImageView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            mProgressbar.setVisibility(View.VISIBLE);
        }
    }

    public void preBtn(View view){
        continuePlayback = false;
        if(nextPosition-1 == -1){
            Toast.makeText(this, R.string.no_previous_step,
                    Toast.LENGTH_LONG).show();
            return;
        }

        player.stop();
        nextPosition = nextPosition-1;
        stepPosition = nextPosition;
        initializeExoPlayer(nextPosition);
        mTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getDescription());
        setTitle(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getShortDescription());
        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getVideoURL() == ""){
            mImageView.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
        }else{
            mImageView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            mProgressbar.setVisibility(View.VISIBLE);
        }
    }

    private boolean isLandScape(){
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int rotation = display.getRotation();

        if (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
    }
}
