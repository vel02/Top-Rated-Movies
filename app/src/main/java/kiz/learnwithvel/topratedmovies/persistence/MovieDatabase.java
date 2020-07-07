package kiz.learnwithvel.topratedmovies.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kiz.learnwithvel.topratedmovies.model.Movie;
import kiz.learnwithvel.topratedmovies.model.Video;

@Database(entities = {Movie.class, Video.class}, version = 10)
public abstract class MovieDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "top_movies_db";

    private static MovieDatabase instance;

    public static MovieDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MovieDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract MoviesDao getMoviesDao();

}
