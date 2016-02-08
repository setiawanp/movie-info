package org.themoviedb.movieinfo.ui.adapter;

public interface DataAdapter<T> {

    boolean hasData();
    T getData();
    void addData(T data, boolean refresh);
    void clearData();
}
