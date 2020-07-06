package kiz.learnwithvel.topratedmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import kiz.learnwithvel.topratedmovies.model.Video;
import kiz.learnwithvel.topratedmovies.repository.MovieRepository;
import kiz.learnwithvel.topratedmovies.util.Resource;

public class VideoListViewModel extends AndroidViewModel {
    public static final String QUERY_EXHAUSTED = "No more results.";
    private static final String TAG = "VideoListViewModel";
    private MediatorLiveData<Resource<List<Video>>> videos = new MediatorLiveData<>();
    private MovieRepository repository;

    private boolean isPerformingQuery;
    private boolean isQueryExhausted;
    private String id;
    private String language;


    public VideoListViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance(application);
    }


    public LiveData<Resource<List<Video>>> getVideos() {
        return videos;
    }

    public void getVideos(String id) {
        if (!isPerformingQuery) {
            this.id = id;
            this.language = "en-US";
            this.isQueryExhausted = false;
            executeVideo();
        }
    }

    private void executeVideo() {
        this.isPerformingQuery = true;
        final LiveData<Resource<List<Video>>> source = repository.getVideosApi(id, language);
        videos.addSource(source, new Observer<Resource<List<Video>>>() {
            @Override
            public void onChanged(Resource<List<Video>> listResource) {
                if (listResource != null) {
                    videos.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        isPerformingQuery = false;
                        if (listResource.data != null && listResource.data.size() == 0) {
                            isQueryExhausted = true;
                            videos.setValue(new Resource<>(
                                    Resource.Status.ERROR,
                                    listResource.data,
                                    QUERY_EXHAUSTED
                            ));
                        }
                        videos.removeSource(source);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        videos.removeSource(source);
                    }
                } else {
                    isPerformingQuery = false;
                    videos.removeSource(source);
                }
            }
        });
    }
}
