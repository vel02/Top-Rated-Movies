package kiz.learnwithvel.topratedmovies.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.repository.MovieRepository;
import kiz.learnwithvel.topratedmovies.util.Resource;

public class MovieListViewModel extends AndroidViewModel {

    public static final String QUERY_EXHAUSTED = "No more results.";
    private static final String TAG = "TopRatedListViewModel";

    private MovieRepository repository;
    private MediatorLiveData<Resource<List<Movie>>> movies = new MediatorLiveData<>();
    private MutableLiveData<RequestType> requestType = new MutableLiveData<>();

    private boolean isPerformingQuery;
    private boolean isQueryExhausted;
    private int page;
    private String id;
    private String query;
    private String language;
    private String include_adult;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        this.repository = MovieRepository.getInstance(application);
        this.requestType.setValue(RequestType.TOP_RATED);
    }

    private long requestStartTime;

    public void nextPage() {
        if (!isQueryExhausted && !isPerformingQuery) {
            Log.d(TAG, "nextPage: called");
            page++;
            if (requestType.getValue() == RequestType.TOP_RATED)
                executeTopRated();
            else if (requestType.getValue() == RequestType.POPULAR)
                executePopular();
            else if (requestType.getValue() == RequestType.UPCOMING)
                executeUpcoming();
            else if (requestType.getValue() == RequestType.SEARCH)
                executeSearch();
        }
    }

    public LiveData<Resource<List<Movie>>> getMovies() {
        return movies;
    }

    public LiveData<RequestType> getRequestType() {
        return requestType;
    }

    public int getPage() {
        return page;
    }

    public void getTopRatedMovies(int page) {
        if (!isPerformingQuery) {
            if (page == 0) page = 1;
            this.requestType.setValue(RequestType.TOP_RATED);
            this.page = page;
            this.isQueryExhausted = false;
            executeTopRated();
        }
    }

    public void searchMovies(String query) {
        if (!isPerformingQuery) {
            if (page == 0) page = 1;
            this.requestType.setValue(RequestType.SEARCH);
            this.query = query;
            this.include_adult = "false";
            this.isQueryExhausted = false;
            executeSearch();
        }
    }

    public void getPopularMovies(int page) {
        if (!isPerformingQuery) {
            if (page == 0) page = 1;
            this.requestType.setValue(RequestType.POPULAR);
            this.page = page;
            this.isQueryExhausted = false;
            executePopular();
        }
    }

    public void getUpcomingMovies(int page) {
        if (!isPerformingQuery) {
            if (page == 0) page = 1;
            this.requestType.setValue(RequestType.UPCOMING);
            this.page = page;
            this.isQueryExhausted = false;
            executeUpcoming();
        }
    }


    private void executeUpcoming() {
        this.isPerformingQuery = true;
        final LiveData<Resource<List<Movie>>> source = repository.getUpcomingMoviesApi(page);
        movies.addSource(source, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    movies.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        isPerformingQuery = false;
                        if (listResource.data != null && listResource.data.size() == 0) {
                            movies.setValue(new Resource<>(
                                    Resource.Status.ERROR,
                                    listResource.data,
                                    QUERY_EXHAUSTED
                            ));
                        }
                        movies.removeSource(source);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        movies.removeSource(source);
                    }
                } else {
                    isPerformingQuery = false;
                    movies.removeSource(source);
                }
            }
        });
    }

    private void executePopular() {
        Log.d(TAG, "executePopular() called");
        this.isPerformingQuery = true;
        final LiveData<Resource<List<Movie>>> source = repository.getPopularMoviesApi(page);
        movies.addSource(source, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    movies.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        isPerformingQuery = false;
                        if (listResource.data != null
                                && listResource.data.size() == 0) {
                            movies.setValue(new Resource<>(
                                    Resource.Status.ERROR,
                                    listResource.data,
                                    QUERY_EXHAUSTED
                            ));
                        }
                        movies.removeSource(source);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        movies.removeSource(source);
                    }
                } else {
                    isPerformingQuery = false;
                    movies.removeSource(source);
                }
            }
        });
    }

    private void executeSearch() {
        Log.d(TAG, "executeSearch() called");
        this.isPerformingQuery = true;
        final LiveData<Resource<List<Movie>>> repositorySource = repository.getSearchMoviesApi(include_adult, query, page);

        movies.addSource(repositorySource, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    movies.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                        isPerformingQuery = false;
                        if ((listResource.data != null
                                && listResource.data.size() == 0)
                                || (listResource.data != null
                                && listResource.data.size() < 20)) {
                            isQueryExhausted = true;
                            movies.setValue(
                                    new Resource<>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED
                                    )
                            );
                        }
                        movies.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        movies.removeSource(repositorySource);
                    }
                } else {
                    isPerformingQuery = false;
                    movies.removeSource(repositorySource);
                }
            }
        });

    }

    private void executeTopRated() {
        Log.d(TAG, "executeTopRated() called");
        this.requestStartTime = System.currentTimeMillis();
        this.isPerformingQuery = true;
        final LiveData<Resource<List<Movie>>> repositorySource = repository.getTopRatedMoviesApi(page);
        movies.addSource(repositorySource, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (listResource != null) {
                    movies.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                        isPerformingQuery = false;
                        if (listResource.data != null && listResource.data.size() == 0) {
                            isQueryExhausted = true;
                            movies.setValue(
                                    new Resource<>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED
                                    ));
                        }
                        movies.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        movies.removeSource(repositorySource);
                    }
                } else {
                    isPerformingQuery = false;
                    movies.removeSource(repositorySource);
                }
            }
        });
    }

    public enum RequestType {SEARCH, TOP_RATED, POPULAR, UPCOMING, VIDEO}

}
