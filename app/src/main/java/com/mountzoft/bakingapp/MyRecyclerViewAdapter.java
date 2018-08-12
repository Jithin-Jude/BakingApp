package com.mountzoft.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JithinJude on 15-03-2018.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {
    String RECIPE_POSITION;

    private int[] item = {
            R.drawable.nutella_pie,
            R.drawable.brownies,
            R.drawable.yellow_cake,
            R.drawable.cheesecake,
    };

    private LayoutInflater mInflater;
    Context mContext;

    List<Ingredient> mIngredients;

    private ArrayList<RetroRecipeData> recipeDataList;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<RetroRecipeData> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.recipeDataList = data;


        RECIPE_POSITION = mContext.getString(R.string.recipe_position);
    }

    // inflates the row layout from xml when needed
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipe_recyclerview_row, parent, false);
        return new MyRecyclerViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyRecyclerViewHolder viewHolder, int position) {
        RetroRecipeData animal = recipeDataList.get(position);
        viewHolder.mTextView.setText(recipeDataList.get(position).getName());
        String imageUrl=recipeDataList.get(position).getImage();
        viewHolder.mImageView.setImageResource(item[position]);

        viewHolder.setItemClickListener(new MyRecyclerViewClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(mContext, "You clicked " + getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.recipe_app_widget);
                ComponentName thisWidget = new ComponentName(mContext, RecipeAppWidget.class);

                mIngredients = RecipeActivity.recipeDataList.get(position).getIngredients();
                String widgetText = "";
                widgetText += RecipeActivity.recipeDataList.get(position).getName()+" : Ingredients\n\n";
                for(int i=0; i<RecipeActivity.recipeDataList.size(); i++){
                    widgetText+= mIngredients.get(i).getIngredient()+"\n";
                }
                remoteViews.setTextViewText(R.id.appwidget_text, widgetText);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

                boolean isPhone = mContext.getResources().getBoolean(R.bool.is_phone);
                if (isPhone) {
                    Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                    intent.putExtra(RECIPE_POSITION,position);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, RecipeDetailsActivityTablet.class);
                    intent.putExtra(RECIPE_POSITION,position);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return recipeDataList.size();
    }
}
