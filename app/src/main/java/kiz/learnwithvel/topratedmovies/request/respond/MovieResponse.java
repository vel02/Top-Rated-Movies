package kiz.learnwithvel.topratedmovies.request.respond;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import kiz.learnwithvel.topratedmovies.model.Movie;

public class MovieResponse {

    @SerializedName("page")
    @Expose()
    private String page;
    @SerializedName("total_results")
    @Expose()
    private String total_results;
    @SerializedName("total_pages")
    @Expose()
    private String total_pages;
    @SerializedName("results")
    @Expose()
    private List<Movie> movies;
    @SerializedName("success")
    @Expose()
    private String success;

    @Override
    public String toString() {
        return "MovieResponse{" +
                "page='" + page + '\'' +
                ", total_results='" + total_results + '\'' +
                ", total_pages='" + total_pages + '\'' +
                ", movies=" + movies +
                ", success='" + success + '\'' +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public String getPage() {
        return page;
    }

    public String getTotal_results() {
        return total_results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
