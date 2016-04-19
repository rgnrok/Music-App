package com.example.pgorbach.yandexmusicschool.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pgorbach.yandexmusicschool.DetailActivity;
import com.example.pgorbach.yandexmusicschool.MainActivity;
import com.example.pgorbach.yandexmusicschool.R;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> implements Filterable {

    protected List<Artist> mItems;
    protected List<Artist> mFilteredItems;
    protected FilterFinishListener mFilterListener;

    //Position for animation
    protected int lastPosition = -1;

    //Called when the available search results
    public interface FilterFinishListener {
        void onFilterFinish();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Context mContext;
        protected Artist mArtist;

        @Bind(R.id.list_item_wrapper)
        RelativeLayout mRlWrapper;

        @Bind(R.id.artist_image)
        ImageView mIvArtistImage;

        @Bind(R.id.artist_name)
        TextView mTvArtistName;

        @Bind(R.id.artist_genres)
        TextView mTvArtistGenres;

        @Bind(R.id.artist_tracks)
        TextView mTvArtistTracks;

        public ViewHolder(View v, Context context) {
            super(v);
            mContext = context;
            ButterKnife.bind(this, itemView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Open Detail activity with artist info
            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra(DetailActivity.ARG_ARTIST, mArtist);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mContext.startActivity(detailIntent, ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity) mContext, mIvArtistImage, mContext.getString(R.string.artist_transition_name)).toBundle());
            } else {
                mContext.startActivity(detailIntent);
            }
        }
    }

    public ArtistAdapter() {
        mItems = new ArrayList<>();
        mFilteredItems = new ArrayList<>();
    }

    public ArtistAdapter(List<Artist> artists) {
        mItems = artists;
        mFilteredItems = mItems;
    }

    public ArtistAdapter(Artist[] artists) {
        this(Arrays.asList(artists));
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_item, parent, false);
        return new ViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Set artist info and load image
        final Artist mArtist = mFilteredItems.get(position);
        holder.mArtist = mArtist;
        holder.mTvArtistName.setText(mArtist.name);
        holder.mTvArtistGenres.setText(mArtist.getGenresAsString());

        StringBuilder mBuilder = new StringBuilder();
        mBuilder.append(holder.mContext.getResources().getQuantityString(R.plurals.albums_count, mArtist.albums, mArtist.albums))
                .append(", ")
                .append(holder.mContext.getResources().getQuantityString(R.plurals.tracks_count, mArtist.tracks, mArtist.tracks));
        holder.mTvArtistTracks.setText(mBuilder.toString());

        Glide.with(holder.mContext)
                .load(mArtist.cover.get(Artist.COVER_SMALL))
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvArtistImage);

        setAnimation(holder.mRlWrapper, position, holder.mContext);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.mRlWrapper.clearAnimation();
    }

    public void setFilterListener(FilterFinishListener listener) {
        mFilterListener = listener;
    }

    /**
     * Here is the key method to apply the animation
     */
    protected void setAnimation(View viewToAnimate, int position, Context context) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFilteredItems.size();
    }

    public void clear() {
        mItems.clear();
        mFilteredItems.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Artist> artists) {
        mItems.addAll(artists);
        mFilteredItems.addAll(artists);
        notifyDataSetChanged();
    }

    //For remove animation
    public Artist removeItem(int position) {
        final Artist model = mFilteredItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    //For insert animation
    public void addItem(int position, Artist model) {
        mFilteredItems.add(position, model);
        notifyItemInserted(position);
    }

    //For move animation
    public void moveItem(int fromPosition, int toPosition) {
        final Artist model = mFilteredItems.remove(fromPosition);
        mFilteredItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Artist> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    protected void applyAndAnimateRemovals(List<Artist> newModels) {
        for (int i = mFilteredItems.size() - 1; i >= 0; i--) {
            final Artist model = mFilteredItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    protected void applyAndAnimateAdditions(List<Artist> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Artist model = newModels.get(i);
            if (!mFilteredItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    protected void applyAndAnimateMovedItems(List<Artist> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Artist model = newModels.get(toPosition);
            final int fromPosition = mFilteredItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new ArtistFilter(this, mItems);
    }

    protected static class ArtistFilter extends Filter {

        protected final ArtistAdapter adapter;
        protected final List<Artist> originalList;
        protected final List<Artist> filteredList;

        protected ArtistFilter(ArtistAdapter adapter, List<Artist> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            filteredList.clear();

            //If search text is empty
            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                //Search by string
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Artist artist : originalList) {
                    if (artist.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(artist);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.animateTo((ArrayList<Artist>) results.values);
            if (adapter.mFilterListener != null) {
                adapter.mFilterListener.onFilterFinish();
            }
        }
    }
}
