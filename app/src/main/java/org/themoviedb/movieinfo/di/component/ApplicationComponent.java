package org.themoviedb.movieinfo.di.component;

import org.themoviedb.movieinfo.di.module.ApplicationModule;
import org.themoviedb.movieinfo.domain.service.IMovieService;
import org.themoviedb.movieinfo.ui.activity.MovieDetailActivity;
import org.themoviedb.movieinfo.ui.fragment.MovieListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    IMovieService providesMovieService();

    void inject(MovieListFragment movieListFragment);

    void inject(MovieDetailActivity movieDetailActivity);
}
