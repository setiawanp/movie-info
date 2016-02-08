package org.themoviedb.movieinfo.di.component;

import org.themoviedb.movieinfo.di.module.MockApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MockApplicationModule.class)
public interface MockApplicationComponent extends ApplicationComponent {
}
