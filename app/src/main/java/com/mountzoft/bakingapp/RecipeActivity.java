package com.mountzoft.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {

    public static ArrayList<RetroRecipeData> recipeDataList;

    LinearLayoutManager mLinearLayoutManager;
    GridAutofitLayoutManager mGridAutofitLayoutManager;
    MyRecyclerViewAdapter adapter;
    RecyclerView mRecipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        RetroGetRecipeData service = RetrofitClientInstance.getRetrofitInstance().create(RetroGetRecipeData.class);
        Call<ArrayList<RetroRecipeData>> call = service.getRecipeData();
        call.enqueue(new Callback<ArrayList<RetroRecipeData>>() {
            @Override
            public void onResponse(Call<ArrayList<RetroRecipeData>> call, Response<ArrayList<RetroRecipeData>> response) {
                recipeDataList = response.body();
                generateDataList(recipeDataList);
            }

            @Override
            public void onFailure(Call<ArrayList<RetroRecipeData>> call, Throwable t) {
                Toast.makeText(RecipeActivity.this, R.string.network_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(ArrayList<RetroRecipeData> recipeDataList) {
        mRecipeRecyclerView = findViewById(R.id.rv_recipe);

        boolean isPhone = getResources().getBoolean(R.bool.is_phone);

        if (isPhone) {
            mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecipeRecyclerView.setLayoutManager(mLinearLayoutManager);
        } else {
            mGridAutofitLayoutManager = new GridAutofitLayoutManager(getApplicationContext(), 400);
            mRecipeRecyclerView.setLayoutManager(mGridAutofitLayoutManager);
        }

        adapter = new MyRecyclerViewAdapter(this, recipeDataList);
        mRecipeRecyclerView.setAdapter(adapter);
    }
}
