package com.android.wealth.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Newsletter  {

    @SerializedName("QueryID")
    public String queryID;
    @SerializedName("ResultCode")
    public String resultCode;
    @SerializedName("Result")
    public ResultDTO result;

    public static class ResultDTO {

        public String title;
        public String type;
        public ContentDTO content;

        public static class ContentDTO {
            public List<Tag> tag;
            public List<ListDTO> list;

            public static class Tag {
                public String text;
            }

            public static class ListDTO {

                public String title;
                public Content content;
                public long publish_time;
                public String third_url;
                public String important;
                public String tag;
                public String provider;
                public String evaluate;

                public static class Content {
                    public List<ItemsDTO> items;

                    public static class ItemsDTO {
                        public String type;
                        public String data;
                    }
                }
            }
        }
    }
}
