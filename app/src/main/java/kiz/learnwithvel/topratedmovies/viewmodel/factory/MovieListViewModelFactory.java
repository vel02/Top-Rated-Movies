package kiz.learnwithvel.topratedmovies.viewmodel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import kiz.learnwithvel.topratedmovies.viewmodel.MovieListViewModel;

public class MovieListViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    public MovieListViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieListViewModel(application);
    }
}
