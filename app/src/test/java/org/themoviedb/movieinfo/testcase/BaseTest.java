package org.themoviedb.movieinfo.testcase;

import org.robolectric.annotation.Config;
import org.themoviedb.movieinfo.BuildConfig;
import org.themoviedb.movieinfo.TestApplication;

@Config(application = TestApplication.class,
        constants = BuildConfig.class,
        sdk = 18)
public class BaseTest {
}
