package com.example.movapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movapp.R;
import com.example.movapp.adapters.MoviesAdapter;
import com.example.movapp.databinding.FragmentMoviesBinding;
import com.example.movapp.models.MoviesResult;
import com.example.movapp.models.MoviesRoot;
import com.example.movapp.viewmodel.MoviesViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Movies extends Fragment {

    private FragmentMoviesBinding binding;
    MoviesViewModel model;
    MoviesAdapter adapter;
    static SharedPreferences sharedPreferences;

    public Movies() {

    }
    public static Movies newInstance() {
        Movies fragment = new Movies();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);

        model = new ViewModelProvider(requireActivity()).get(MoviesViewModel.class);
        binding.rvTopRated.setHasFixedSize(true);
        LinearLayoutManager layoutManagerTop = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.rvTopRated.setLayoutManager(layoutManagerTop);

        binding.rvNowPlaying.setHasFixedSize(true);
        LinearLayoutManager layoutManagerNow = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.rvNowPlaying.setLayoutManager(layoutManagerNow);

        AtomicInteger pageTopMovie = new AtomicInteger();
        AtomicInteger pageNowPlaying = new AtomicInteger();

        pageTopMovie.set(1);
        pageNowPlaying.set(1);

        if(getActivity() != null)
            sharedPreferences = getActivity().getSharedPreferences("movies", Context.MODE_PRIVATE);

        model.topMoviesRootLiveData(pageTopMovie.get()).observe(requireActivity(), topMovies -> {
            binding.loadAnimation.setVisibility(View.GONE);
            if(topMovies != null){
                if(getActivity() != null){
                    setList("topMovies"+pageTopMovie.get(), topMovies.results);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                    prefEditor.putString("total_results_top_movies", String.valueOf(topMovies.total_results));
                    prefEditor.putString("total_pages_top_movies", String.valueOf(topMovies.total_pages));
                    prefEditor.apply();

                    pageTopMovie.set(topMovies.page);
                    adapter = new MoviesAdapter(requireActivity(), topMovies.results);
                    binding.rvTopRated.setAdapter(adapter);

                    if(pageTopMovie.get() == 1)
                        binding.btnBeforeTopMovie.setEnabled(false);
                    else
                        binding.btnBeforeTopMovie.setEnabled(true);

                    if(pageTopMovie.get() == topMovies.total_pages)
                        binding.btnNextTopMovie.setEnabled(false);
                    else
                        binding.btnNextTopMovie.setEnabled(true);

                    binding.btnBeforeTopMovie.setOnClickListener(view -> {
                        pageTopMovie.set(pageTopMovie.get() - 1);
                        model.topMoviesRootLiveData(pageTopMovie.get());
                    });

                    binding.btnNextTopMovie.setOnClickListener(view -> {
                        pageTopMovie.set(pageTopMovie.get() + 1);
                        model.topMoviesRootLiveData(pageTopMovie.get());
                    });
                }

            }else{
                List<MoviesResult> top = getTopMovies(pageTopMovie.get());
                if(top != null){
                    //adapter = new MoviesAdapter(requireActivity(), (ArrayList<MoviesResult>) top);
                    MoviesRoot moviesRoot = new MoviesRoot();
                    moviesRoot.page = pageTopMovie.get();
                    moviesRoot.total_results = Integer.parseInt(sharedPreferences.getString("total_results_top_movies", ""));
                    moviesRoot.total_pages = Integer.parseInt(sharedPreferences.getString("total_pages_top_movies", ""));
                    moviesRoot.results = (ArrayList<MoviesResult>) top;
                    model.topMoviesRootMutableLiveData.setValue(moviesRoot);
                    //binding.rvTopRated.setAdapter(adapter);
                }else{
                    if(isAdded()){
                        Toast.makeText(requireActivity(), "No hay datos guardados para mostrar", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        model.nowPlayingRootLiveData(pageNowPlaying.get()).observe(requireActivity(), nowPlaying -> {
            if(nowPlaying != null){
                pageNowPlaying.set(nowPlaying.page);

                setList("nowPlaying"+pageNowPlaying.get(), nowPlaying.results);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                prefEditor.putString("total_results_now_playing", String.valueOf(nowPlaying.total_results));
                prefEditor.putString("total_pages_now_playing", String.valueOf(nowPlaying.total_pages));
                prefEditor.apply();

                adapter = new MoviesAdapter(requireActivity(), nowPlaying.results);
                binding.rvNowPlaying.setAdapter(adapter);

                if(pageNowPlaying.get() == 1)
                    binding.btnBeforeNowPlaying.setEnabled(false);
                else
                    binding.btnBeforeNowPlaying.setEnabled(true);

                if(pageNowPlaying.get() == nowPlaying.total_pages)
                    binding.btnNextNowPlaying.setEnabled(false);
                else
                    binding.btnNextNowPlaying.setEnabled(true);

                binding.btnBeforeNowPlaying.setOnClickListener(view -> {
                    pageNowPlaying.set(pageNowPlaying.get() - 1);
                    model.nowPlayingRootLiveData(pageNowPlaying.get());
                });

                binding.btnNextNowPlaying.setOnClickListener(view -> {
                    pageNowPlaying.set(pageNowPlaying.get() + 1);
                    model.nowPlayingRootLiveData(pageNowPlaying.get());
                });

            }else{
                List<MoviesResult> now = getNowPlaying(pageNowPlaying.get());
                if(now != null){
                    //adapter = new MoviesAdapter(requireActivity(), (ArrayList<MoviesResult>) top);
                    MoviesRoot moviesRoot = new MoviesRoot();
                    moviesRoot.page = pageNowPlaying.get();
                    moviesRoot.total_results = Integer.parseInt(sharedPreferences.getString("total_results_now_playing", ""));
                    moviesRoot.total_pages = Integer.parseInt(sharedPreferences.getString("total_pages_now_playing", ""));
                    moviesRoot.results = (ArrayList<MoviesResult>) now;
                    model.nowPlayingRootMutableLiveData.setValue(moviesRoot);
                    //binding.rvTopRated.setAdapter(adapter);
                }else{
                    if(isAdded()){
                        Toast.makeText(requireActivity(), "No hay datos guardados para mostrar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.etSearchMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                model.topMoviesRootMutableLiveData.setValue(model.fullTopMoviesRootMutableLiveData.getValue());
                model.nowPlayingRootMutableLiveData.setValue(model.fullNowPlayingRootMutableLiveData.getValue());
                model.searchMovie(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_movies, container, false);
    }
    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json);
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public List<MoviesResult> getTopMovies(int page){
        List<MoviesResult> arrayItems;
        String serializedObject = sharedPreferences.getString("topMovies"+page, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<MoviesResult>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            return arrayItems;
        }
        return null;
    }

    public List<MoviesResult> getNowPlaying(int page){
        List<MoviesResult> arrayItems;
        String serializedObject = sharedPreferences.getString("nowPlaying"+page, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<MoviesResult>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            return arrayItems;
        }
        return null;
    }
}