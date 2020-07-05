package kiz.learnwithvel.topratedmovies.util;

import androidx.lifecycle.MediatorLiveData;

import java.util.LinkedList;
import java.util.Queue;

import kotlin.jvm.Synchronized;

public class ValueKeeperLiveData<T> extends MediatorLiveData<T> {
    private Queue<T> queueValues = new LinkedList<>();

    @Synchronized
    @Override
    public void postValue(T value) {
        queueValues.offer(value);
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        queueValues.remove(value);
        queueValues.offer(value);
        while (!queueValues.isEmpty())
            super.setValue(queueValues.poll());
    }
}
