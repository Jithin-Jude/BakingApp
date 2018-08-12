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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeDetailsActivityTablet extends AppCompatActivity {

    String RECIPE_POSITION = "RECIPE_POSITION";

    LinearLayoutManager mLinearLayoutManager;
    RecipeStepRecyclerViewAdapter adapter;

    @BindView(R.id.rv_recipe_step_tablet)
    RecyclerView mRecipeRecyclerView;

    static int recipePosition;
    static int stepPosition;
    List<Ingredient> mIngredients;

    @BindView(R.id.tv_ingridents)
    TextView mTextView;
    static TextView descriptionTextView;
    static ImageView mImageView;

    static Uri videoUri;
    static MediaController mediaController;
    static ProgressBar mProgressbar;
    static VideoView mVideoView;

    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details_tablet);

        mContext = getApplicationContext();

        descriptionTextView = findViewById(R.id.tv_step_detials);
        mVideoView =(VideoView)findViewById(R.id.videoView);
        mProgressbar = findViewById(R.id.progressBar);
        mImageView = findViewById(R.id.no_video);

        mProgressbar.setVisibility(View.GONE);
        descriptionTextView.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);
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

    public static void stepUpdate(){
        descriptionTextView.setText(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(stepPosition).getDescription());
        if(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(stepPosition).getVideoURL() == ""){
            mImageView.setVisibility(View.VISIBLE);
            descriptionTextView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
            return;
        }else {
            mProgressbar.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.VISIBLE);
            descriptionTextView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);

            mediaController = new MediaController(mContext);
            mediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mediaController);
            videoUri = Uri.parse(RecipeActivity.recipeDataList.get(recipePosition).getSteps().get(stepPosition).getVideoURL());
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
}
