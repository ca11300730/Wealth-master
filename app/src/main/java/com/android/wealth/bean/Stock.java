package com.android.wealth.bean;

import java.util.List;

public class Stock {

    public String code;
    public String name;
    public String market;
    public String financeType;
    public String is_warrants;
    public String expire_date;
    public String status;
    public List<ListDTO> list;
    public String exchange;
    public String sf_url;

    public static class ListDTO {
        public String text;
        public String value;
    }
}
