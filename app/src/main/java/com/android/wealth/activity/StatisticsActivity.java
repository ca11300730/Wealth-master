package com.android.wealth.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.databinding.ActivityStatisticsBinding;
import com.android.wealth.fragment.StatisticsFragment;
import com.android.wealth.utils.NumberUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends ViewBindingActivity<ActivityStatisticsBinding> {

    private final String[] titles = new String[]{"月账单", "年账单"};

    private String currentYear;

    private Calendar calendar;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        currentYear = formatter.format(new Date(System.currentTimeMillis()));
        getViewBinding().year.setText(currentYear);

        getViewBinding().cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getViewBinding().viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        getViewBinding().tabLayout.setupWithViewPager(getViewBinding().viewPager);

        getViewBinding().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getViewBinding().timeLayout.setVisibility(tab.getPosition() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getViewBinding().timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });

    }

    private void showTime() {

        new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String dateStr = formatter.format(date);
                calendar.set(Integer.parseInt(dateStr), 1, 1);
                currentYear = dateStr;

                getViewBinding().year.setText(currentYear);

                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (Fragment fragment : fragments) {
                    StatisticsFragment statisticsFragment = (StatisticsFragment) fragment;
                    statisticsFragment.setYear(currentYear);
                }

            }
        }).setType(new boolean[]{true, false, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择年份")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setDate(calendar)
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setItemVisibleCount(8) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(3.0f)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build().show();
    }

    private class Adapter extends FragmentPagerAdapter {

        public Adapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            StatisticsFragment statisticsFragment = new StatisticsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(StatisticsFragment.POSITION, position);
            bundle.putString(StatisticsFragment.YEAR, currentYear);

            statisticsFragment.setArguments(bundle);
            return statisticsFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}