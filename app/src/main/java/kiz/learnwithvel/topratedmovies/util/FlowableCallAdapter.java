package kiz.learnwithvel.topratedmovies.util;

import org.reactivestreams.Subscriber;

import java.lang.reflect.Type;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import kiz.learnwithvel.topratedmovies.request.respond.ApiResponse;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

@EverythingIsNonNull
public class FlowableCallAdapter<R> implements CallAdapter<R, Flowable<ApiResponse<R>>> {

    private Type responseType;

    public FlowableCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Flowable<ApiResponse<R>> adapt(Call<R> call) {
        return new Flowable<ApiResponse<R>>() {
            @Override
            protected void subscribeActual(@NonNull Subscriber<? super ApiResponse<R>> subscriber) {
                final ApiResponse<R> apiResponse = new ApiResponse<>();
                call.enqueue(new Callback<R>() {
                    @Override
                    public void onResponse(Call<R> call, Response<R> response) {
                        subscriber.onNext(apiResponse.success(response));
                    }

                    @Override
                    public void onFailure(Call<R> call, Throwable t) {
                        subscriber.onNext(apiResponse.error(t));
                    }
                });
            }
        };
    }
}
