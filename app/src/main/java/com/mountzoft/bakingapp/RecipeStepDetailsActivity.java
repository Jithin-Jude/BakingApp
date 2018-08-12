package com.mountzoft.bakingapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class RecipeStepDetailsActivity extends AppCompatActivity {

    String RECIPE_POSITION = "RECIPE_POSITION";

    Uri videoUri;
    MediaController mediaController;
    ProgressBar mProgressbar;
    VideoView mVideoView;
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
        mVideoView =(VideoView)findViewById(R.id.videoView);
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

        playVideo(nextPosition);
    }

    private void playVideo(int currentPosition){
        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(currentPosition).getVideoURL() == ""){
            return;
        }else {
            mVideoView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            mediaController = new MediaController(this);
            mediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mediaController);
            videoUri = Uri.parse(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(currentPosition).getVideoURL());
            mVideoView.setVideoURI(videoUri);
            mVideoView.requestFocus();

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                            mProgressbar.setVisibility(View.GONE);
                            mp.start();
                        }
                    });


                }
            });
        }
    }

    public void nextBtn(View view){
        if(nextPosition+1 == RecipeActivity.recipeDataList.get(recipePosition).getSteps().size()){
            Toast.makeText(this, R.string.no_next_step,
                    Toast.LENGTH_LONG).show();
            return;
        }

        nextPosition = nextPosition+1;
        stepPosition = nextPosition;
        playVideo(nextPosition);
        mTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getDescription());
        setTitle(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getShortDescription());
        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getVideoURL() == ""){
            mImageView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
        }else{
            mImageView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mProgressbar.setVisibility(View.VISIBLE);
        }
    }

    public void preBtn(View view){
        if(nextPosition-1 == -1){
            Toast.makeText(this, R.string.no_previous_step,
                    Toast.LENGTH_LONG).show();
            return;
        }

        nextPosition = nextPosition-1;
        stepPosition = nextPosition;
        playVideo(nextPosition);
        mTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getDescription());
        setTitle(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getShortDescription());
        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(nextPosition).getVideoURL() == ""){
            mImageView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
        }else{
            mImageView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
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

}
