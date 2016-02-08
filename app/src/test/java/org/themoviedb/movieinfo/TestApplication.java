package org.themoviedb.movieinfo;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.themoviedb.movieinfo.di.component.ApplicationComponent;
import org.themoviedb.movieinfo.di.component.DaggerMockApplicationComponent;
import org.themoviedb.movieinfo.di.module.MockApplicationModule;

public class TestApplication extends MovieApplication {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        mApplicationComponent = DaggerMockApplicationComponent.builder()
                .mockApplicationModule(new MockApplicationModule(BuildConfig.BASE_URL_API))
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
