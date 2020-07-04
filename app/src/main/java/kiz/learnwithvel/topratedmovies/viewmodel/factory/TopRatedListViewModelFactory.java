package kiz.learnwithvel.topratedmovies.viewmodel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import kiz.learnwithvel.topratedmovies.viewmodel.TopRatedListViewModel;

public class TopRatedListViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    public TopRatedListViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TopRatedListViewModel(application);
    }
}
