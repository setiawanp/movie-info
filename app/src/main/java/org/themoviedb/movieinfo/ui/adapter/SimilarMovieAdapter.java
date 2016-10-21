package org.themoviedb.movieinfo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.themoviedb.movieinfo.R;

/**
 * Created by Setiawan Paiman on 21/10/16.
 */

public class SimilarMovieAdapter extends MovieAdapter {

    public SimilarMovieAdapter(@NonNull Context context, @NonNull OnClickListener onClickListener) {
        super(context, onClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_similar_movie, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onThumbnailClicked(getData().get(viewHolder.getAdapterPosition()));
                }
            });
            return viewHolder;
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }
}
