package com.android.wealth.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotRank {

    @SerializedName("ResultCode")
    public int resultCode;
    @SerializedName("ResultNum")
    public int resultNum;
    @SerializedName("QueryID")
    public String queryID;
    @SerializedName("Result")
    public Result result;

    public static class Result {
        public List<List<String>> body;
        public List<String> header;
    }
}
