package kiz.learnwithvel.topratedmovies.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kiz.learnwithvel.topratedmovies.model.Movie;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TopRatedMovieDao {

    @Insert(onConflict = IGNORE)
    long[] insertMovies(Movie... movies);

    @Insert(onConflict = REPLACE)
    long insertMovie(Movie movie);

    @Query("UPDATE movies SET title = :title, overview = :overview, release_date = :release_date, " +
            "poster_path = :poster_path, backdrop_path = :backdrop_path, " +
            "vote_average = :vote_average, original_language = :original_language " +
            "WHERE id = :id")
    void updateMovies(String id, String title, String overview, String release_date, String poster_path, String backdrop_path, String vote_average, String original_language);

    @Query("SELECT * FROM movies LIMIT (:page * 20)")
    LiveData<List<Movie>> getMovies(int page);

}
