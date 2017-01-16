package com.jhonweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import com.jhonweather.gson.Weather;
import com.jhonweather.util.Constant;
import com.jhonweather.util.HttpUtil;
import com.jhonweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {



    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager= ((AlarmManager) getSystemService(ALARM_SERVICE));
        int anHour = 1*60*60*1010;
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString !=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            String weatherId= weather.basic.weatherId;
            String weatherUrl = Constant.BASE_PATH_WEATHER + Constant.REQUEST_KEY_CITY + weatherId + Constant.BASE_KEY;

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseT= response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseT);
                if (weather !=null &&"ok".equals(weather.status)){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("weather",responseT);
                    editor.apply();
                }
            }
        });
        }
    }
    private void updateBingPic(){
        String requestBingPic = Constant.BASE_PATH_PIC;
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic =response.body().string();
                SharedPreferences.Editor e= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                e.putString("bing_pic",bingPic);
                e.apply();



            }
        });
    }
}
