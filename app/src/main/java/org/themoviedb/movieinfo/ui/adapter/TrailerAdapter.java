package org.themoviedb.movieinfo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.squareup.picasso.Picasso;

import org.themoviedb.movieinfo.R;
import org.themoviedb.movieinfo.data.model.YoutubeVideo;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private List<YoutubeVideo> mVideos;
    private OnClickListener mOnClickListener;

    public TrailerAdapter(@NonNull Context context, @NonNull OnClickListener onClickListener) {
        mContext = context;
        mOnClickListener = onClickListener;
        mVideos = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_trailer, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onVideoClicked(mVideos.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        YoutubeVideo video = mVideos.get(position);
        Picasso.with(mContext)
                .load(video.getThumbnailUrl())
                .placeholder(R.color.gray)
                .error(R.drawable.bg_broken_image)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void setVideos(List<YoutubeVideo> videos) {
        mVideos.clear();
        if (videos != null) mVideos.addAll(videos);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {

        void onVideoClicked(YoutubeVideo video);
    }
}
