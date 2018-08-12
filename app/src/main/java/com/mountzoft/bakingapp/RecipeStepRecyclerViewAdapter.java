package com.mountzoft.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.mountzoft.bakingapp.RecipeDetailsActivityTablet.stepUpdate;

/**
 * Created by JithinJude on 15-03-2018.
 *
 * FOR TABLETS======================================================================================
 */

public class RecipeStepRecyclerViewAdapter extends RecyclerView.Adapter<RecipeStepRecyclerViewHolder> {

    List<Step> mSteps;
    private LayoutInflater mInflater;
    Context mContext;

    // data is passed into the constructor
    RecipeStepRecyclerViewAdapter(Context context, int position) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mSteps = RecipeActivity.recipeDataList.get(position).getSteps();
    }

    // inflates the row layout from xml when needed
    @Override
    public RecipeStepRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipe_details_recyclerview_row, parent, false);
        return new RecipeStepRecyclerViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecipeStepRecyclerViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(mSteps.get(position).getId()+". "+ mSteps.get(position).getShortDescription());

        viewHolder.setItemClickListener(new MyRecyclerViewClickListener() {
            @Override
            public void onItemClick(int position) {
                RecipeDetailsActivityTablet.stepPosition = position;
                stepUpdate();
                //Toast.makeText(mContext, "You clicked on row number " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mSteps.size();
    }
}
