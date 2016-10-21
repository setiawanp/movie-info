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
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements EndlessDataAdapter<PaginatedList<Movie>> {

    protected static final int ITEM_VIEW_TYPE_ITEM = 0;
    protected static final int ITEM_VIEW_TYPE_FOOTER = 1;

    protected OnClickListener mOnClickListener;
    private final Context mContext;
    private final PaginatedList<Movie> mList;
    private boolean mFooterVisible = false;

    public MovieAdapter(@NonNull Context context, @NonNull OnClickListener onClickListener) {
        mContext = context;
        mList = new PaginatedList<>();
        mOnClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_movie, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onThumbnailClicked(getData().get(viewHolder.getAdapterPosition()));
                }
            });
            return viewHolder;
        } else {
            View view = inflater.inflate(R.layout.item_footer, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Movie movie = getData().get(position);

            if (movie != null) {
                Picasso.with(mContext)
                        .load(movie.getPosterUrl())
                        .placeholder(R.color.gray)
                        .error(R.drawable.bg_broken_image)
                        .resize(200, 280)
                        .into(viewHolder.mThumbnail);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.getData().size()) return ITEM_VIEW_TYPE_FOOTER;
        else return ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mList.getData().size() + (isFooterVisible()? 1 : 0);
    }

    @Override
    public boolean isNextPageAvailable() {
        return mList.isNextPageAvailable();
    }

    @Override
    public boolean hasData() {
        return !mList.getData().isEmpty();
    }

    @Override
    public PaginatedList<Movie> getData() {
        return mList;
    }

    @Override
    public void addData(PaginatedList<Movie> list, boolean refresh) {
        if (refresh) {
            mList.set(list);
            notifyDataSetChanged();
        }
        else {
            int totalCount = getItemCount();
            int addedCount = list.getData().size();
            mList.add(list);
            notifyItemRangeInserted(totalCount, addedCount);
        }
    }

    @Override
    public void clearData() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setFooterVisible(boolean footerVisible) {
        if (isFooterVisible() != footerVisible) {
            mFooterVisible = footerVisible;
            int pos = mList.getData().size();
            if (mFooterVisible) notifyItemInserted(pos);
            else notifyItemRemoved(pos);
        }
    }

    public boolean isFooterVisible() {
        return mFooterVisible;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        public ImageView mThumbnail;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnClickListener {

        void onThumbnailClicked(Movie movie);
    }
}
