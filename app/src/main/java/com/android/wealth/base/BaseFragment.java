package com.android.wealth.base;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    @Override
    public void onResume() {
        super.onResume();
        if (!hasFirstVisible) {
            onUserFirstVisible();
            hasFirstVisible = true;
        }
        onUserVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        onUserInvisible();
    }


    private boolean hasFirstVisible;

    public void onUserFirstVisible() {
    }

    // fragment 可见回调
    public void onUserVisible() {
    }

    // fragment 不可见回调
    public void onUserInvisible() {
    }

}
