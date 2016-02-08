package org.themoviedb.movieinfo;

import android.test.ApplicationTestCase;

import org.themoviedb.movieinfo.di.component.ApplicationComponent;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<MovieApplication> {

    private MovieApplication mApplication;

    public ApplicationTest() {
        super(MovieApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        mApplication = getApplication();
    }

    public void testApplicationComponentNotNull() {
        ApplicationComponent appComponent = mApplication.getApplicationComponent();
        assertNotNull(appComponent);
    }
}