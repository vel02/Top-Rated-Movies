package kiz.learnwithvel.topratedmovies.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.rxjava3.core.Flowable;
import kiz.learnwithvel.topratedmovies.request.respond.ApiResponse;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

@EverythingIsNonNull
public class FlowableAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        if (CallAdapter.Factory.getRawType(returnType) != Flowable.class)
            return null;

        Type observableType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);

        Type rawObservableType = CallAdapter.Factory.getRawType(observableType);
        if (rawObservableType != ApiResponse.class) {
            throw new IllegalArgumentException("Type must be a defined resource");
        }

        if (!(observableType instanceof ParameterizedType))
            throw new IllegalArgumentException("resource must be parameterized");

        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) observableType);
        return new FlowableCallAdapter<Type>(bodyType);
    }
}
