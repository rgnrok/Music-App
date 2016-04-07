package com.example.pgorbach.yandexmusicschool.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pgorbach.yandexmusicschool.DetailActivity;
import com.example.pgorbach.yandexmusicschool.R;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ArtistAdapter extends RecyclerView.Adapter <ArtistAdapter.ViewHolder> implements Filterable {

    protected List<Artist> mItems;
    protected List<Artist> mFilteredItems;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Context mContext;
        protected Artist mArtist;

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
            Logger.e("SET ON CLI!");
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra(DetailActivity.ARG_ARTIST, mArtist);
            mContext.startActivity(detailIntent);
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
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v, parent.getContext());
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Artist mArtist = mFilteredItems.get(position);
        holder.mArtist = mArtist;
        holder.mTvArtistName.setText(mArtist.name);
        holder.mTvArtistGenres.setText(mArtist.getGenresAsString());

        StringBuilder mBuilder = new StringBuilder();
        mBuilder.setLength(0);
        mBuilder.append(holder.mContext.getResources().getQuantityString(R.plurals.albums_count, mArtist.albums, mArtist.albums))
                .append(',')
                .append(holder.mContext.getResources().getQuantityString(R.plurals.tracks_count, mArtist.tracks, mArtist.tracks));
        holder.mTvArtistTracks.setText(mBuilder.toString());

        holder.mTvArtistName.setText(mArtist.name);

        Glide.with(holder.mContext)
                .load(mArtist.cover.get(Artist.COVER_SMALL))
                .centerCrop()
                .crossFade()
                .into(holder.mIvArtistImage);


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

    @Override
    public Filter getFilter() {
        return new ArtistFilter(this, mItems);
    }

    private static class ArtistFilter extends Filter {

        private final ArtistAdapter adapter;

        private final List<Artist> originalList;

        private final List<Artist> filteredList;

        private ArtistFilter(ArtistAdapter adapter, List<Artist> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Artist artist : originalList) {
                    if (artist.name.toLowerCase().contains(filterPattern.toLowerCase())) {
                        filteredList.add(artist);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            Logger.e("F r " + results.count);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Logger.e("publishResults ");
            adapter.mFilteredItems.clear();
            adapter.mFilteredItems.addAll((ArrayList<Artist>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
