package com.mountzoft.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTextView;
    ImageView mImageView;
    MyRecyclerViewClickListener itemClickListener;

    MyRecyclerViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.tv_item);
        mImageView = itemView.findViewById(R.id.recipe_background);
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
