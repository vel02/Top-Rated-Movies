package kiz.learnwithvel.topratedmovies.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.model.Video;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private Movie movie;
    private Video video;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract void setupImage();

    protected abstract String format();

    protected abstract void clear();

    public void onBind(Movie movie) {
        this.clear();
        this.movie = movie;
    }

    public void onBind(Video video) {
        this.clear();
        this.video = video;
    }

    public Movie getMovie() {
        return movie;
    }
}
