package com.android.wealth.activity;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.bean.Bill;
import com.android.wealth.databinding.ActivityRecordBinding;
import com.android.wealth.fragment.RecordFragment;


public class RecordActivity extends ViewBindingActivity<ActivityRecordBinding> {

    public static final String BILL = "BILL";


    private final String[] titles = new String[]{"支出", "收入"};

    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bill = (Bill) getIntent().getParcelableExtra(BILL);

        getViewBinding().cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getViewBinding().viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        getViewBinding().tabLayout.setupWithViewPager(getViewBinding().viewPager);

        if (bill != null) {
            getViewBinding().viewPager.setCurrentItem(bill.status);
        }

    }

    private class Adapter extends FragmentPagerAdapter {

        public Adapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            RecordFragment recordFragment = new RecordFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(RecordFragment.POSITION, position);
            if (bill != null) {
                bundle.putParcelable(RecordActivity.BILL, bill);
            }
            recordFragment.setArguments(bundle);
            return recordFragment;
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