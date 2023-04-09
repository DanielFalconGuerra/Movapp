package com.example.movapp.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movapp.R;
import com.example.movapp.databinding.FragmentMovieDataBinding;
import com.example.movapp.models.MovieInformation;
import com.example.movapp.viewmodel.MoviesViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class MovieData extends Fragment {
    MoviesViewModel model;
    private FragmentMovieDataBinding binding;
    static SharedPreferences sharedPreferences;
    public MovieData() {
        // Required empty public constructor
    }


    public static MovieData newInstance(String param1, String param2) {
        MovieData fragment = new MovieData();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_data, container, false);
        model = new ViewModelProvider(requireActivity()).get(MoviesViewModel.class);

        if(getActivity() != null)
            sharedPreferences = getActivity().getSharedPreferences("movies", Context.MODE_PRIVATE);

        if (getArguments() != null) {
            String id = getArguments().getString("id");
            if(isAdded()) {
                model.getMovieInformation(id, requireActivity());
                model.movieInformationLiveData(id).observe(requireActivity(), movieInformation -> {
                    if (movieInformation != null) {
                        binding.tvTitleMovie.setText(movieInformation.title);
                        binding.tvOverviewMovie.setText(movieInformation.overview);
                        binding.tvOverviewMovie.setMovementMethod(new ScrollingMovementMethod());
                        if(isAdded()) {
                            Glide
                                    .with(requireActivity())
                                    .load("https://image.tmdb.org/t/p/w500/" + movieInformation.backdrop_path)
                                    .into(binding.ivPosterMovie);
                        }

                        binding.rbVoteAverage.setRating((float) movieInformation.vote_average / 2);
                    } else {
                        loadOfflineInformation(id);
                    }
                });
            }
            binding.btnLoadOfflineInformation.setOnClickListener(view -> {
                loadOfflineInformation(id);
            });
        }
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_movie_data, container, false);
    }

    public MovieInformation getMovieData(int id){
        MovieInformation arrayItems;
        String serializedObject = sharedPreferences.getString("movie-"+id, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<MovieInformation>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            return arrayItems;
        }
        return null;
    }

    public void loadOfflineInformation(String id){
        MovieInformation data = getMovieData(Integer.parseInt(id));
        if (data != null) {
            model.movieInformationMutableLiveData.setValue(data);
        } else if (isAdded()) {
            Toast.makeText(requireActivity(), "No hay datos guardados para mostrar", Toast.LENGTH_SHORT).show();
        }
    }

}