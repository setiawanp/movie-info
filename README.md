# Movie Info

Movie Information Android Application. 
All data in the application are retrieved using the API provided by [http://themoviedb.org](http://themoviedb.org), while the images were taken from [Google's Material Design Icons](https://design.google.com/icons/).

Third-party libraries used:
- [Dagger 2](http://google.github.io/dagger/) - Dependency Injector
- [Retrofit](http://square.github.io/retrofit/) - REST Client
- [RxJava](https://github.com/ReactiveX/RxJava) - Reactive Functional Programming Framework
- [RxAndroid](https://github.com/ReactiveX/RxAndroid) - RxJava Bindings for Android
- [Gson](https://github.com/google/gson) - JSON Serialization Library
- [ButterKnife](http://jakewharton.github.io/butterknife/) - Android View Injector
- [Timber](https://github.com/JakeWharton/timber) - Convenience Logger for Android
- [DbFlow](https://github.com/Raizlabs/DBFlow) - Sqlite ORM
- [Picasso](http://square.github.io/picasso/) - Image Loader Library
- [JUnit](http://junit.org/) - Java Unit Test Framework
- [Hamcrest](http://hamcrest.org/JavaHamcrest/) - Matcher Library for Unit Test
- [Mockito](http://mockito.org/) - Mocking Framework
- [Robolectric](http://robolectric.org/) - Android Unit Test Framework

**Note**: Currently, the application had been tested to run only in Debug mode. Issues may appear if it was run in Release mode, since `proguard-rules` may strip and obfuscate some classes and methods.

To build the app in Debug mode, please run this in the command line of the application directory:
```
./gradlew assembleDebug
```

To execute unit tests, please run:
```
./gradlew test
```
The report of the unit tests could be found in `{appDirectory}/app/build/reports/tests/debug/index.html`

**Note**: Running the unit tests in Android Studio may require some changes in JUnit configuration since there is an [issue](https://github.com/robolectric/robolectric/issues/1648#issuecomment-113731011) where `RobolectricGradleTestRunner` could not find `AndroidManifest.xml`.
