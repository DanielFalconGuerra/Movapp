package com.example.movapp.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movapp.models.MovieInformation;
import com.example.movapp.models.MoviesRoot;
import com.example.movapp.repositories.Api;
import com.example.movapp.repositories.Retrofit;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesViewModel extends ViewModel {
    public MutableLiveData<MoviesRoot> topMoviesRootMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MoviesRoot> nowPlayingRootMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MoviesRoot> fullTopMoviesRootMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MoviesRoot> fullNowPlayingRootMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MovieInformation> movieInformationMutableLiveData = new MutableLiveData<>();

    public LiveData<MoviesRoot> topMoviesRootLiveData(int page){
        getTopMovies(page);
        return topMoviesRootMutableLiveData;
    }

    public LiveData<MoviesRoot> nowPlayingRootLiveData(int page){
        getNowPlaying(page);
        return nowPlayingRootMutableLiveData;
    }

    public LiveData<MovieInformation> movieInformationLiveData(String id){
        //getMovieInformation(id);
        return movieInformationMutableLiveData;
    }

    public void getTopMovies(int page){
        Api api = Retrofit.create();
        Call<MoviesRoot> call = api.getTopMovies("ce8c405220ac4695d6b35a1070297c4c", "es-MX", page);
        call.enqueue(new Callback<MoviesRoot>() {
            @Override
            public void onResponse(Call<MoviesRoot> call, Response<MoviesRoot> response) {
                topMoviesRootMutableLiveData.setValue(response.body());
                fullTopMoviesRootMutableLiveData.setValue(response.body());

            }

            @Override
            public void onFailure(Call<MoviesRoot> call, Throwable t) {
                Log.e("topMovies", t.getMessage());
                topMoviesRootMutableLiveData.setValue(null);
                fullTopMoviesRootMutableLiveData.setValue(null);
            }
        });
    }

    public void getNowPlaying(int page){
        Api api = Retrofit.create();
        Call<MoviesRoot> call = api.getNowPlaying("ce8c405220ac4695d6b35a1070297c4c", "es-MX", page);
        call.enqueue(new Callback<MoviesRoot>() {
            @Override
            public void onResponse(Call<MoviesRoot> call, Response<MoviesRoot> response) {
                nowPlayingRootMutableLiveData.setValue(response.body());
                fullNowPlayingRootMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MoviesRoot> call, Throwable t) {
                Log.e("topMovies", t.getMessage());
                nowPlayingRootMutableLiveData.setValue(null);
                fullNowPlayingRootMutableLiveData.setValue(null);
            }
        });
    }

    public void getMovieInformation(String id, Context context){
        Api api = Retrofit.create();
        Call<MovieInformation> call = api.getMovieDetails(id, "ce8c405220ac4695d6b35a1070297c4c", "es-MX");
        call.enqueue(new Callback<MovieInformation>() {
            @Override
            public void onResponse(Call<MovieInformation> call, Response<MovieInformation> response) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("movies", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                prefsEditor.putString("movie-"+id, json);
                prefsEditor.apply();
                movieInformationMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieInformation> call, Throwable t) {
                Log.e("MovieInformation", t.getMessage());
                movieInformationMutableLiveData.setValue(null);
            }
        });
    }

    public void searchMovie(String search){
        MoviesRoot movies = new MoviesRoot();
        if(topMoviesRootMutableLiveData.getValue() != null) {
            movies.page = topMoviesRootMutableLiveData.getValue().page;
            movies.total_pages = topMoviesRootMutableLiveData.getValue().total_pages;
            movies.total_results = topMoviesRootMutableLiveData.getValue().total_results;
            movies.results = new ArrayList<>();
            for (int movie = 0; movie < topMoviesRootMutableLiveData.getValue().results.size(); movie++) {
                if (topMoviesRootMutableLiveData.getValue().results.get(movie).original_title.contains(search)) {
                    movies.results.add(topMoviesRootMutableLiveData.getValue().results.get(movie));
                }
            }
            topMoviesRootMutableLiveData.setValue(movies);
        }

        if(nowPlayingRootMutableLiveData.getValue() != null) {
            movies.page = nowPlayingRootMutableLiveData.getValue().page;
            movies.total_pages = nowPlayingRootMutableLiveData.getValue().total_pages;
            movies.total_results = nowPlayingRootMutableLiveData.getValue().total_results;
            movies.results = new ArrayList<>();
            for (int movie = 0; movie < nowPlayingRootMutableLiveData.getValue().results.size(); movie++) {
                if (nowPlayingRootMutableLiveData.getValue().results.get(movie).original_title.contains(search)) {
                    movies.results.add(nowPlayingRootMutableLiveData.getValue().results.get(movie));
                }
            }
            nowPlayingRootMutableLiveData.setValue(movies);
        }
    }
}
