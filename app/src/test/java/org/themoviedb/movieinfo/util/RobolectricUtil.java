package org.themoviedb.movieinfo.util;

import org.robolectric.Robolectric;

public final class RobolectricUtil {

    public static void forceScheduler() {
        Robolectric.flushForegroundThreadScheduler();
        Robolectric.getForegroundThreadScheduler().idleConstantly(true);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Robolectric.flushForegroundThreadScheduler();
        Robolectric.getForegroundThreadScheduler().idleConstantly(true);
    }
}
