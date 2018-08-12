package com.mountzoft.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeStepRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTextView;
    MyRecyclerViewClickListener itemClickListener;

    RecipeStepRecyclerViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.tv_recipe_detials_step);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }

    void setItemClickListener(MyRecyclerViewClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
