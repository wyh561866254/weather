package com.jhonweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2017/1/11.
 */

public class Suggestion {

   @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CwBean carWash;
    public Sport sport;

    public static class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public static class CwBean {
        @SerializedName("txt")
        public String info;
    }

    public static class Sport {
        @SerializedName("txt")
        public String info;
    }


}
