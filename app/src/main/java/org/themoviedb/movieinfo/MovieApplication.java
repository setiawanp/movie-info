package org.themoviedb.movieinfo;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.themoviedb.movieinfo.di.component.ApplicationComponent;
import org.themoviedb.movieinfo.di.component.DaggerApplicationComponent;
import org.themoviedb.movieinfo.di.module.ApplicationModule;

import timber.log.Timber;

public class MovieApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(BuildConfig.BASE_URL_API))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
