package com.example.movapp.repositories;

import static com.example.movapp.repositories.Api.BASE_URL;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    public static Api create(){
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(Api.class);
    }
}
