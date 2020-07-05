package kiz.learnwithvel.topratedmovies.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.model.Video;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MoviesDao {

    @Insert(onConflict = IGNORE)
    long[] insertMovies(Movie... movies);

    @Insert
    void insertMoviesCompletely(Movie... movies);

    @Insert(onConflict = IGNORE)
    long[] insertVideos(Video... videos);

    @Insert(onConflict = REPLACE)
    long insertVideo(Video video);

    @Query("UPDATE videos SET id = :id, `key` = :key, name = :name, site = :site, size = :size, type = :type " +
            "WHERE id = :id")
    void updateVideos(String id, String key, String name, String site, String size, String type);


    @Query("UPDATE movies SET title = :title, overview = :overview, release_date = :release_date, " +
            "poster_path = :poster_path, backdrop_path = :backdrop_path, " +
            "vote_average = :vote_average, original_language = :original_language " +
            "WHERE id = :id")
    void updateMovies(String id, String title, String overview, String release_date, String poster_path, String backdrop_path, String vote_average, String original_language);

    @Query("SELECT * FROM movies WHERE type_request = 'TOP_RATED_MOVIES' LIMIT (:page * 20)")
    LiveData<List<Movie>> getTopRatedMovies(int page);

    @Query("SELECT * FROM movies WHERE type_request = 'POPULAR_MOVIES' LIMIT (:page * 20)")
    LiveData<List<Movie>> getPopularMovies(int page);

    @Query("SELECT * FROM movies WHERE type_request = 'UPCOMING_MOVIES' LIMIT (:page * 20)")
    LiveData<List<Movie>> getUpcomingMovies(int page);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' LIMIT (:page * 20)")
    LiveData<List<Movie>> getMovies(String query, int page);

    @Query("SELECT * FROM videos WHERE movie_id = :movie_id")
    LiveData<List<Video>> getVideos(String movie_id);

}
