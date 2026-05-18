package com.android.wealth.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Bill implements Parcelable {

    /**
     * 0 支出  1 收入
     */
    public int status;

    public double money;

    /**
     * 交易类型
     */
    public int category;

    public String userId;

    public String billDate;
    public String billId;
    public long createTime;
    public String remake;

    public Bill() {

    }

    protected Bill(Parcel in) {
        status = in.readInt();
        money = in.readDouble();
        category = in.readInt();
        userId = in.readString();
        billDate = in.readString();
        billId = in.readString();
        createTime = in.readLong();
        remake = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeDouble(money);
        dest.writeInt(category);
        dest.writeString(userId);
        dest.writeString(billDate);
        dest.writeString(billId);
        dest.writeLong(createTime);
        dest.writeString(remake);
    }
}
