package com.android.wealth.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wealth.activity.DetailModifyActivity;
import com.android.wealth.activity.RecordActivity;
import com.android.wealth.activity.StatisticsActivity;
import com.android.wealth.adapter.BillListAdapter;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.Bill;
import com.android.wealth.bean.BillTitle;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.BillDBHelper;
import com.android.wealth.databinding.FragHomeBinding;
import com.android.wealth.databinding.IncludeEmptyLayoutBinding;
import com.android.wealth.utils.NumberUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class HomeFragment extends ViewBindingFragment<FragHomeBinding> implements View.OnClickListener {

    private BillListAdapter billListAdapter;


    private Calendar billCalendar;
    private String billDate;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewBinding().timeLayout.setOnClickListener(this);
        getViewBinding().addRecord.setOnClickListener(this);
        getViewBinding().billList.setOnClickListener(this);

        billListAdapter = new BillListAdapter();

        getViewBinding().list.setAdapter(billListAdapter);

        billListAdapter.setEmptyView(IncludeEmptyLayoutBinding.inflate(getLayoutInflater()).getRoot());

        billListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object obj = billListAdapter.getData().get(position);

                if (obj instanceof Bill){
                    Intent intent = new Intent(getActivity(), DetailModifyActivity.class);
                    intent.putExtra(DetailModifyActivity.BILL_ID, ((Bill) obj).billId);
                    startActivity(intent);
                }
            }
        });

        billCalendar = Calendar.getInstance();
        billCalendar.setTimeInMillis(System.currentTimeMillis());

        billDate = formatter.format(new Date(System.currentTimeMillis()));
        String[] split = billDate.split("/");
        getViewBinding().year.setText(String.format("%s年", split[0]));
        getViewBinding().month.setText(String.format("%s", split[1]));


    }

    @Override
    public void onClick(View v) {
        if (v == getViewBinding().timeLayout) {
            showTime();
        }else  if (v == getViewBinding().addRecord) {
            startActivity(new Intent(getActivity(), RecordActivity.class));
        }else  if (v == getViewBinding().billList) {
            startActivity(new Intent(getActivity(), StatisticsActivity.class));
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        showView();
    }

    private void showView() {
        String userId = UserManager.getUserManager().getUserId();
        List<Object> list = new ArrayList<>();
        Map<String, List<Bill>> billMap = BillDBHelper.getInstance().queryBillList(userId, billDate);
        double totalIncome = 0;
        double totalExport = 0;

        for (String time : billMap.keySet()) {

            BillTitle billTitle = new BillTitle();
            list.add(billTitle);

            billTitle.billDate = billDate;
            billTitle.createTime = time;

            List<Bill> billList = billMap.get(time);

            for (int i = 0; i < billList.size(); i++) {
                Bill bill = billList.get(i);

                if (bill.status == 0) {
                    totalExport += bill.money;
                    billTitle.export += bill.money;
                } else {
                    totalIncome += bill.money;
                    billTitle.income += bill.money;
                }
                list.add(bill);
            }
        }
        getViewBinding().export.setText(NumberUtil.formatNum(totalExport));
        getViewBinding().income.setText(NumberUtil.formatNum(totalIncome));

        billListAdapter.setNewData(list);
    }


    private void showTime() {

        new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String dateStr = formatter.format(date);
                String[] split = dateStr.split("/");
                int year = NumberUtil.parseInt(split[0]);
                int month = NumberUtil.parseInt(split[1]);
                billCalendar.set(year, month - 1, 1);

                billDate = dateStr;
                getViewBinding().year.setText(String.format("%s年", year));
                getViewBinding().month.setText(String.format("%s", split[1]));

                showView();

            }
        }).setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setDate(billCalendar)
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setItemVisibleCount(8) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(3.0f)
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build().show();
    }




}
