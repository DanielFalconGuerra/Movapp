package com.example.movapp.repositories;

import com.example.movapp.models.TopMoviesRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://api.themoviedb.org";

    @GET("/3/movie/top_rated") //Get Top Movies
    Call<TopMoviesRoot> getTopMovies(@Query("api_key") String api_key, @Query("language") String language, @Query("page") int page);
}
