package com.android.wealth.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.wealth.R;
import com.android.wealth.activity.RecordActivity;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.HotRank;
import com.android.wealth.bean.Stock;
import com.android.wealth.bean.StockHq;
import com.android.wealth.databinding.FragProductBinding;
import com.android.wealth.http.HttpCallback;
import com.android.wealth.http.OkHttpUtils;
import com.android.wealth.utils.GsonPoll;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends ViewBindingFragment<FragProductBinding> {

    private final String[] titles = new String[]{"基金", "行情","资讯"};

    private HqAdapter mHqAdapter;

    private final Handler mHandler = new Handler();

    private final Runnable runnable = this::getStockHq;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new FundFragment());
        fragmentList.add(new StockFragment());
        fragmentList.add(new NewLettersFragment());

        getViewBinding().viewPager.setAdapter(new Adapter(getChildFragmentManager(),fragmentList));
        getViewBinding().viewPager.setOffscreenPageLimit(titles.length);
        getViewBinding().tabLayout.setupWithViewPager(getViewBinding().viewPager);

        mHqAdapter = new HqAdapter();
        getViewBinding().hQList.setAdapter(mHqAdapter);

        mHandler.postDelayed(runnable,1000);
    }

    private class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList;

        public Adapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getHotStock();

    }

    private void getStockHq() {
        String hq = "https://finance.pae.baidu.com/api/getbanner?market=asia&marketType=&code=&finClientType=pc";
        OkHttpUtils.get(getActivity(), hq, null, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // 添加空值检查，防止 NullPointerException
                    JSONObject resultObj = jsonObject.optJSONObject("Result");
                    if (resultObj != null) {
                        JSONArray list = resultObj.optJSONArray("list");
                        if (list != null) {
                            List<StockHq> stockHqs = GsonPoll.get().fromJson(list.toString(), new TypeToken<List<StockHq>>() {}.getType());
                            if (stockHqs != null) {
                                mHqAdapter.setNewData(stockHqs);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.postDelayed(runnable,300);
            }

            @Override
            public void onFailure(Exception e, String errorMessage) {
                mHandler.postDelayed(runnable,300);
            }
        });
    }

    private static class HqAdapter extends BaseQuickAdapter<StockHq, BaseViewHolder>{

        public HqAdapter() {
            super(R.layout.item_stock_hq);
        }

        @Override
        protected void convert(BaseViewHolder helper, StockHq item) {
            int color = Color.parseColor("#333333");
            if (item.increase.startsWith("+")) {
                color = Color.parseColor("#fc0002");
            } else if (item.increase.startsWith("-")) {
                color = Color.parseColor("#1DAA34");
            }

            helper.setText(R.id.name,item.name)
                    .setText(R.id.lastPrice,item.lastPrice)
                    .setText(R.id.increase,item.increase)
                    .setTextColor(R.id.increase,color)
                    .setText(R.id.ratio,item.ratio)
                    .setTextColor(R.id.ratio,color)
            ;
        }
    }


    private void getHotStock() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
        String today = sdf.format(new java.util.Date());
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
        String hotrank = "https://finance.pae.baidu.com/vapi/v1/hotrank?tn=wisexmlnew&dsp=iphone&product=stock&day=" + today + "&hour=" + hour + "&pn=0&rn=10&market=all&type=hour&finClientType=pc";
        OkHttpUtils.get(getActivity(), hotrank, null, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    HotRank hotRank = GsonPoll.get().fromJson(response, HotRank.class);
                    // 添加空值检查，防止 NullPointerException
                    if (hotRank != null && hotRank.result != null && hotRank.result.body != null) {
                        List<List<String>> body = hotRank.result.body;
                        for (List<String> list : body) {
                            // 检查列表元素是否足够，避免 IndexOutOfBoundsException
                            if (list != null && list.size() >= 9) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(list.get(0)).append(" ");
                                sb.append(list.get(5)).append(list.get(3)).append(" ").append(list.get(2)).append(" 涨跌幅 ");
                                sb.append(list.get(1)).append(" 热度 ").append(list.get(8));
                                System.out.println(sb.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e, String errorMessage) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(runnable);
    }
}
