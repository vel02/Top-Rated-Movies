package kiz.learnwithvel.topratedmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kiz.learnwithvel.topratedmovies.R;
import kiz.learnwithvel.topratedmovies.adapter.viewholder.MovieViewHolder;
import kiz.learnwithvel.topratedmovies.model.Movie;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private final OnMovieClickListener listener;

    public MovieRecyclerAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_list_item, parent, false);
        return new MovieViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return ((movies != null && movies.size() > 0) ? movies.size() : 0);
    }

    public void addList(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public interface OnMovieClickListener {
        void onClick(Movie movie);
    }

}
