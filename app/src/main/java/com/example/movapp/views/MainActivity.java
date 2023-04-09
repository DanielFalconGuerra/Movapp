package com.example.movapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.movapp.R;
import com.example.movapp.adapters.MoviesAdapter;
import com.example.movapp.databinding.ActivityMainBinding;
import com.example.movapp.viewmodel.MoviesViewModel;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    MoviesViewModel model;
    //MoviesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        setTheme(R.style.Theme_Movapp);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        model = new ViewModelProvider(MainActivity.this).get(MoviesViewModel.class);

        /*binding.rvTopRated.setHasFixedSize(true);
        LinearLayoutManager layoutManagerTop = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.rvTopRated.setLayoutManager(layoutManagerTop);

        binding.rvNowPlaying.setHasFixedSize(true);
        LinearLayoutManager layoutManagerNow = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.rvNowPlaying.setLayoutManager(layoutManagerNow);

        AtomicInteger pageTopMovie = new AtomicInteger();
        AtomicInteger pageNowPlaying = new AtomicInteger();

        pageTopMovie.set(1);
        pageNowPlaying.set(1);
        model.topMoviesRootLiveData(pageTopMovie.get()).observe(this, topMovies -> {
            binding.loadAnimation.setVisibility(View.GONE);
            if(topMovies != null){
                pageTopMovie.set(topMovies.page);
                adapter = new MoviesAdapter(this, topMovies.results);
                binding.rvTopRated.setAdapter(adapter);

                if(pageTopMovie.get() == 1)
                    binding.btnBeforeTopMovie.setEnabled(false);
                else
                    binding.btnBeforeTopMovie.setEnabled(true);

                if(pageTopMovie.get() == topMovies.total_pages)
                    binding.btnNextTopMovie.setEnabled(false);
                else
                    binding.btnNextTopMovie.setEnabled(true);
            }
        });

        binding.btnBeforeTopMovie.setOnClickListener(view -> {
            pageTopMovie.set(pageTopMovie.get() - 1);
            model.topMoviesRootLiveData(pageTopMovie.get());
        });

        binding.btnNextTopMovie.setOnClickListener(view -> {
            pageTopMovie.set(pageTopMovie.get() + 1);
            model.topMoviesRootLiveData(pageTopMovie.get());
        });

        model.nowPlayingRootLiveData(pageNowPlaying.get()).observe(this, nowPlaying -> {
            if(nowPlaying != null){
                pageNowPlaying.set(nowPlaying.page);
                adapter = new MoviesAdapter(this, nowPlaying.results);
                binding.rvNowPlaying.setAdapter(adapter);

                if(pageNowPlaying.get() == 1)
                    binding.btnBeforeNowPlaying.setEnabled(false);
                else
                    binding.btnBeforeNowPlaying.setEnabled(true);

                if(pageNowPlaying.get() == nowPlaying.total_pages)
                    binding.btnNextNowPlaying.setEnabled(false);
                else
                    binding.btnNextNowPlaying.setEnabled(true);
            }
        });

        binding.btnBeforeNowPlaying.setOnClickListener(view -> {
            pageNowPlaying.set(pageNowPlaying.get() - 1);
            model.nowPlayingRootLiveData(pageNowPlaying.get());
        });

        binding.btnNextNowPlaying.setOnClickListener(view -> {
            pageNowPlaying.set(pageNowPlaying.get() + 1);
            model.nowPlayingRootLiveData(pageNowPlaying.get());
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
        });*/

        Fragment init = new Movies();
        //init.setArguments(bundle_init);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, init).commit();

    }
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}