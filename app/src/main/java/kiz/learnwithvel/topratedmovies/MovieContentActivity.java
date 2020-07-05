package kiz.learnwithvel.topratedmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.util.Constants;

public class MovieContentActivity extends BaseActivity {

    private static final String TAG = "MovieContentActivity";
    private static final String DATE_OLD_FORMAT = "yyyy-MM-dd";
    private static final String DATE_NEW_FORMAT = "MMMM dd, yyyy";

    private ImageView image;
    private TextView title;
    private TextView ratings;
    private TextView description;
    private TextView release;

    private String title_toolbar;

    private FetchDialogFragment fetchDialogFragment;

    private void getIncomingIntent() {

        if (getIntent().hasExtra("movie_content")) {
            Movie movie = getIntent().getParcelableExtra("movie_content");
            if (movie != null) {
                Glide.with(this)
                        .load(Constants.BASE_URL_IMG + movie.getBackdrop_path())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(image);

                this.title_toolbar = movie.getTitle();
                this.title.setText(movie.getTitle());
                this.ratings.setText(movie.getVote_average());
                this.description.setText(movie.getOverview());
                try {
                    this.release.setText(format(movie.getRelease_date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "getIncomingIntent: " + movie);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_content);
        activateToolBar(true, true);
        this.image = findViewById(R.id.content_movie_image);
        this.title = findViewById(R.id.content_movie_title);
        this.ratings = findViewById(R.id.content_movie_rating);
        this.description = findViewById(R.id.content_movie_description);
        this.release = findViewById(R.id.content_movie_release);
        getIncomingIntent();
        initCollapsingToolbar();

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout layout = findViewById(R.id.appbar);
        layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShowImage = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset > -600) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShowImage = true;
                } else if (isShowImage && (scrollRange + verticalOffset) <= -600) {
                    collapsingToolbarLayout.setTitle(title_toolbar);
                    isShowImage = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected String format(String input) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_OLD_FORMAT, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DATE_NEW_FORMAT, Locale.ENGLISH);
        Date date = inputFormat.parse(input);
        assert date != null;
        return outputFormat.format(date);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}