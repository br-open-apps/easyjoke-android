package com.divpix.easyjoke.services;

import com.divpix.easyjoke.models.Category;
import com.divpix.easyjoke.models.Joke;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("categories")
    Call<ListResponse<Category>> getCategories();

    @GET("categories/{id}")
    Call<Response<Category>> getCategory(@Path("id") long id);

    @GET("categories/{id}/jokes")
    Call<ListResponse<Joke>> getJokesFromCategory(
            @Path("id") long id,
            @Query("page") Integer page);

    @GET("jokes")
    Call<ListResponse<Joke>> getJokes();

    @GET("jokes/{id}")
    Call<Response<Joke>> getJoke(@Path("id") long id);
}
