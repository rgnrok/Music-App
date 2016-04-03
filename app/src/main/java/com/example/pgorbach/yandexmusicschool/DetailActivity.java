package com.example.pgorbach.yandexmusicschool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.pgorbach.yandexmusicschool.adapters.ArtistAdapter;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {

    public static final String ARG_ARTIST = "artist";

    @Bind(R.id.detail_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.header_image)
    ImageView mIvArtistImage;

    @Bind(R.id.artist_genres)
    TextView mTvGenres;

    @Bind(R.id.artist_tracks)
    TextView mTvTracks;

    @Bind(R.id.artist_desc)
    TextView mTvArtistDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Artist mArtist =  getIntent().getParcelableExtra(DetailActivity.ARG_ARTIST);

            if (mArtist != null) {
                mCollapsingToolbar.setTitle(mArtist.name);
                mTvGenres.setText(mArtist.getGenresAsString());
                mTvArtistDescription.setText(mArtist.description + mArtist.description + mArtist.description + mArtist.description);

                StringBuilder mBuilder = new StringBuilder();
                mBuilder.setLength(0);
                mBuilder.append(getResources().getQuantityString(R.plurals.albums_count, mArtist.albums, mArtist.albums))
                        .append(',')
                        .append(getResources().getQuantityString(R.plurals.tracks_count, mArtist.tracks, mArtist.tracks));
                mTvTracks.setText(mBuilder.toString());

                Glide.with(this)
                        .load(mArtist.cover.get(Artist.COVER_BIG))
                        .asBitmap()
                        .centerCrop()
                        .into(new BitmapImageViewTarget(mIvArtistImage) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @SuppressWarnings("ResourceType")
                            @Override
                            public void onGenerated(Palette palette) {

                                int mutedColor = palette.getMutedColor(R.color.colorPrimary);
                                mCollapsingToolbar.setContentScrimColor(mutedColor);
                                mCollapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
                            }
                        });
                    }
                });

            }

            }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}