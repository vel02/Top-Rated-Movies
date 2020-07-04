package kiz.learnwithvel.topratedmovies.util;

import android.util.Log;

import androidx.appcompat.widget.SearchView;

import java.util.List;

import kiz.learnwithvel.topratedmovies.adapter.MovieRecyclerAdapter;
import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.model.Video;
import kiz.learnwithvel.topratedmovies.request.RequestApi;
import kiz.learnwithvel.topratedmovies.request.ServiceGenerator;
import kiz.learnwithvel.topratedmovies.request.respond.MovieResponse;
import kiz.learnwithvel.topratedmovies.request.respond.VideoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class Utilities {

    public static void clearSearch(SearchView searchView) {
        searchView.clearFocus();
        searchView.setQuery("", false);
        searchView.setIconified(true);
    }

    @EverythingIsNonNull
    public static void topRatedRequestRetrofit(MovieRecyclerAdapter adapter, String tag) {
        final String TAG = tag;
        RequestApi api = ServiceGenerator.getRequestApi();
        Call<MovieResponse> respondCall = api.getTopRatedMoviesApi(Constants.API_KEY, 1);
        respondCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.raw().networkResponse() != null) {
                    Log.d(TAG, "onResponse: response is from NETWORK...");
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<Movie> movies = response.body().getMovies();
                        int count = 0;
                        for (Movie movie : movies) {
                            Log.d(tag, "onResponse: count: #" + ++count + " " + movie.getTitle());
                        }
                        adapter.addList(movies);
                        Log.d(tag, "onResponse: total count is " + count);
                        Log.d(tag, "==================================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    @EverythingIsNonNull
    public static void searchRequestRetrofit(MovieRecyclerAdapter adapter, String tag, String query, int page) {
        final String TAG = tag;
        RequestApi api = ServiceGenerator.getRequestApi();
        Call<MovieResponse> respondCall = api.searchMoviesApi(Constants.API_KEY, "false", query, page);
        respondCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.raw().networkResponse() != null) {
                    Log.d(TAG, "onResponse: response is from NETWORK...");
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<Movie> movies = response.body().getMovies();
                        int count = 0;
                        for (Movie movie : movies) {
                            Log.d(tag, "onResponse: count: #" + ++count + " " + movie.getTitle());
                        }
                        adapter.addList(movies);
                        Log.d(tag, "onResponse: total count is " + count);
                        Log.d(tag, "==================================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }


    @EverythingIsNonNull
    public static void getVideoRequestRetrofit(String tag, String id) {
        final String TAG = tag;
        RequestApi api = ServiceGenerator.getRequestApi();
        Call<VideoResponse> respondCall = api.getVideoApi(id, Constants.API_KEY, "en-US");
        respondCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.raw().networkResponse() != null) {
                    Log.d(TAG, "onResponse: response is from NETWORK...");
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<Video> videos = response.body().getVideos();
                        int count = 0;
                        for (Video video : videos) {
                            Log.d(tag, "onResponse: count: #" + ++count + " " + video.getName());
                        }
                        Log.d(tag, "onResponse: total count is " + count);
                        Log.d(tag, "==================================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });
    }

    @EverythingIsNonNull
    public static void getPopularRequestRetrofit(MovieRecyclerAdapter adapter, String tag, int page) {
        final String TAG = tag;
        RequestApi api = ServiceGenerator.getRequestApi();
        Call<MovieResponse> respondCall = api.getPopularMoviesApi(Constants.API_KEY, page);
        respondCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.raw().networkResponse() != null) {
                    Log.d(TAG, "onResponse: response is from NETWORK...");
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<Movie> movies = response.body().getMovies();
                        int count = 0;
                        for (Movie movie : movies) {
                            Log.d(tag, "onResponse: count: #" + ++count + " " + movie.getTitle());
                        }
                        adapter.addList(movies);
                        Log.d(tag, "onResponse: total count is " + count);
                        Log.d(tag, "==================================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    @EverythingIsNonNull
    public static void getUpcomingRequestRetrofit(MovieRecyclerAdapter adapter, String tag, int page) {
        final String TAG = tag;
        RequestApi api = ServiceGenerator.getRequestApi();
        Call<MovieResponse> respondCall = api.getUpcomingMoviesApi(Constants.API_KEY, page);
        respondCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.raw().networkResponse() != null) {
                    Log.d(TAG, "onResponse: response is from NETWORK...");
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<Movie> movies = response.body().getMovies();
                        int count = 0;
                        for (Movie movie : movies) {
                            Log.d(tag, "onResponse: count: #" + ++count + " " + movie.getTitle());
                        }
                        adapter.addList(movies);
                        Log.d(tag, "onResponse: total count is " + count);
                        Log.d(tag, "==================================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

}
