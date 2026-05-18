package com.android.wealth.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.wealth.R;
import com.android.wealth.adapter.FundAdapter;
import com.android.wealth.adapter.StockAdapter;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.Fund;
import com.android.wealth.bean.Stock;
import com.android.wealth.databinding.FragFundBinding;
import com.android.wealth.databinding.FragStockBinding;
import com.android.wealth.databinding.ItemStockTabBinding;
import com.android.wealth.http.HttpCallback;
import com.android.wealth.http.OkHttpUtils;
import com.android.wealth.utils.GsonPoll;
import com.android.wealth.view.SyncHorizontalScrollManager;
import com.android.wealth.view.VerticalItemDecoration;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 基金
 */
public class FundFragment extends ViewBindingFragment<FragFundBinding>  {

    private String [] titles = new String[]{"股票型","混合型","债券型","指数型","QDII","FOF"};
    private String [] ft = new String[]{"gp","hh","zq","zs","qdii","fof"};

    private int index = 0;

    private static final String TAG = "StockFragment";

    public static final String TYPE = "type";

    private SyncHorizontalScrollManager mScrollManager;

    private int pn = 50;
    private int pi = 1;
    private boolean onRefresh = false;

    private FundAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollManager = new SyncHorizontalScrollManager();

        mScrollManager.removeViews();
        mScrollManager.addView(getViewBinding().titleScrollView);

        mAdapter = new FundAdapter(getContext(), mScrollManager);
            getViewBinding().container.addItemDecoration(new VerticalItemDecoration.Builder(requireContext())
                    .setHeight(R.dimen.dp_0_7).setColorResource(R.color.gray_line)
                    .setDefIgnorePosition().build());
        getViewBinding().container.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(this::reqData, getViewBinding().container);

        getViewBinding().swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pi = 1;
                onRefresh = true;
                reqData();
            }
        });

        for (String title : titles) {
            TabLayout.Tab tab = getViewBinding().tabLayout.newTab();
            ItemStockTabBinding binding = ItemStockTabBinding.inflate(getLayoutInflater());
            binding.text.setText(title);
            tab.setCustomView(binding.getRoot());
            getViewBinding().tabLayout.addTab(tab);
        }
        getViewBinding().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pi = 1;
                index = tab.getPosition();
                reqData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getViewBinding().tabLayout.selectTab(getViewBinding().tabLayout.getTabAt(0));
    }

    @Override
    public void onUserFirstVisible() {
        super.onUserFirstVisible();
        reqData();
    }

    private void reqData() {

        if ( pi == 1 && !onRefresh){
            getViewBinding().loadingView.showLoading();
        }
        String fund = "https://fund.eastmoney.com/data/fundtradenewapi.aspx";
        if (index == 3){
            fund = "https://api.fund.eastmoney.com/FundTradeRank/GetRankList";
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sc", "1n");
        map.put("pn", pn);
        map.put("pi", pi);
        map.put("ft",ft[index]);
        map.put("st","desc");
        map.put("callback","jQuery" + System.currentTimeMillis());
        map.put("fl", "0");
        map.put("isab", "1");
        OkHttpUtils.get(getActivity(), fund, map, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                getViewBinding().loadingView.showContentView();
                try {
                    if (index == 3){
                        response = response.replaceFirst("jQuery\\d+_\\d+\\(","").replace("\\)","");
                        response = new JSONObject(response).optString("Data");
                    }else {
                        response = response.replaceFirst("var rankData = ","")
                                .replace(";","");
                    }
                    List<String> funds = GsonPoll.get().fromJson(response, Fund.class).datas;
                    if (pi == 1){
                        mAdapter.setNewData(funds);
                        getViewBinding().titleScrollView.scrollTo(0,0);
                        getViewBinding().container.scrollToPosition(0);
                        getViewBinding().loadingView.showContentView();
                    }else {
                        mAdapter.addData(funds);
                    }
                    if (funds.size() < pn){
                        mAdapter.loadMoreEnd();
                    }else {
                        mAdapter.loadMoreComplete();
                    }
                    getViewBinding().swipeRefreshLayout.setRefreshing(false);
                    onRefresh = false;
                    pi += 1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e, String errorMessage) {
                getViewBinding().swipeRefreshLayout.setRefreshing(false);
                onRefresh = false;
                if (pi == 1){
                    getViewBinding().loadingView.showDataError();
                }else{
                    mAdapter.loadMoreFail();
                }
            }
        });

    }


}
