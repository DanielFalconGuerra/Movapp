package com.example.movapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movapp.R;
import com.example.movapp.models.MoviesResult;
import com.example.movapp.viewmodel.MoviesViewModel;
import com.example.movapp.views.MainActivity;
import com.example.movapp.views.MovieData;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolderTopMoviesAdapter> {
    Context mCtx;
    private ArrayList<MoviesResult> topMovies;

    public MoviesAdapter(Context mCtx, ArrayList<MoviesResult>  topMovies) {
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
        MoviesViewModel model = new ViewModelProvider((ViewModelStoreOwner) mCtx).get(MoviesViewModel.class);
        holder.tvNameMovie.setText(topMovies.get(position).original_title);
        holder.tvVoteAverageMovie.setText(String.valueOf(topMovies.get(position).vote_average));
        Glide
                .with(mCtx)
                .load("https://image.tmdb.org/t/p/w500/" + topMovies.get(position).poster_path)
                .into(holder.ivPoster);
        holder.clMovie.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(topMovies.get(position).id));
            Fragment fragment = new MovieData();
            FragmentManager fragmentManager = ((FragmentActivity)mCtx).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.body_container, fragment);
            fragmentTransaction.addToBackStack("details");
            //fragmentTransaction.disallowAddToBackStack();
            fragment.setArguments(bundle);
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return topMovies.size();
    }


    public static class ViewHolderTopMoviesAdapter extends RecyclerView.ViewHolder{
        public TextView tvNameMovie, tvVoteAverageMovie;
        public ShapeableImageView ivPoster;
        public ConstraintLayout clMovie;
        public ViewHolderTopMoviesAdapter(@NonNull View itemView) {
            super(itemView);
            tvNameMovie =  itemView.findViewById(R.id.tvNameMovie);
            tvVoteAverageMovie =  itemView.findViewById(R.id.tvVoteAverageMovie);
            ivPoster =  itemView.findViewById(R.id.ivPoster);
            clMovie = itemView.findViewById(R.id.clMovie);
        }
    }


}
