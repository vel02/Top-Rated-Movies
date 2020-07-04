package kiz.learnwithvel.topratedmovies.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import kiz.learnwithvel.topratedmovies.AppExecutor;
import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.persistence.TopRatedMovieDao;
import kiz.learnwithvel.topratedmovies.persistence.TopRatedMovieDatabase;
import kiz.learnwithvel.topratedmovies.request.ServiceGenerator;
import kiz.learnwithvel.topratedmovies.request.respond.ApiResponse;
import kiz.learnwithvel.topratedmovies.request.respond.MovieResponse;
import kiz.learnwithvel.topratedmovies.util.Constants;
import kiz.learnwithvel.topratedmovies.util.NetworkBoundResource;
import kiz.learnwithvel.topratedmovies.util.Resource;

public class TopRatedListRepository {

    private static TopRatedListRepository instance;

    private TopRatedMovieDao topRatedMoviesDao;

    private TopRatedListRepository(Context context) {
        topRatedMoviesDao = TopRatedMovieDatabase.getInstance(context).getTopRatedMoviesDao();
    }

    public static TopRatedListRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TopRatedListRepository(context);
        }
        return instance;
    }

    public LiveData<Resource<List<Movie>>> getTopRatedMovies(int page) {
        return new NetworkBoundResource<List<Movie>, MovieResponse>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                if (item.getMovies() != null) {

                    Movie[] movies = new Movie[item.getMovies().size()];

                    int index = 0;
                    for (long row : topRatedMoviesDao.insertMovies(item.getMovies().toArray(movies))) {

                        if (row == -1) {
                            topRatedMoviesDao.updateMovies(
                                    String.valueOf(movies[index].getId()),
                                    movies[index].getTitle(),
                                    movies[index].getOverview(),
                                    movies[index].getRelease_date(),
                                    movies[index].getPoster_path(),
                                    movies[index].getBackdrop_path(),
                                    movies[index].getVote_average(),
                                    movies[index].getOriginal_language()
                            );
                        }
                        index++;
                    }

                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                return topRatedMoviesDao.getMovies(page);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieResponse>> createCall() {
                return ServiceGenerator.getRequestApi().getTopRatedMoviesApi(
                        Constants.API_KEY, page
                );
            }
        }.asLiveData();
    }

}
