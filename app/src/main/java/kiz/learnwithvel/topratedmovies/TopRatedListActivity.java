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
import kiz.learnwithvel.topratedmovies.util.Resource;
import kiz.learnwithvel.topratedmovies.util.Utilities;
import kiz.learnwithvel.topratedmovies.viewmodel.TopRatedListViewModel;
import kiz.learnwithvel.topratedmovies.viewmodel.factory.TopRatedListViewModelFactory;

import static kiz.learnwithvel.topratedmovies.viewmodel.TopRatedListViewModel.QUERY_EXHAUSTED;

public class TopRatedListActivity extends AppCompatActivity implements MovieRecyclerAdapter.OnMovieClickListener {

    private static final String TAG = "TopRatedListActivity";

    private RecyclerView recyclerView;
    private MovieRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;
    private TopRatedListViewModel topRatedListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated);
        setSupportActionBar(findViewById(R.id.toolbar));

        ViewModelProvider provider = new ViewModelProvider(this, new TopRatedListViewModelFactory(this.getApplication()));
        topRatedListViewModel = provider.get(TopRatedListViewModel.class);

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.rv_list);
        initRecyclerAdapter();
        initSearch();

        topRatedListViewModel.getTopRatedMovies(1);
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
                Utilities.getPopularRequestRetrofit(adapter, TAG, 1);
                break;
            case R.id.action_upcoming:
                adapter.displayOnlyLoading();
                Utilities.getUpcomingRequestRetrofit(adapter, TAG, 1);
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
                    topRatedListViewModel.nextPage();
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
                Utilities.searchRequestRetrofit(adapter, TAG, query, 1);
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
        topRatedListViewModel.getTopRatedMovies().observe(this, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    if (listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING:
                                if (topRatedListViewModel.getPage() > 1) {
                                    adapter.displayLoading();
                                } else adapter.displayOnlyLoading();
                                break;
                            case ERROR:
                                Log.d(TAG, "onChanged: cannot refresh the cache.");
                                Log.d(TAG, "onChanged: ERROR message:  " + listResource.message);
                                Log.d(TAG, "onChanged: status ERROR, #recipes:  " + listResource.data.size());
                                adapter.hideLoading();
                                adapter.addList(listResource.data);
                                Toast.makeText(TopRatedListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(listResource.message).equals(QUERY_EXHAUSTED))
                                    adapter.displayExhausted();
                                break;
                            case SUCCESS:
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #recipes: " + listResource.data.size());
                                adapter.hideLoading();
                                adapter.addList(listResource.data);
                                if (listResource.message != null && listResource.message.equals(QUERY_EXHAUSTED))
                                    adapter.displayExhausted();
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
        Utilities.getVideoRequestRetrofit(TAG, String.valueOf(movie.getId()));
    }
}