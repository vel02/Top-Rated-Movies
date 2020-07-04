package kiz.learnwithvel.topratedmovies.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kiz.learnwithvel.topratedmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class TopRatedMovieDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "top_movies_db";

    private static TopRatedMovieDatabase instance;

    public static TopRatedMovieDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TopRatedMovieDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract TopRatedMovieDao getTopRatedMoviesDao();

}
