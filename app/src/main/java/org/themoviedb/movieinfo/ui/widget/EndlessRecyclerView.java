package org.themoviedb.movieinfo.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.themoviedb.movieinfo.ui.adapter.EndlessDataAdapter;

public class EndlessRecyclerView extends RecyclerView {

    private int mPreviousTotal = 0;

    private boolean mLoading = false;

    private EndlessDataAdapter mEndlessDataAdapter;

    private OnLoadMoreListener mOnLoadMoreListener = null;

    private OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager == null || mOnLoadMoreListener == null) {
                return;
            }

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
            boolean nextPageAvailable =
                    mEndlessDataAdapter == null || mEndlessDataAdapter.isNextPageAvailable();

            if (mLoading && mPreviousTotal != totalItemCount) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }

            if (!mLoading && nextPageAvailable &&
                    (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                mLoading = true;
                mPreviousTotal = totalItemCount;
                mOnLoadMoreListener.onLoadMore();
            }
        }
    };

    public EndlessRecyclerView(Context context) {
        super(context);
        init();
    }

    public EndlessRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EndlessRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        setEndlessDataAdapter(adapter);
    }

    public void enableLoadMore() {
        mPreviousTotal = Integer.MIN_VALUE;
        mLoading = true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private void setLoadMoreScrollListener() {
        removeOnScrollListener(mOnScrollListener);
        addOnScrollListener(mOnScrollListener);
    }

    private void setEndlessDataAdapter(Adapter adapter) {
        if (adapter instanceof EndlessDataAdapter) {
            mEndlessDataAdapter = (EndlessDataAdapter) adapter;
        }
    }

    private void init() {
        setLoadMoreScrollListener();
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }
}
