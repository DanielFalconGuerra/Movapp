package com.example.movapp.viewmodel;

import static com.example.movapp.repositories.Api.BASE_URL;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movapp.models.TopMoviesRoot;
import com.example.movapp.repositories.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesViewModel extends ViewModel {
    MutableLiveData<TopMoviesRoot> topMoviesRootMutableLiveData = new MutableLiveData<>();
    public LiveData<TopMoviesRoot> topMoviesRootLiveData(){
        getTopMovies();
        return topMoviesRootMutableLiveData;
    }
    public void getTopMovies(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<TopMoviesRoot> call = api.getTopMovies("ce8c405220ac4695d6b35a1070297c4c", "es-MX", 1);
        call.enqueue(new Callback<TopMoviesRoot>() {
            @Override
            public void onResponse(Call<TopMoviesRoot> call, Response<TopMoviesRoot> response) {
                topMoviesRootMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TopMoviesRoot> call, Throwable t) {
                Log.e("topMovies", t.getMessage());
                topMoviesRootMutableLiveData.setValue(null);
            }
        });
    }

}
