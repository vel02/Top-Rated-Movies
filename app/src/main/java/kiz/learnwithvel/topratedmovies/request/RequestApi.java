package kiz.learnwithvel.topratedmovies.request;

import io.reactivex.rxjava3.core.Flowable;
import kiz.learnwithvel.topratedmovies.request.respond.MovieResponse;
import kiz.learnwithvel.topratedmovies.request.respond.VideoResponse;
import kiz.learnwithvel.topratedmovies.util.Constants;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestApi {

    @GET(Constants.GET_TOP_RATED_URL)
    Flowable<MovieResponse> getTopRatedMoviesApi(
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET(Constants.GET_POPULAR_URL)
    Flowable<MovieResponse> getPopularMoviesApi(
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET(Constants.GET_UPCOMING_URL)
    Flowable<MovieResponse> getUpcomingMoviesApi(
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET(Constants.GET_SEARCH_URL)
    Flowable<MovieResponse> searchMoviesApi(
            @Query("api_key") String api_key,
            @Query("include_adult") String include_adult,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET(Constants.GET_SEARCH_VIDEO_URL)
    Flowable<VideoResponse> getVideoApi(
            @Path("movie_id") String movie_id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );


}
