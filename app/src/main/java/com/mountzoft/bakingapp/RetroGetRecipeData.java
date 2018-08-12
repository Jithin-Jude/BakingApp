package com.mountzoft.bakingapp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetroGetRecipeData {

    @GET("baking.json")
    Call<ArrayList<RetroRecipeData>> getRecipeData();
}