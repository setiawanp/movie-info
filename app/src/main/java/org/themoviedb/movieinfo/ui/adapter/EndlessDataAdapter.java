package org.themoviedb.movieinfo.ui.adapter;

public interface EndlessDataAdapter<T> extends DataAdapter<T> {

    boolean isNextPageAvailable();
}
