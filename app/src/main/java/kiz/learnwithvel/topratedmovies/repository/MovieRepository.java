package kiz.learnwithvel.topratedmovies.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import kiz.learnwithvel.topratedmovies.AppExecutor;
import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.model.Video;
import kiz.learnwithvel.topratedmovies.persistence.MovieDatabase;
import kiz.learnwithvel.topratedmovies.persistence.MoviesDao;
import kiz.learnwithvel.topratedmovies.request.ServiceGenerator;
import kiz.learnwithvel.topratedmovies.request.respond.ApiResponse;
import kiz.learnwithvel.topratedmovies.request.respond.MovieResponse;
import kiz.learnwithvel.topratedmovies.request.respond.VideoResponse;
import kiz.learnwithvel.topratedmovies.util.Constants;
import kiz.learnwithvel.topratedmovies.util.NetworkBoundResource;
import kiz.learnwithvel.topratedmovies.util.Resource;

public class MovieRepository {

    private static final String TAG = "MovieViewModelRepositor";

    private static MovieRepository instance;

    private MoviesDao moviesDao;

    private MovieRepository(Context context) {
        moviesDao = MovieDatabase.getInstance(context).getMoviesDao();
    }

    public static MovieRepository getInstance(Context context) {
        if (instance == null) {
            instance = new MovieRepository(context);
        }
        return instance;
    }

    public LiveData<Resource<List<Movie>>> getTopRatedMoviesApi(int page) {
        return new NetworkBoundResource<List<Movie>, MovieResponse>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                if (item.getMovies() != null) {
                    insertMovies(item, "TOP_RATED_MOVIES");
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                return moviesDao.getTopRatedMovies(page);
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

    public LiveData<Resource<List<Movie>>> getPopularMoviesApi(int page) {
        return new NetworkBoundResource<List<Movie>, MovieResponse>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                if (item.getMovies() != null) {
                    insertMovies(item, "POPULAR_MOVIES");
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                return moviesDao.getPopularMovies(page);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieResponse>> createCall() {
                return ServiceGenerator.getRequestApi().getPopularMoviesApi(
                        Constants.API_KEY, page);
            }
        }.asLiveData();
    }


    public LiveData<Resource<List<Movie>>> getUpcomingMoviesApi(int page) {
        return new NetworkBoundResource<List<Movie>, MovieResponse>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                insertMovies(item, "UPCOMING_MOVIES");
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                return moviesDao.getUpcomingMovies(page);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieResponse>> createCall() {
                return ServiceGenerator.getRequestApi().getUpcomingMoviesApi(
                        Constants.API_KEY, page);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Movie>>> getSearchMoviesApi(String include_adult, String query, int page) {
        return new NetworkBoundResource<List<Movie>, MovieResponse>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                if (item.getMovies() != null) {
//                    insertMovies(item, "SEARCH_MOVIES");
                    setRequestType(item, "SEARCH_MOVIES");
                    Movie[] movies = new Movie[item.getMovies().size()];
                    moviesDao.insertMoviesCompletely(item.getMovies().toArray(movies));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                return moviesDao.getMovies(query, page);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieResponse>> createCall() {
                return ServiceGenerator.getRequestApi().searchMoviesApi(
                        Constants.API_KEY, include_adult, query, page);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Video>>> getVideosApi(String movie_id, String language) {
        return new NetworkBoundResource<List<Video>, VideoResponse>(AppExecutor.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull VideoResponse item) {
                if (item.getVideos() != null) {

                    List<Video> list = item.getVideos();
                    for (Video video : list) {
                        video.setMovie_id(item.getId());
                    }

                    Video[] videos = new Video[item.getVideos().size()];
                    int index = 0;
                    for (long row : moviesDao.insertVideos(item.getVideos().toArray(videos))) {
                        if (row == -1) {
                            moviesDao.updateVideos(
                                    videos[index].getId(),
                                    videos[index].getKey(),
                                    videos[index].getName(),
                                    videos[index].getSite(),
                                    videos[index].getSize(),
                                    videos[index].getType()
                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Video> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Video>> loadFromDb() {
                return moviesDao.getVideos(movie_id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VideoResponse>> createCall() {
                return ServiceGenerator.getRequestApi().getVideoApi(
                        movie_id, Constants.API_KEY, language);
            }
        }.asLiveData();
    }

    private void setRequestType(@NonNull MovieResponse item, String type) {
        List<Movie> list = item.getMovies();
        for (Movie movies : list) {
            movies.setType_request(type);
        }
    }

    private void insertMovies(@NonNull MovieResponse item, String type) {
        setRequestType(item, type);

        Movie[] movies = new Movie[item.getMovies().size()];

        int index = 0;
        for (long row : moviesDao.insertMovies(item.getMovies().toArray(movies))) {

            if (row == -1) {
                moviesDao.updateMovies(
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