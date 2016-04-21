package com.example.pgorbach.yandexmusicschool.helpers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


/*
 * Hides and shows the elements according to RecyclerView item count
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    public static final int MIN_VISIBLE_ITEMS_COUNT = 5;
    private boolean controlsVisible = true;
    private int prevFirstItemVisible = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int totalCount = (recyclerView.getLayoutManager()).getItemCount();

        if (totalCount <= MIN_VISIBLE_ITEMS_COUNT) {
            if (controlsVisible) {
                onHide();
                controlsVisible = false;
            }
            return;
        }

        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisibleItem < prevFirstItemVisible && firstVisibleItem != 0 ) {
            if (!controlsVisible) {
                onShow();
                controlsVisible = true;
            }
        } else {
            if (firstVisibleItem != prevFirstItemVisible || firstVisibleItem == 0) {
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