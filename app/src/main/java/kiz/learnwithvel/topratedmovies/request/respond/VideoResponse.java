package kiz.learnwithvel.topratedmovies.request.respond;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import kiz.learnwithvel.topratedmovies.model.Video;

public class VideoResponse {

    @SerializedName("id")
    @Expose()
    private String id;

    @SerializedName("results")
    @Expose()
    private List<Video> videos;

    @SerializedName("success")
    @Expose()
    private String success;


    @Override
    public String toString() {
        return "VideoResponse{" +
                "id='" + id + '\'' +
                ", videos=" + videos +
                ", success='" + success + '\'' +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public String getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
