package com.jhonweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2017/1/11.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More  more;
    public class  More{
        @SerializedName("txt")
        public String info;
    }

}
