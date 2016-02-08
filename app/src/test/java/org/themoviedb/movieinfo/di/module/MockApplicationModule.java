package org.themoviedb.movieinfo.di.module;

import android.support.annotation.NonNull;

import org.mockito.Mockito;
import org.themoviedb.movieinfo.domain.api.ApiFactory;
import org.themoviedb.movieinfo.domain.api.MovieApi;
import org.themoviedb.movieinfo.domain.service.IMovieService;
import org.themoviedb.movieinfo.domain.service.MovieService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockApplicationModule {

    private String mBaseUrl;

    public MockApplicationModule(@NonNull String baseUrl) {
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
        return Mockito.mock(MovieApi.class);
    }

    @Provides
    @Singleton
    IMovieService providesMovieService(MovieApi movieApi) {
        return Mockito.mock(MovieService.class);
    }
}
