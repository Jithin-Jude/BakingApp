package com.mountzoft.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.mountzoft.bakingapp.RecipeStepDetailsActivity.continuePlayback;

/**
 * Created by JithinJude on 15-03-2018.
 */

public class RecipeDetailsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeDetailsRecyclerViewHolder> {

    String RECIPE_POSITION = "RECIPE_POSITION";

    List<Step> mSteps;
    private LayoutInflater mInflater;
    Context mContext;
    int recipePosition;

    // data is passed into the constructor
    RecipeDetailsRecyclerViewAdapter(Context context, int position) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.recipePosition = position;
        this.mSteps = RecipeActivity.recipeDataList.get(position).getSteps();
    }

    // inflates the row layout from xml when needed
    @Override
    public RecipeDetailsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipe_details_recyclerview_row, parent, false);
        return new RecipeDetailsRecyclerViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecipeDetailsRecyclerViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(mSteps.get(position).getId()+". "+ mSteps.get(position).getShortDescription());

        viewHolder.setItemClickListener(new MyRecyclerViewClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(mContext, "You clicked " + getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, RecipeStepDetailsActivity.class);
                intent.putExtra(RECIPE_POSITION,recipePosition);
                RecipeStepDetailsActivity.stepPosition = position;
                continuePlayback = false;
                mContext.startActivity(intent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mSteps.size();
    }
}
