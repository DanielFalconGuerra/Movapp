package com.example.movapp.repositories;

import com.example.movapp.models.MovieInformation;
import com.example.movapp.models.MoviesRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://api.themoviedb.org";

    @GET("/3/movie/top_rated") //Get Top Movies
    Call<MoviesRoot> getTopMovies(@Query("api_key") String api_key, @Query("language") String language, @Query("page") int page);

    @GET("/3/movie/now_playing") //Get Top Movies
    Call<MoviesRoot> getNowPlaying(@Query("api_key") String api_key, @Query("language") String language, @Query("page") int page);

    @GET("/3/movie/{id}") //Get Movie information
    Call<MovieInformation> getMovieDetails(@Path(value = "id", encoded = true) String id, @Query("api_key") String api_key, @Query("language") String language);
}
