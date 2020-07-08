package kiz.learnwithvel.topratedmovies.util;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kiz.learnwithvel.topratedmovies.AppExecutor;
import kiz.learnwithvel.topratedmovies.request.respond.ApiResponse;

public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";
    private final AppExecutor appExecutor;
    private MediatorLiveData<Resource<CacheObject>> result = new MediatorLiveData<>();

    protected NetworkBoundResource(AppExecutor appExecutor) {
        this.appExecutor = appExecutor;
        init();
    }

    private void init() {
        result.setValue(Resource.loading(null));

        final LiveData<CacheObject> dbSource = loadFromDb();

        result.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                result.removeSource(dbSource);
                if (shouldFetch(cacheObject)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    private LiveData<ApiResponse<RequestObject>> createCallResponse() {

        MediatorLiveData<ApiResponse<RequestObject>> result = new MediatorLiveData<>();

        final LiveData<ApiResponse<RequestObject>> source = LiveDataReactiveStreams.fromPublisher(
                createCall()
                        .onErrorReturn(new Function<Throwable, RequestObject>() {
                            @Override
                            public RequestObject apply(Throwable throwable) throws Throwable {
                                return (RequestObject) new ApiResponse.ApiErrorResponse<RequestObject>(throwable.getMessage());
                            }
                        })
                        .map(new Function<RequestObject, ApiResponse<RequestObject>>() {
                            @Override
                            public ApiResponse<RequestObject> apply(RequestObject requestObject) throws Throwable {
                                if (requestObject instanceof ApiResponse.ApiErrorResponse) {
                                    return new ApiResponse.ApiErrorResponse<>(((ApiResponse.ApiErrorResponse) requestObject).getMessage());
                                }
                                return new ApiResponse.ApiSuccessResponse<>(requestObject);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );

        result.addSource(source, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(ApiResponse<RequestObject> requestObjectApiResponse) {
                result.setValue(requestObjectApiResponse);
                result.removeSource(source);
            }
        });

        return result;
    }

    private void fetchFromNetwork(LiveData<CacheObject> dbSource) {
        result.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCallResponse();

        result.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(ApiResponse<RequestObject> requestObjectApiResponse) {
                result.removeSource(dbSource);
                result.removeSource(apiResponse);

                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {

                    appExecutor.getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCallResult((RequestObject)
                                    processResponse((ApiResponse.ApiSuccessResponse<RequestObject>) requestObjectApiResponse));

                            appExecutor.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    result.addSource(loadFromDb(), new Observer<CacheObject>() {
                                        @Override
                                        public void onChanged(CacheObject cacheObject) {
                                            setValue(Resource.success(cacheObject));
                                        }
                                    });
                                }
                            });
                        }
                    });

                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                    appExecutor.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            result.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });

                } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                    result.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setValue(Resource.error(((ApiResponse.ApiErrorResponse<RequestObject>)
                                    requestObjectApiResponse).getMessage(), cacheObject));
                        }
                    });
                }

            }
        });
    }

    private void setValue(Resource<CacheObject> newValue) {
        if (result.getValue() != newValue) result.setValue(newValue);
    }


    public LiveData<Resource<CacheObject>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected CacheObject processResponse(ApiResponse.ApiSuccessResponse<RequestObject> response) {
        return (CacheObject) response.getBody();
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    @NonNull
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    @NonNull
    @MainThread
    protected abstract Flowable<RequestObject> createCall();

}
