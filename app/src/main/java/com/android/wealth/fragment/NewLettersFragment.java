package com.android.wealth.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.wealth.adapter.NewsletterAdapter;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.Newsletter;
import com.android.wealth.databinding.FragNewsletterBinding;
import com.android.wealth.http.HttpCallback;
import com.android.wealth.http.OkHttpUtils;
import com.android.wealth.utils.GsonPoll;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class NewLettersFragment extends ViewBindingFragment<FragNewsletterBinding> {

    private NewsletterAdapter newsletterAdapter;

    private int rn = 20;
    private int pn = 10;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsletterAdapter = new NewsletterAdapter();

        getViewBinding().recyclerView.setAdapter(newsletterAdapter);

        newsletterAdapter.setOnLoadMoreListener(this::getNews, getViewBinding().recyclerView);

        getViewBinding().swipeRefreshLayout.setOnRefreshListener(() -> {
            pn = 10;
            getNews();
        });
    }

    @Override
    public void onUserFirstVisible() {
        super.onUserFirstVisible();

        getNews();
    }

    private void getNews() {
        String expressnews = "https://finance.pae.baidu.com/selfselect/expressnews";
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("rn", rn);
        map.put("pn", pn);
        map.put("finClientType", "pc");
        OkHttpUtils.get(getActivity(), expressnews, map, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    // 验证响应字符串不为空
                    if (response == null || response.trim().isEmpty()) {
                        throw new Exception("响应数据为空");
                    }
                    
                    // 先解析为 JsonObject，手动处理 Result 字段（可能是对象或数组）
                    JsonObject jsonObject = GsonPoll.get().fromJson(response, JsonObject.class);
                    if (jsonObject != null && jsonObject.has("Result")) {
                        JsonElement resultElement = jsonObject.get("Result");
                        Newsletter.ResultDTO resultDTO = null;
                        
                        // 判断 Result 是对象还是数组
                        if (resultElement != null && resultElement.isJsonObject()) {
                            // Result 是对象，直接解析
                            resultDTO = GsonPoll.get().fromJson(resultElement, Newsletter.ResultDTO.class);
                        } else if (resultElement != null && resultElement.isJsonArray()) {
                            // Result 是数组，取第一个元素
                            JsonArray resultArray = resultElement.getAsJsonArray();
                            if (resultArray != null && resultArray.size() > 0) {
                                resultDTO = GsonPoll.get().fromJson(resultArray.get(0), Newsletter.ResultDTO.class);
                            }
                        }
                        
                        // 处理解析后的数据
                        if (resultDTO != null && resultDTO.content != null && resultDTO.content.list != null) {
                            if (pn == 10){
                                newsletterAdapter.setNewData(resultDTO.content.list);
                                getViewBinding().contentLayout.showContentView();
                            }else {
                                newsletterAdapter.addData(resultDTO.content.list);
                            }
                            if (resultDTO.content.list.size() < rn){
                                newsletterAdapter.loadMoreEnd();
                            }else {
                                newsletterAdapter.loadMoreComplete();
                            }
                        }
                    }
                    getViewBinding().swipeRefreshLayout.setRefreshing(false);
                    pn += 20;
                } catch (Exception e) {
                    e.printStackTrace();
                    getViewBinding().swipeRefreshLayout.setRefreshing(false);
                    if (pn == 10){
                        getViewBinding().contentLayout.showDataError();
                    }else{
                        newsletterAdapter.loadMoreFail();
                    }
                }
            }

            @Override
            public void onFailure(Exception e, String errorMessage) {
                getViewBinding().swipeRefreshLayout.setRefreshing(false);
                if (pn == 10){
                    getViewBinding().contentLayout.showDataError();
                }else{
                    newsletterAdapter.loadMoreFail();
                }
            }
        });
    }
}
