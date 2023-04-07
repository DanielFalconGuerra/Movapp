package com.example.movapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movapp.R;
import com.example.movapp.models.TopMoviesResult;
import com.example.movapp.models.TopMoviesRoot;

import java.util.ArrayList;

public class TopMoviesAdapter extends RecyclerView.Adapter<TopMoviesAdapter.ViewHolderTopMoviesAdapter> {
    Context mCtx;
    private ArrayList<TopMoviesResult> topMovies;

    public TopMoviesAdapter(Context mCtx, ArrayList<TopMoviesResult>  topMovies) {
        this.mCtx = mCtx;
        this.topMovies = topMovies;
    }

    @NonNull
    @Override
    public ViewHolderTopMoviesAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_style, parent, false);
        return new ViewHolderTopMoviesAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTopMoviesAdapter holder, int position) {
        holder.tvNameMovie.setText(topMovies.get(position).original_title);
    }

    @Override
    public int getItemCount() {
        return topMovies.size();
    }


    public static class ViewHolderTopMoviesAdapter extends RecyclerView.ViewHolder{
        public TextView tvNameMovie;
        public ViewHolderTopMoviesAdapter(@NonNull View itemView) {
            super(itemView);
            tvNameMovie =  itemView.findViewById(R.id.tvNameMovie);
        }
    }


}
