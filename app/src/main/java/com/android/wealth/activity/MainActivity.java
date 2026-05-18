package com.android.wealth.activity;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.wealth.R;
import com.android.wealth.adapter.MainFragmentPagerAdapter;
import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.databinding.ActivityMainBinding;
import com.android.wealth.fragment.HomeFragment;
import com.android.wealth.fragment.CommunityFragment;
import com.android.wealth.fragment.ProductFragment;
import com.android.wealth.fragment.UserFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class MainActivity extends ViewBindingActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ProductFragment());
        fragmentList.add(new CommunityFragment());
        fragmentList.add(new UserFragment());

        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        //初始话适配器
        getViewBinding().pagerContainer.setAdapter(mainFragmentPagerAdapter);
        //禁止滑动
        getViewBinding().pagerContainer.setNoScroll(true);

        getViewBinding().pagerContainer.setOffscreenPageLimit(mainFragmentPagerAdapter.getCount());

        //设置bottomNavigationView
        getViewBinding().bottomNavigationView.setItemIconTintList(null);

        // 底部tab点击
        getViewBinding().bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.tab_main_product){
                    getViewBinding().pagerContainer.setCurrentItem(1, false);
                }else if (item.getItemId() == R.id.tab_main_community){
                    getViewBinding().pagerContainer.setCurrentItem(2, false);
                }else if (item.getItemId() == R.id.tab_main_analyze){
                    getViewBinding().pagerContainer.setCurrentItem(0, false);
                }else if (item.getItemId() == R.id.tab_main_user){
                    getViewBinding().pagerContainer.setCurrentItem(3, false);
                }
                return true;
            }
        });

    }

    private long mBackPress = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mBackPress < 3000) {
            super.onBackPressed();
        } else {
            showToast("再按一次退出程序");
        }
        mBackPress = System.currentTimeMillis();
    }
}