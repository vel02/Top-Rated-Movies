package kiz.learnwithvel.topratedmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kiz.learnwithvel.topratedmovies.adapter.MovieRecyclerAdapter;
import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.util.Utilities;

public class TopRatedListActivity extends AppCompatActivity implements MovieRecyclerAdapter.OnMovieClickListener {

    private static final String TAG = "TopRatedListActivity";

    private RecyclerView recyclerView;
    private MovieRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated);
        setSupportActionBar(findViewById(R.id.toolbar));
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.rv_list);
        initRecyclerAdapter();
        initSearch();

        Utilities.topRatedRequestRetrofit(adapter, TAG);
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
                Utilities.getPopularRequestRetrofit(adapter, TAG, 1);
                break;
            case R.id.action_upcoming:
                Utilities.getUpcomingRequestRetrofit(adapter, TAG, 1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerAdapter() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void initSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

    @Override
    public void onClick(Movie movie) {
        Log.d(TAG, "onClick: " + movie.getTitle());
        Utilities.getVideoRequestRetrofit(TAG, String.valueOf(movie.getId()));
    }
}