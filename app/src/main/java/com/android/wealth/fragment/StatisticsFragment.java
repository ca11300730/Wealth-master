package com.android.wealth.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wealth.activity.StatisticsDetailActivity;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.Bill;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.BillDBHelper;
import com.android.wealth.databinding.FragStatisticsBinding;
import com.android.wealth.databinding.ItemStatisticsBinding;
import com.android.wealth.utils.NumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class StatisticsFragment extends ViewBindingFragment<FragStatisticsBinding> {


    public static String POSITION = "POSITION";
    public static String YEAR = "YEAR";

    public String userId;

    private boolean isMonthStatistics;

    private String currentYear;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isMonthStatistics = getArguments().getInt(POSITION) == 0;
        currentYear = getArguments().getString(YEAR);

        getViewBinding().value0.setText(isMonthStatistics ? "年结余" : "总结余");
        getViewBinding().value1.setText(isMonthStatistics ? "年收入" : "总收入");
        getViewBinding().value2.setText(isMonthStatistics ? "年支出" : "总支出");
        getViewBinding().value3.setText(isMonthStatistics ? "月份" : "年份");
        getViewBinding().value4.setText(isMonthStatistics ? "月收入" : "年收入");
        getViewBinding().value5.setText(isMonthStatistics ? "月支出" : "年支出");
        getViewBinding().value6.setText(isMonthStatistics ? "月结余" : "年结余");
        getViewBinding().value7.setVisibility(isMonthStatistics ? View.INVISIBLE : View.GONE);


        userId = UserManager.getUserManager().getUserId();

    }

    @Override
    public void onUserFirstVisible() {
        super.onUserFirstVisible();
        if (isMonthStatistics) {
            queryByMonth();
        } else {
            groupByYear();
        }
    }

    private void queryByMonth() {
        Map<String, ArrayList<Bill>> billMap = BillDBHelper.getInstance().queryBillListByYear(userId, currentYear);
        double totalIncome = 0;
        double totalExport = 0;
        getViewBinding().container.removeAllViews();
        for (String time : billMap.keySet()) {
            ArrayList<Bill> billList = billMap.get(time);
            double monthIncome = 0;
            double monthExport = 0;
            for (int i = 0; i < billList.size(); i++) {
                Bill bill = billList.get(i);

                if (bill.status == 0) {
                    totalExport += bill.money;
                    monthExport += bill.money;
                } else {
                    totalIncome += bill.money;
                    monthIncome += bill.money;
                }
            }
            ItemStatisticsBinding binding = ItemStatisticsBinding.inflate(getLayoutInflater());
            binding.value0.setText(String.format("%s月", Integer.parseInt(time)));
            binding.value1.setText(NumberUtil.formatNum(monthIncome));
            binding.value2.setText(NumberUtil.formatNum(monthExport));
            binding.value3.setText(NumberUtil.formatNum(monthIncome - monthExport));
            binding.value4.setVisibility(isMonthStatistics ? View.VISIBLE : View.GONE);
            getViewBinding().container.addView(binding.getRoot());

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), StatisticsDetailActivity.class);
                    intent.putExtra(StatisticsDetailActivity.YEAY, currentYear);
                    intent.putExtra(StatisticsDetailActivity.MONTH, time);
                    intent.putExtra(StatisticsDetailActivity.LIST, billList);
                    startActivity(intent);
                }
            });
        }
        getViewBinding().cashIncome.setText(NumberUtil.formatNum(totalIncome));
        getViewBinding().cashExport.setText(NumberUtil.formatNum(totalExport));
        getViewBinding().cashSurplus.setText(NumberUtil.formatNum(totalIncome - totalExport));

        checkEmpty();
    }

    private void groupByYear() {

        List<Bill> billList = BillDBHelper.getInstance().queryBillList(userId);
        double totalIncome = 0;
        double totalExport = 0;

        Map<String, List<Bill>> billMap = new LinkedHashMap<>();
        for (int i = 0; i < billList.size(); i++) {
            Bill bill = billList.get(i);
            String year = bill.billDate.split("/")[0];
            List<Bill> yearBillList = billMap.get(year);
            if (yearBillList == null) {
                yearBillList = new ArrayList<>();
                yearBillList.add(bill);
                billMap.put(year, yearBillList);
            } else {
                yearBillList.add(bill);
            }
        }

        for (String time : billMap.keySet()) {
            List<Bill> yearBillList = billMap.get(time);
            double monthIncome = 0;
            double monthExport = 0;

            for (int i = 0; i < yearBillList.size(); i++) {
                Bill bill = yearBillList.get(i);

                if (bill.status == 0) {
                    totalExport += bill.money;
                    monthExport += bill.money;
                } else {
                    totalIncome += bill.money;
                    monthIncome += bill.money;
                }
            }
            ItemStatisticsBinding binding = ItemStatisticsBinding.inflate(getLayoutInflater());
            binding.value0.setText(String.format("%s年", Integer.parseInt(time)));
            binding.value1.setText(NumberUtil.formatNum(monthIncome));
            binding.value2.setText(NumberUtil.formatNum(monthExport));
            binding.value3.setText(NumberUtil.formatNum(monthIncome - monthExport));
            binding.value4.setVisibility(isMonthStatistics ? View.VISIBLE : View.GONE);
            getViewBinding().container.addView(binding.getRoot());
        }

        getViewBinding().cashIncome.setText(NumberUtil.formatNum(totalIncome));
        getViewBinding().cashExport.setText(NumberUtil.formatNum(totalExport));
        getViewBinding().cashSurplus.setText(NumberUtil.formatNum(totalIncome - totalExport));
        checkEmpty();
    }

    private void checkEmpty(){
        int count = getViewBinding().container.getChildCount();
        if (count == 0){
            getViewBinding().container.setVisibility(View.GONE);
            getViewBinding().emptyLayout.getRoot().setVisibility(View.VISIBLE);
        }else {
            getViewBinding().container.setVisibility(View.VISIBLE);
            getViewBinding().emptyLayout.getRoot().setVisibility(View.GONE);
        }
    }

    public void setYear(String currentYear) {
        this.currentYear = currentYear;
        if (isMonthStatistics) {
            queryByMonth();
        }
    }
}
