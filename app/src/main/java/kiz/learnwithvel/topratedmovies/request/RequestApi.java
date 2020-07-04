package kiz.learnwithvel.topratedmovies.request;

import androidx.lifecycle.LiveData;

import kiz.learnwithvel.topratedmovies.request.respond.ApiResponse;
import kiz.learnwithvel.topratedmovies.request.respond.MovieResponse;
import kiz.learnwithvel.topratedmovies.request.respond.VideoResponse;
import kiz.learnwithvel.topratedmovies.util.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestApi {

    @GET(Constants.GET_TOP_RATED_URL)
    LiveData<ApiResponse<MovieResponse>> getTopRatedMoviesApi(
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET(Constants.GET_POPULAR_URL)
    Call<MovieResponse> getPopularMoviesApi(
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET(Constants.GET_UPCOMING_URL)
    Call<MovieResponse> getUpcomingMoviesApi(
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET(Constants.GET_SEARCH_URL)
    Call<MovieResponse> searchMoviesApi(
            @Query("api_key") String api_key,
            @Query("include_adult") String include_adult,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET(Constants.GET_SEARCH_VIDEO_URL)
    Call<VideoResponse> getVideoApi(
            @Path("movie_id") String movie_id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );


}
