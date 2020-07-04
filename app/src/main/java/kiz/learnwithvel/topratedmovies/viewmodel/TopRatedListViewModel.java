package kiz.learnwithvel.topratedmovies.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.repository.TopRatedListRepository;
import kiz.learnwithvel.topratedmovies.util.Resource;

public class TopRatedListViewModel extends AndroidViewModel {

    public static final String QUERY_EXHAUSTED = "No more results.";
    private static final String TAG = "TopRatedListViewModel";
    private TopRatedListRepository repository;

    private MediatorLiveData<Resource<List<Movie>>> movies = new MediatorLiveData<>();

    private boolean isPerformingQuery;
    private boolean isQueryExhausted;
    private int page;

    private long requestStartTime;

    public TopRatedListViewModel(@NonNull Application application) {
        super(application);
        repository = TopRatedListRepository.getInstance(application);
    }

    public LiveData<Resource<List<Movie>>> getTopRatedMovies() {
        return movies;
    }

    public int getPage() {
        return page;
    }

    public void nextPage() {
        if (!isQueryExhausted && !isPerformingQuery) {
            page++;
            execute();
        }
    }

    public void getTopRatedMovies(int page) {
        if (!isPerformingQuery) {
            if (page == 0) page = 1;
            this.page = page;
            this.isQueryExhausted = false;
            execute();
        }

    }

    private void execute() {
        this.requestStartTime = System.currentTimeMillis();
        this.isPerformingQuery = true;
        final LiveData<Resource<List<Movie>>> repositorySource = repository.getTopRatedMovies(page);
        movies.addSource(repositorySource, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    movies.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                        isPerformingQuery = false;
                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                isQueryExhausted = true;
                                movies.setValue(
                                        new Resource<>(
                                                Resource.Status.ERROR,
                                                listResource.data,
                                                QUERY_EXHAUSTED
                                        ));
                            }
                        }
                        movies.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        movies.removeSource(repositorySource);
                    }
                } else {
                    movies.removeSource(repositorySource);
                }
            }
        });
    }

}
