package com.example.movapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.movapp.R;
import com.example.movapp.adapters.MoviesAdapter;
import com.example.movapp.databinding.ActivityMainBinding;
import com.example.movapp.utility.NetworkChangeListener;
import com.example.movapp.viewmodel.MoviesViewModel;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    NetworkChangeListener networkChangeListener;
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
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        model = new ViewModelProvider(MainActivity.this).get(MoviesViewModel.class);
        networkChangeListener = new NetworkChangeListener();
        /*networkChangeListener.isConnected().observe(this, isConnected -> {
            Toast.makeText(this, "observe", Toast.LENGTH_SHORT).show();
            if(!isConnected){
                Toast.makeText(this, "not connected", Toast.LENGTH_SHORT).show();
                Log.e("networkChangeListener", "not connected");

            }else{
                Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                Log.e("networkChangeListener", "connected");
            }
        });*/
        Fragment init = new Movies();
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
    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager. CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}