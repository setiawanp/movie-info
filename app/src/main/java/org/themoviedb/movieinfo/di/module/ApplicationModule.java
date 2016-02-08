package org.themoviedb.movieinfo.di.module;

import android.support.annotation.NonNull;

import org.themoviedb.movieinfo.domain.api.ApiFactory;
import org.themoviedb.movieinfo.domain.api.MovieApi;
import org.themoviedb.movieinfo.domain.service.IMovieService;
import org.themoviedb.movieinfo.domain.service.MovieService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private String mBaseUrl;

    public ApplicationModule(@NonNull String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    ApiFactory providesApiFactory() {
        return new ApiFactory(mBaseUrl);
    }

    @Provides
    @Singleton
    MovieApi providesMovieApi(ApiFactory apiFactory) {
        return apiFactory.create(MovieApi.class);
    }

    @Provides
    @Singleton
    IMovieService providesMovieService(MovieApi movieApi) {
        return new MovieService(movieApi);
    }
}
