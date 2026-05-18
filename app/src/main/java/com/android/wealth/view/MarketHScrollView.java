package com.android.wealth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MarketHScrollView extends HorizontalScrollView {
    private final List<UPOnScrollChangeObserver> mScrollObservers;
    private boolean mScrollable;

    public MarketHScrollView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MarketHScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarketHScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mScrollObservers = new ArrayList();
        this.mScrollable = true;
        this.setOverScrollMode(2);
        this.setHorizontalScrollBarEnabled(false);
    }

    public void addScrollChangeObserver(UPOnScrollChangeObserver observer) {
        if (!this.mScrollObservers.contains(observer)) {
            this.mScrollObservers.add(observer);
        }
    }

    public void removeScrollChangeObserver(UPOnScrollChangeObserver observer) {
        mScrollObservers.remove(observer);
    }

    public void setScrollable(boolean scrollable) {
        this.mScrollable = scrollable;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return !this.mScrollable ? false : super.onTouchEvent(ev);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Iterator var5 = this.mScrollObservers.iterator();

        while (var5.hasNext()) {
            UPOnScrollChangeObserver observer = (UPOnScrollChangeObserver) var5.next();
            observer.onScrollChanged(l, t, oldl, oldt);
        }

    }

    public interface UPOnScrollChangeObserver {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}