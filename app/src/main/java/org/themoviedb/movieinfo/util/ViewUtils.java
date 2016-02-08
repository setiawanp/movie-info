package org.themoviedb.movieinfo.util;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

public final class ViewUtils {

    private ViewUtils() {}

    public static void setSwipeRefreshing(@NonNull final SwipeRefreshLayout swipeRefreshLayout,
                                          final boolean refreshing) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(refreshing);
            }
        });
    }
}
