package org.themoviedb.movieinfo.domain.api;

import android.support.annotation.NonNull;

import org.themoviedb.movieinfo.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import timber.log.Timber;

public class ApiFactory {

    private static final String API_KEY_QUERY_PARAM = "api_key";

    private final Retrofit mRetrofit;

    public ApiFactory(@NonNull String baseUrl) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Timber.d(request.url().toString());
                        HttpUrl url = request.url()
                                .newBuilder()
                                .addQueryParameter(API_KEY_QUERY_PARAM, BuildConfig.API_KEY)
                                .build();
                        request = request.newBuilder().url(url).build();
                        return chain.proceed(request);
                    }
                })
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }
}
