package kiz.learnwithvel.topratedmovies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "release_date")
    private String release_date;
    @ColumnInfo(name = "poster_path")
    private String poster_path;
    @ColumnInfo(name = "backdrop_path")
    private String backdrop_path;
    @ColumnInfo(name = "vote_average")
    private String vote_average;
    @ColumnInfo(name = "original_language")
    private String original_language;
    @ColumnInfo(name = "type_request")
    private String type_request;
    @ColumnInfo(name = "timestamp")
    private int timestamp;
    @ColumnInfo(name = "top_rated_total")
    private int top_rated_total;


    public Movie(int id, String title, String overview, String release_date,
                 String poster_path, String backdrop_path, String vote_average,
                 String original_language) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.original_language = original_language;
        this.timestamp = 0;
    }

    public Movie() {
        this.timestamp = 0;
    }

    @NotNull
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", original_language='" + original_language + '\'' +
                ", type_request='" + type_request + '\'' +
                ", timestamp=" + timestamp +
                ", top_rated_total=" + top_rated_total +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getType_request() {
        return type_request;
    }

    public void setType_request(String type_request) {
        this.type_request = type_request;
    }

    public int getTop_rated_total() {
        return top_rated_total;
    }

    public void setTop_rated_total(int top_rated_total) {
        this.top_rated_total = top_rated_total;
    }
}
