package com.android.wealth.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.bean.Bill;
import com.android.wealth.bean.TradeCategory;
import com.android.wealth.data.TradeCategoryUtils;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.BillDBHelper;
import com.android.wealth.databinding.ActivityDetailModifyActivityBinding;
import com.android.wealth.utils.NumberUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailModifyActivity extends ViewBindingActivity<ActivityDetailModifyActivityBinding> {

    public static final String BILL_ID = "BILL_ID";

    private Bill bill;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");

    private String billId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        billId = getIntent().getStringExtra(BILL_ID);


        getViewBinding().back.setOnClickListener(v -> finish());

        getViewBinding().delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bill == null){
                    return;
                }
                new AlertDialog.Builder(v.getContext()).setTitle("提示")
                        .setMessage("确认删除该条记录吗？")
                        .setPositiveButton("删除", (dialog, which) -> {
                            showToast("删除成功");
                            BillDBHelper.getInstance().deleteBill(bill.billId);
                            finish();
                        }).setNegativeButton("取消",null).show();
            }
        });

        getViewBinding().edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bill == null){
                    return;
                }
                Intent intent = new Intent(DetailModifyActivity.this, RecordActivity.class);
                intent.putExtra(RecordActivity.BILL, bill);
                startActivity(intent);
            }
        });

        getViewBinding().back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        bill = BillDBHelper.getInstance().queryBill(UserManager.getUserManager().getUserId(), billId);
        if (bill == null){
            return;
        }
        TradeCategory tradeCategory = TradeCategoryUtils.getTradeCategory(bill.category);
        getViewBinding().icon0.setImageResource(tradeCategory.selectedIcon);
        getViewBinding().icon0.setBackgroundColor(Color.WHITE);
        getViewBinding().icon1.setImageResource(tradeCategory.unSelectedIcon);

        getViewBinding().category.setText(tradeCategory.name);

        getViewBinding().status.setText(bill.status == 0 ? "支出" : "收入");
        getViewBinding().money.setText(NumberUtil.formatNum(bill.money));

        Date date = null;
        try {
            date = formatter2.parse(bill.billDate);
            getViewBinding().time.setText( formatter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getViewBinding().remake.setText(bill.remake);
    }
}