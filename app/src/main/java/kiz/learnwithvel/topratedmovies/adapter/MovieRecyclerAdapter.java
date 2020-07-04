package kiz.learnwithvel.topratedmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kiz.learnwithvel.topratedmovies.R;
import kiz.learnwithvel.topratedmovies.adapter.viewholder.LoadingViewHolder;
import kiz.learnwithvel.topratedmovies.adapter.viewholder.MovieViewHolder;
import kiz.learnwithvel.topratedmovies.model.Movie;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String INDICATOR_VIEW_TYPE_LOADING = "kiz.learnwithvel.topratedmovies.LOADING...";
    private static final String INDICATOR_VIEW_TYPE_EXHAUSTED = "kiz.learnwithvel.topratedmovies.EXHAUSTED...";
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_EXHAUSTED = 2;

    private List<Movie> movies;
    private final OnMovieClickListener listener;

    public MovieRecyclerAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_list_item, parent, false);
        switch (viewType) {
            case VIEW_TYPE_LOADING:
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_loading_list_item, parent, false));
            case VIEW_TYPE_EXHAUSTED:
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_exhausted_list_item, parent, false));
            case VIEW_TYPE_NORMAL:
            default:
                return new MovieViewHolder(view, listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == VIEW_TYPE_NORMAL) holder.onBind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return ((movies != null && movies.size() > 0) ? movies.size() : 0);
    }

    public void addList(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getTitle().equals(INDICATOR_VIEW_TYPE_LOADING)) {
            return VIEW_TYPE_LOADING;
        } else if (movies.get(position).getTitle().equals(INDICATOR_VIEW_TYPE_EXHAUSTED))
            return VIEW_TYPE_EXHAUSTED;
        return VIEW_TYPE_NORMAL;
    }

    public void displayExhausted() {
        hideLoading();
        Movie exhausted = new Movie();
        exhausted.setTitle(INDICATOR_VIEW_TYPE_EXHAUSTED);
        movies.add(exhausted);
        notifyDataSetChanged();
    }

    public void displayOnlyLoading() {
        clearMovieList();
        Movie loading = new Movie();
        loading.setTitle(INDICATOR_VIEW_TYPE_LOADING);
        movies.add(loading);
        notifyDataSetChanged();
    }

    private void clearMovieList() {
        if (movies == null) {
            movies = new ArrayList<>();
        } else movies.clear();
        notifyDataSetChanged();
    }

    public void hideLoading() {
        if (isLoading()) {
            if (movies.get(0).getTitle().equals(INDICATOR_VIEW_TYPE_LOADING)) {
                movies.remove(0);
            } else if (movies.get(movies.size() - 1).getTitle().equals(INDICATOR_VIEW_TYPE_LOADING)) {
                movies.remove(movies.size() - 1);
            }
        }
    }

    public void displayLoading() {
        if (movies == null) {
            movies = new ArrayList<>();
        }

        if (!isLoading()) {
            Movie loading = new Movie();
            loading.setTitle(INDICATOR_VIEW_TYPE_LOADING);
            movies.add(loading);
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (movies != null) {
            return movies.size() > 0
                    && movies.get(movies.size() - 1).getTitle()
                    .equals(INDICATOR_VIEW_TYPE_LOADING);
        }
        return false;
    }


    public interface OnMovieClickListener {
        void onClick(Movie movie);
    }

}
