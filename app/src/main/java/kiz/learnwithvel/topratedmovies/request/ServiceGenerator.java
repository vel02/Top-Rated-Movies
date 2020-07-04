package kiz.learnwithvel.topratedmovies.request;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import kiz.learnwithvel.topratedmovies.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String TAG = "ServiceGenerator";
    private static final int CONNECTION_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 2;
    private static final int WRITE_TIMEOUT = 2;

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor())
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    private static HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor(message
                -> Log.d(TAG,
                "log: http log: " + message))
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static RequestApi requestApi = retrofit.create(RequestApi.class);

    public static RequestApi getRequestApi() {
        return requestApi;
    }
}
