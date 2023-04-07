package com.example.movapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.movapp.R;
import com.example.movapp.adapters.TopMoviesAdapter;
import com.example.movapp.databinding.ActivityMainBinding;
import com.example.movapp.viewmodel.MoviesViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    MoviesViewModel model;
    TopMoviesAdapter adapter;
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

        binding.rvTopRated.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.rvTopRated.setLayoutManager(layoutManager);

        model.topMoviesRootLiveData().observe(this, topMoviesRoot -> {
            binding.loadAnimation.setVisibility(View.GONE);
            if(topMoviesRoot != null){
                adapter = new TopMoviesAdapter(this, topMoviesRoot.results);
                binding.rvTopRated.setAdapter(adapter);
            }
        });
    }
}