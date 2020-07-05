package kiz.learnwithvel.topratedmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import kiz.learnwithvel.topratedmovies.adapter.VideoRecyclerAdapter;
import kiz.learnwithvel.topratedmovies.model.Video;
import kiz.learnwithvel.topratedmovies.util.Resource;
import kiz.learnwithvel.topratedmovies.viewmodel.VideoListViewModel;
import kiz.learnwithvel.topratedmovies.viewmodel.factory.VideoListViewModelFactory;

public class VideoListActivity extends BaseActivity {

    private static final String TAG = "VideoListActivity";

    private VideoListViewModel videoListViewModel;
    private FetchDialogFragment fetchDialogFragment;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private VideoRecyclerAdapter adapter;

    private String video_id;

    private void getIncomingIntent() {
        if (getIntent().hasExtra("video_content")) {
            video_id = getIntent().getStringExtra("video_content");
            Log.d(TAG, "getIncomingIntent: " + video_id);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ViewModelProvider provider = new ViewModelProvider(this, new VideoListViewModelFactory(this.getApplication()));
        videoListViewModel = provider.get(VideoListViewModel.class);
        activateToolBar(true, true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Videos");
        fetchDialogFragment = new FetchDialogFragment();
        recyclerView = findViewById(R.id.rv_list);
        initRecyclerAdapter();
        subscribeObservable();
        getIncomingIntent();
        videoListViewModel.getVideos(video_id);
    }

    private void initRecyclerAdapter() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VideoRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void subscribeObservable() {
        videoListViewModel.getVideos().observe(this, new Observer<Resource<List<Video>>>() {
            @Override
            public void onChanged(Resource<List<Video>> listResource) {
                if (listResource != null) {
                    if (listResource.data != null) {
                        switch (listResource.status) {

                            case LOADING:
                                initFetchDialog();
                                break;
                            case ERROR:
                                Log.d(TAG, "onChanged: cannot refresh the cache.");
                                Log.d(TAG, "onChanged: ERROR message:  " + listResource.message);
                                Log.d(TAG, "onChanged: status ERROR, #Videos:  " + listResource.data.size());
                                Log.d(TAG, "onChanged: " + listResource.data);
                                Toast.makeText(VideoListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                fetchDialogFragment.dismiss();
                                adapter.addList(listResource.data);
                                break;
                            case SUCCESS:
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Videos: " + listResource.data.size());
                                Log.d(TAG, "onChanged: " + listResource.data);
                                fetchDialogFragment.dismiss();
                                adapter.addList(listResource.data);
                                break;
                        }
                    }
                }
            }
        });
    }

    private void initFetchDialog() {
        fetchDialogFragment.show(getSupportFragmentManager(), "fetcher_fragment");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}