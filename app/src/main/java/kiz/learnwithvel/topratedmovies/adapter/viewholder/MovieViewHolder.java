package kiz.learnwithvel.topratedmovies.adapter.viewholder;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import kiz.learnwithvel.topratedmovies.R;
import kiz.learnwithvel.topratedmovies.adapter.BaseViewHolder;
import kiz.learnwithvel.topratedmovies.adapter.MovieRecyclerAdapter;
import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.util.Constants;


public class MovieViewHolder extends BaseViewHolder implements View.OnClickListener {

    ImageView image, error;
    TextView title, description, year;
    ProgressBar progressBar;

    final MovieRecyclerAdapter.OnMovieClickListener listener;

    public MovieViewHolder(@NonNull View itemView, MovieRecyclerAdapter.OnMovieClickListener listener) {
        super(itemView);
        this.listener = listener;
        this.error = itemView.findViewById(R.id.movie_error_poster);
        this.image = itemView.findViewById(R.id.movie_poster);
        this.title = itemView.findViewById(R.id.movie_title);
        this.description = itemView.findViewById(R.id.movie_desc);
        this.year = itemView.findViewById(R.id.movie_year);
        this.progressBar = itemView.findViewById(R.id.movie_progress);

        itemView.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBind(Movie movie) {
        super.onBind(movie);

        this.setupImage();
        this.title.setText(movie.getTitle());
        this.description.setText(movie.getOverview());
        this.year.setText(format());

    }

    @Override
    protected void setupImage() {
        String img = "";
        if (getMovie().getPoster_path() != null) {
            img = Constants.BASE_URL_IMG + getMovie().getPoster_path();
        }
        Glide.with(itemView.getContext())
                .load(img)
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .into(image);


    }

    @Override
    protected String format() {
        String year = getMovie().getRelease_date();
        if (year != null && !year.isEmpty())
            year = year.substring(0, 4);
        else year = "N/A";
        String language = getMovie().getOriginal_language().toUpperCase();
        return year + " | " + language;
    }

    @Override
    protected void clear() {
        this.title.setText("");
        this.description.setText("");
        this.year.setText("");
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        this.listener.onClick(getMovie());
    }
}
