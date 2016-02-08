package org.themoviedb.movieinfo.data.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PaginatedList<T> {

    @SerializedName("results")
    private List<T> mData;

    @SerializedName("page")
    private int mCurrentPage;

    @SerializedName("total_pages")
    private int mTotalPages;

    public PaginatedList() {
        this(null, 0, 0);
    }

    public PaginatedList(List<T> data, int currentPage, int totalPages) {
        mData = new ArrayList<>();
        if (data != null) mData.addAll(data);
        mCurrentPage = currentPage;
        mTotalPages = totalPages;
    }

    public List<T> getData() {
        // defensive copy
        return new ArrayList<>(mData);
    }

    @Nullable
    public T get(int position) {
        if (position >= mData.size()) return null;

        return mData.get(position);
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void add(PaginatedList<T> list) {
        if (list != null && list.getData() != null) {
            mData.addAll(list.getData());
            mCurrentPage = list.getCurrentPage();
            mTotalPages = list.getTotalPages();
        }
    }

    public void set(PaginatedList<T> list) {
        clear();
        add(list);
    }

    public void clear() {
        mData.clear();
        mCurrentPage = 0;
        mTotalPages = 0;
    }

    public boolean isNextPageAvailable() {
        return mCurrentPage < mTotalPages;
    }
}
