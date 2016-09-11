package com.alexandremota.easyjoke_android.services;

import com.alexandremota.easyjoke_android.models.Category;
import com.alexandremota.easyjoke_android.models.Joke;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("categories")
    Call<ListResponse<Category>> getCategories();

    @GET("categories/{id}")
    Call<Response<Category>> getCategory(@Path("id") int categoryId);

    @GET("categories/{id}/jokes")
    Call<ListResponse<Joke>> getJokesFromCategory(@Path("id") int categoryId);

    @GET("jokes")
    Call<ListResponse<Joke>> getJokes();

    @GET("jokes/{id}")
    Call<Response<Joke>> getJoke(@Path("id") int jokeId);
}
