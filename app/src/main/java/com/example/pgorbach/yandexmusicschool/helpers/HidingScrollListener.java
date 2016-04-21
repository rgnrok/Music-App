package com.example.pgorbach.yandexmusicschool.helpers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orhanobut.logger.Logger;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private boolean controlsVisible = true;
    private int prevFirstItemVisible=0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        Logger.e("dy  " + dy);
        int totalCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
        if (totalCount <= 5) {
            if (controlsVisible) {
                onHide();
                controlsVisible = false;
            }
        }
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        Logger.e("firstVisibleItem " + firstVisibleItem + ", " + prevFirstItemVisible);
        if (firstVisibleItem == 0 || firstVisibleItem < prevFirstItemVisible) {
            if(!controlsVisible) {
                onShow();
                controlsVisible = true;
            }
        } else {
if (firstVisibleItem != prevFirstItemVisible) {
    if (controlsVisible) {
        onHide();
        controlsVisible = false;
    }
}

        }
        prevFirstItemVisible = firstVisibleItem;
    }

    public abstract void onHide();
    public abstract void onShow();

}