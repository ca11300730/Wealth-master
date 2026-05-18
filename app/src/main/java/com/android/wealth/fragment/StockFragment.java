package com.android.wealth.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.android.wealth.R;
import com.android.wealth.adapter.StockAdapter;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.Stock;
import com.android.wealth.databinding.FragStockBinding;
import com.android.wealth.databinding.ItemStockTabBinding;
import com.android.wealth.http.HttpCallback;
import com.android.wealth.http.OkHttpUtils;
import com.android.wealth.utils.GsonPoll;
import com.android.wealth.view.SyncHorizontalScrollManager;
import com.android.wealth.view.VerticalItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 股票
 */
public class StockFragment extends ViewBindingFragment<FragStockBinding>  {

    private String [] titles = new String[]{"涨幅榜","跌幅榜","市值榜","成交量榜","成交额榜","换手率榜"};
    private String [] sort_type = new String[]{"1","0","1","1","1","1"};
    private String [] sort_key = new String[]{"14","14","24","29","16","15"};

    private int index = 0;

    private static final String TAG = "StockFragment";

    public static final String TYPE = "type";

    private SyncHorizontalScrollManager mScrollManager;

    private int rn = 20;
    private int pn = 0;
    private boolean onRefresh = false;

    private StockAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollManager = new SyncHorizontalScrollManager();

        mScrollManager.removeViews();
        mScrollManager.addView(getViewBinding().titleScrollView);

        mAdapter = new StockAdapter(getContext(), mScrollManager);
            getViewBinding().container.addItemDecoration(new VerticalItemDecoration.Builder(requireContext())
                    .setHeight(R.dimen.dp_0_7).setColorResource(R.color.gray_line)
                    .setDefIgnorePosition().build());
        getViewBinding().container.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(this::reqData, getViewBinding().container);

        getViewBinding().swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pn = 0;
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
                pn = 0;
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
//        https://finance.pae.baidu.com/selfselect/getmarketrank?sort_type=1&sort_key=14&from_mid=1&pn=0&rn=20&group=ranklist&type=ab&finClientType=pc

        if ( pn == 0 && !onRefresh){
            getViewBinding().loadingView.showLoading();
        }

        String selfselect = "https://finance.pae.baidu.com/selfselect/getmarketrank";
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sort_type", sort_type[index]);
        map.put("sort_key", sort_key[index]);
        map.put("from_mid", 1);
        map.put("pn", pn);
        map.put("rn", rn);
        map.put("group","ranklist");
        map.put("type","ab");
        map.put("finClientType", "pc");
        OkHttpUtils.get(getActivity(), selfselect, map, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                getViewBinding().loadingView.showContentView();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject obj = (JSONObject) jsonObject.optJSONObject("Result").optJSONArray("Result").get(0);
                    JSONArray result = obj.optJSONObject("DisplayData").optJSONObject("resultData")
                            .optJSONObject("tplData").optJSONObject("result").optJSONArray("rank");
                    List<Stock> stocks = GsonPoll.get().fromJson(result.toString(), new TypeToken<List<Stock>>() {}.getType());
                    if (pn == 0){
                        mAdapter.setNewData(stocks);
                        getViewBinding().titleScrollView.scrollTo(0,0);
                        getViewBinding().container.scrollToPosition(0);
                        getViewBinding().loadingView.showContentView();
                    }else {
                        mAdapter.addData(stocks);
                    }
                    if (stocks.size() < rn){
                        mAdapter.loadMoreEnd();
                    }else {
                        mAdapter.loadMoreComplete();
                    }
                    getViewBinding().swipeRefreshLayout.setRefreshing(false);
                    onRefresh = false;
                    pn += 20;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e, String errorMessage) {
                getViewBinding().swipeRefreshLayout.setRefreshing(false);
                onRefresh = false;
                if (pn == 0){
                    getViewBinding().loadingView.showDataError();
                }else{
                    mAdapter.loadMoreFail();
                }
            }
        });

    }


}
