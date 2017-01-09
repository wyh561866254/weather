package com.jhonweather.util;

import android.text.TextUtils;

import com.jhonweather.db.City;
import com.jhonweather.db.County;
import com.jhonweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/1/9.
 */

public class Utility  {

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    Province province = new Province();
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }

    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    City city = new City();
                    JSONObject cityObject = allCities.getJSONObject(i);
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountiesResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    County county = new County();
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }





}
