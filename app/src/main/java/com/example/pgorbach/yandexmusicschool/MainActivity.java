package com.example.pgorbach.yandexmusicschool;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.pgorbach.yandexmusicschool.adapters.ArtistAdapter;
import com.example.pgorbach.yandexmusicschool.api.ApiFactory;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.artist_list)
    RecyclerView mRvArtists;

    @Bind(R.id.swiperefresh)
    SwipeRefreshLayout mRefreshLayout;

    private ArtistAdapter mArtistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setAllowEnterTransitionOverlap(true);
        }
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mRvArtists.setHasFixedSize(true);
        mRvArtists.addItemDecoration(new SimpleDividerItemDecoration(this));
        mRvArtists.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mArtistAdapter = new ArtistAdapter();
        mArtistAdapter.setFilterListener(new ArtistAdapter.FilterFinishListener() {
            @Override
            public void onFilterFinish() {
                mRvArtists.scrollToPosition(0);
            }
        });
        mRvArtists.setAdapter(mArtistAdapter);
        updateList();
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateList();
                    }
                }
        );


    }

    protected void updateList() {
        Call<List<Artist>> call = ApiFactory.getArtistService(this).listArtists();
        call.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                if (response.isSuccessful()) {
                    Logger.e("updateList");
                    mArtistAdapter.clear();
                    mArtistAdapter.addAll(response.body());
                    mRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.sync_error), Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                Logger.e("onQueryTextSubmit " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                mArtistAdapter.getFilter().filter(text);
                Logger.e("onQueryTextChange " + text);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchViewItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        Logger.e("onMenuItemActionExpand");
                        mRefreshLayout.setEnabled(false);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        Logger.e("onMenuItemActionCollapse");
                        mRefreshLayout.setEnabled(true);
                        // Return true to allow the action view to collapse
                        return true;
                    }
                });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                updateList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
