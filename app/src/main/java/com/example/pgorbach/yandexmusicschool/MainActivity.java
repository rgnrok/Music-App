package com.example.pgorbach.yandexmusicschool;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pgorbach.yandexmusicschool.adapters.ArtistAdapter;
import com.example.pgorbach.yandexmusicschool.api.ApiFactory;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.artist_list)
    RecyclerView mRvArtists;

    @Bind(R.id.swiperefresh)
    SwipeRefreshLayout mRefreshLayout;

    private RecyclerView.Adapter mArtistAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRvArtists.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRvArtists.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mArtistAdapter = new ArtistAdapter();
        mRvArtists.setAdapter(mArtistAdapter);


        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Logger.d("onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateList();
                    }
                }
        );


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void updateList() {
//        Call<Artist> call = ApiFactory.getArtistService().listArtists();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
