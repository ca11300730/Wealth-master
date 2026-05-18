package com.android.wealth.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wealth.activity.RecordActivity;
import com.android.wealth.adapter.RecordAdapter;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.Bill;
import com.android.wealth.bean.TradeCategory;
import com.android.wealth.data.TradeCategoryUtils;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.BillDBHelper;
import com.android.wealth.databinding.FragRecordBinding;
import com.android.wealth.utils.NumberUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordFragment extends ViewBindingFragment<FragRecordBinding> implements View.OnClickListener {

    private Bill bill;

    public static String POSITION = "POSITION";

    private RecordAdapter recordAdapter;

    private int status;

    private String billDate;
    private String currentDay;

    private Calendar billCalendar;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bill = (Bill) getArguments().getParcelable(RecordActivity.BILL);

        billCalendar = Calendar.getInstance();
        billCalendar.setTimeInMillis(System.currentTimeMillis());

        billDate = formatter.format(new Date(System.currentTimeMillis()));
        currentDay = formatter.format(new Date(System.currentTimeMillis()));

        status = getArguments().getInt(POSITION);
        List<TradeCategory> categoryList = TradeCategoryUtils.getCategoryList(getArguments().getInt(POSITION));
        recordAdapter = new RecordAdapter(categoryList);

        getViewBinding().list.setAdapter(recordAdapter);


        getViewBinding().key0.setOnClickListener(this);
        getViewBinding().key1.setOnClickListener(this);
        getViewBinding().key2.setOnClickListener(this);
        getViewBinding().key3.setOnClickListener(this);
        getViewBinding().key4.setOnClickListener(this);
        getViewBinding().key5.setOnClickListener(this);
        getViewBinding().key6.setOnClickListener(this);
        getViewBinding().key7.setOnClickListener(this);
        getViewBinding().key8.setOnClickListener(this);
        getViewBinding().key9.setOnClickListener(this);
        getViewBinding().keyPoint.setOnClickListener(this);
        getViewBinding().keyDelete.setOnClickListener(this);
        getViewBinding().finish.setOnClickListener(this);
        getViewBinding().keyboard.setOnClickListener(this);
        getViewBinding().keyTime.setOnClickListener(this);

        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                recordAdapter.setSelectPosition(position);
                getViewBinding().keyBoardLayout.setVisibility(View.VISIBLE);
            }
        });

        if (bill != null) {
            getViewBinding().keyBoardLayout.setVisibility(View.VISIBLE);
            getViewBinding().money.setText(NumberUtil.formatNum(bill.money));
            getViewBinding().remake.setText(bill.remake);

            billDate = bill.billDate;
            if (TextUtils.equals(billDate,currentDay)){
                getViewBinding().keyTime.setText("今天");
            }else {
                getViewBinding().keyTime.setText(billDate);
            }

            String[] split = billDate.split("/");
            int year = NumberUtil.parseInt(split[0]);
            int month = NumberUtil.parseInt(split[1]);
            int day = NumberUtil.parseInt(split[2]);
            billCalendar.set(year, month - 1, day);

            int category = bill.category;

            for (int i = 0; i < categoryList.size(); i++) {
                if (category == categoryList.get(i).tradeCategory){
                    recordAdapter.setSelectPosition(i);
                    getViewBinding().list.smoothScrollToPosition(i);
                    break;
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        Editable editable = getViewBinding().money.getText();
        if (v == getViewBinding().key0 || v == getViewBinding().key1 || v == getViewBinding().key2 || v == getViewBinding().key3 ||
                v == getViewBinding().key4 || v == getViewBinding().key5 || v == getViewBinding().key6 || v == getViewBinding().key7 ||
                v == getViewBinding().key8 || v == getViewBinding().key9) {
            String[] split = editable.toString().split("\\.");
            if (split.length >= 2 && split[1].length() == 2) {
                return;
            }
            if (v == getViewBinding().key0 && editable.length() == 0) {
                return;
            }
            String text = ((TextView) v).getText().toString();
            getViewBinding().money.append(text);
        } else if (v == getViewBinding().keyPoint) {
            if (editable.length() == 0 || editable.toString().contains(".")) {
                return;
            }
            String[] split = editable.toString().split("\\.");
            if (split.length < 2 || split[1].length() < 2) {
                getViewBinding().money.append(".");
            }
        } else if (v == getViewBinding().keyDelete) {
            if (editable.length() > 0) {
                editable.delete(editable.length() - 1, editable.length());
            }
        } else if (v == getViewBinding().keyboard) {
            getViewBinding().keyBoardLayout.setVisibility(View.GONE);
            editable.clear();
        } else if (v == getViewBinding().finish) {
            ok();
        } else if (v == getViewBinding().keyTime) {
            showTime();
        }
    }

    private void ok() {
        Editable editable = getViewBinding().money.getText();
        if (editable.toString().isEmpty()) {
            showToast("金额不能为空");
            return;
        }
        TradeCategory selectData = recordAdapter.getSelectData();

        String userId = UserManager.getUserManager().getUserId();
        String remake = getViewBinding().remake.getText().toString().trim();
        if (bill == null){
            BillDBHelper.getInstance().addBill(userId, status, parseDouble(editable.toString()), selectData.tradeCategory, remake, billDate);
            showToast("添加成功");
        }else {
            BillDBHelper.getInstance().updateBill(userId,bill.billId, status, parseDouble(editable.toString()), selectData.tradeCategory, remake,
                    billDate);
            showToast("编辑成功");
        }
        getActivity().finish();
    }

    private double parseDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (Exception e) {

        }
        return 0;
    }

    private void showTime() {

        new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String dateStr = formatter.format(date);
                String[] split = dateStr.split("/");
                int year = NumberUtil.parseInt(split[0]);
                int month = NumberUtil.parseInt(split[1]);
                int day = NumberUtil.parseInt(split[2]);
                billCalendar.set(year, month - 1, day);

                if (TextUtils.equals(dateStr,currentDay)){
                    getViewBinding().keyTime.setText("今天");
                }else {
                    getViewBinding().keyTime.setText(dateStr);
                }
                billDate = dateStr;

            }
        }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
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
