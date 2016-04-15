package com.example.pgorbach.yandexmusicschool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {

    public static final String ARG_ARTIST = "artist";
    private static final String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";


    @Bind(R.id.detail_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.header_image)
    ImageView mIvArtistImage;

    @Bind(R.id.header_image_gradient)
    View mArtistImageGradient;

    @Bind(R.id.artist_genres)
    TextView mTvGenres;

    @Bind(R.id.artist_tracks)
    TextView mTvTracks;

    @Bind(R.id.artist_desc)
    TextView mTvArtistDescription;

    @Bind(R.id.fab)
    FloatingActionButton fabLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Filling author info
        final Artist mArtist = getIntent().getParcelableExtra(DetailActivity.ARG_ARTIST);

        if (mArtist != null) {
            mCollapsingToolbar.setTitle(mArtist.name);
            mTvGenres.setText(mArtist.getGenresAsString());
            mTvArtistDescription.setText(mArtist.description);

            StringBuilder mBuilder = new StringBuilder();
            mBuilder.setLength(0);
            mBuilder.append(getResources().getQuantityString(R.plurals.albums_count, mArtist.albums, mArtist.albums))
                    .append(", ")
                    .append(getResources().getQuantityString(R.plurals.tracks_count, mArtist.tracks, mArtist.tracks));
            mTvTracks.setText(mBuilder.toString());

            Glide.with(this)
                    .load(mArtist.cover.get(Artist.COVER_BIG))
                    .asBitmap()
                    .centerCrop()

                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(new BitmapImageViewTarget(mIvArtistImage) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            super.onResourceReady(bitmap, anim);
                            mArtistImageGradient.setVisibility(View.VISIBLE);
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

            fabLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                    builder.setShowTitle(true);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(DetailActivity.this, Uri.parse(mArtist.link));
                }
            });

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
