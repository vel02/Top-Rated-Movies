package kiz.learnwithvel.topratedmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import kiz.learnwithvel.topratedmovies.adapter.MovieRecyclerAdapter;
import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.model.Video;
import kiz.learnwithvel.topratedmovies.util.Resource;
import kiz.learnwithvel.topratedmovies.util.Utilities;
import kiz.learnwithvel.topratedmovies.viewmodel.MovieListViewModel;
import kiz.learnwithvel.topratedmovies.viewmodel.factory.MovieListViewModelFactory;

import static kiz.learnwithvel.topratedmovies.viewmodel.MovieListViewModel.QUERY_EXHAUSTED;

public class MovieListActivity extends AppCompatActivity implements MovieRecyclerAdapter.OnMovieClickListener {

    private static final String TAG = "TopRatedListActivity";

    private RecyclerView recyclerView;
    private MovieRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;
    private MovieListViewModel movieListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated);
        setSupportActionBar(findViewById(R.id.toolbar));

        ViewModelProvider provider = new ViewModelProvider(this, new MovieListViewModelFactory(this.getApplication()));
        movieListViewModel = provider.get(MovieListViewModel.class);

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.rv_list);
        initRecyclerAdapter();
        initSearch();

        subscribeObservable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_popular:
                adapter.displayOnlyLoading();
                movieListViewModel.getPopularMovies(1);
                break;
            case R.id.action_upcoming:
                adapter.displayOnlyLoading();
                movieListViewModel.getUpcomingMovies(1);
                break;
            case R.id.action_top_rated:
                adapter.displayOnlyLoading();
                movieListViewModel.getTopRatedMovies(1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerAdapter() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieRecyclerAdapter(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    movieListViewModel.nextPage();
                }

            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void initSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.displayOnlyLoading();
                recyclerView.smoothScrollToPosition(0);
                movieListViewModel.searchMovies(query);
                Utilities.clearSearch(searchView);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservable() {
        //TODO: Temporary place of request videos, it should be in the VideoActivity.class
        movieListViewModel.getVideos().observe(this, new Observer<Resource<List<Video>>>() {
            @Override
            public void onChanged(Resource<List<Video>> listResource) {
                if (listResource != null) {
                    if (listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING:
//                                adapter.displayOnlyLoading();
                                break;
                            case ERROR:
                                Log.d(TAG, "onChanged: cannot refresh the cache.");
                                Log.d(TAG, "onChanged: ERROR message:  " + listResource.message);
                                Log.d(TAG, "onChanged: status ERROR, #Videos:  " + listResource.data.size());
                                adapter.hideLoading();
                                Log.d(TAG, "onChanged: " + listResource.data);
                                Toast.makeText(MovieListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                break;
                            case SUCCESS:
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Videos: " + listResource.data.size());
                                adapter.hideLoading();
                                Log.d(TAG, "onChanged: " + listResource.data);
                                break;
                        }
                    }
                }
            }
        });

        movieListViewModel.getRequestType().observe(this, new Observer<MovieListViewModel.RequestType>() {
            @Override
            public void onChanged(MovieListViewModel.RequestType requestType) {
                if (requestType != null) {
                    if (requestType == MovieListViewModel.RequestType.TOP_RATED) {
                        movieListViewModel.getTopRatedMovies(1);
                    }
                }
            }
        });

        movieListViewModel.getMovies().observe(this, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    if (listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING:
                                if (movieListViewModel.getPage() > 1) {
                                    adapter.displayLoading();
                                } else adapter.displayOnlyLoading();
                                break;
                            case ERROR:
                                Log.d(TAG, "onChanged: cannot refresh the cache.");
                                Log.d(TAG, "onChanged: ERROR message:  " + listResource.message);
                                Log.d(TAG, "onChanged: status ERROR, #movies:  " + listResource.data.size());
                                adapter.hideLoading();
                                adapter.addList(listResource.data);
                                Toast.makeText(MovieListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(listResource.message).equals(QUERY_EXHAUSTED))
                                    adapter.displayExhausted();
                                break;
                            case SUCCESS:
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #movies: " + listResource.data.size());
                                adapter.hideLoading();
                                adapter.addList(listResource.data);
                                break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(Movie movie) {
        Log.d(TAG, "onClick: " + movie.getTitle());
        movieListViewModel.getVideos(String.valueOf(movie.getId()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}