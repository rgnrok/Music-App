package com.example.pgorbach.yandexmusicschool;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pgorbach.yandexmusicschool.adapters.ArtistAdapter;
import com.example.pgorbach.yandexmusicschool.api.ApiFactory;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.example.pgorbach.yandexmusicschool.helpers.HidingScrollListener;
import com.example.pgorbach.yandexmusicschool.helpers.DividerItemDecoration;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_content)
    CoordinatorLayout mClContent;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.artist_list)
    RecyclerView mRvArtists;

    @Bind(R.id.swiperefresh)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.fab_up)
    FloatingActionButton fabUp;

    @Bind(R.id.empty_search_result)
    TextView mTvEmptySearchResult;

    protected ArtistAdapter mArtistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mRvArtists.setHasFixedSize(true);
        mRvArtists.addItemDecoration(new DividerItemDecoration(this));
        mRvArtists.setLayoutManager(new LinearLayoutManager(this));


        mArtistAdapter = new ArtistAdapter();
        mArtistAdapter.setFilterListener(new ArtistAdapter.FilterFinishListener() {
            //After search complete
            @Override
            public void onFilterFinish(List<Artist> results) {
                mRvArtists.scrollToPosition(0);
                mTvEmptySearchResult.setVisibility(results.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });
        mRvArtists.setAdapter(mArtistAdapter);
        //Init load data
        updateList();

        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateList();
                    }
                }
        );

        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRvArtists.scrollToPosition(0);
            }
        });

        mRvArtists.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                fabUp.hide();
            }

            @Override
            public void onShow() {
                fabUp.show();
            }
        });
    }

    protected void updateList() {
        Call<List<Artist>> call = ApiFactory.getArtistService(this).listArtists();
        call.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                if (response.isSuccessful()) {
                    mArtistAdapter.clear();
                    mArtistAdapter.addAll(response.body());
                    mRvArtists.scrollToPosition(0);

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

        final MenuItem refreshItem = menu.findItem(R.id.menu_refresh);
        final MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                mArtistAdapter.getFilter().filter(text);
                return false;
            }
        });

        //Disable pull-refresh if searching
        MenuItemCompat.setOnActionExpandListener(searchViewItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        mRefreshLayout.setEnabled(false);
                        refreshItem.setEnabled(false);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        // Return true to allow the action view to collapse
                        mRefreshLayout.setEnabled(true);
                        refreshItem.setEnabled(true);
                        return true;
                    }
                });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                updateList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
