package kiz.learnwithvel.topratedmovies.util;

import androidx.annotation.MainThread;
import androidx.lifecycle.MediatorLiveData;

import java.util.LinkedList;
import java.util.Queue;

import kotlin.jvm.Synchronized;

//https://stackoverflow.com/questions/56097647/can-we-use-livedata-without-loosing-any-value
public class ValueKeeperLiveData<T> extends MediatorLiveData<T> {
    private Queue<T> queueValues = new LinkedList<>();

    @Synchronized
    @Override
    public void postValue(T value) {
        queueValues.offer(value);
        super.postValue(value);
    }

    @MainThread
    @Synchronized
    @Override
    public void setValue(T value) {
        queueValues.remove(value);
        queueValues.offer(value);
        while (!queueValues.isEmpty())
            super.setValue(queueValues.poll());
    }
}
