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
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        Logger.e("firstVisibleItem " + firstVisibleItem + ", " + prevFirstItemVisible);
        if (firstVisibleItem <= prevFirstItemVisible) {
            if(!controlsVisible) {
                onShow();
                controlsVisible = true;
            }
        } else {
            if (dy >= 0) {
                if (controlsVisible) {
                    onHide();
                    controlsVisible = false;
                }
            } else {
                if (!controlsVisible) {
                    onShow();
                    controlsVisible = true;
                }
            }
        }
        prevFirstItemVisible = firstVisibleItem;
    }

    public abstract void onHide();
    public abstract void onShow();

}