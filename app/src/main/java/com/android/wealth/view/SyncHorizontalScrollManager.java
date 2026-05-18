package com.android.wealth.view;


import java.util.ArrayList;
import java.util.List;

public class SyncHorizontalScrollManager implements MarketHScrollView.UPOnScrollChangeObserver {

    private final List<MarketHScrollView> mViews = new ArrayList<>();

    public void addView(MarketHScrollView view) {
        mViews.add(view);
        if (mViews.size() > 1) {
            setScrollX(view);
        }
        view.addScrollChangeObserver(this);
    }

    public void setScrollX(MarketHScrollView view) {
        view.post(() -> view.setScrollX(mViews.get(0).getScrollX()));
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (MarketHScrollView view : mViews) {
            view.scrollTo(l, t);
        }
    }

    public void removeViews() {
        for (MarketHScrollView view : mViews) {
            view.removeScrollChangeObserver(this);
        }
        mViews.clear();
    }


}
