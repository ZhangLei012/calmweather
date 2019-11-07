package com.example.calmweather.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ScrollView;

import com.example.calmweather.db.County;
import com.example.calmweather.db.Province;
import com.example.calmweather.db.City;
import com.example.calmweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /*
    *解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /* 处理服务器返回的市级数据 */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCitys = new JSONArray(response);
                for(int i=0;i<allCitys.length();i++){
                    JSONObject cityObject = allCitys.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*解析和处理服务器返回的县级数据*/
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCountys=new JSONArray(response);
                for (int i = 0; i < allCountys.length(); i++) {
                    JSONObject countyObject = allCountys.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray;
            if(response.charAt(11)=='6')
                jsonArray=jsonObject.getJSONArray("HeWeather6");
            else{
                jsonArray=jsonObject.getJSONArray("HeWeather");
            }
            String weatherContent=jsonArray.getJSONObject(0).toString();
            Log.d("TAG","weatherContent = "+weatherContent);
            Weather weather=new Gson().fromJson(weatherContent,Weather.class);
            if(weather==null){
                Log.d("TAG","weather is null");
            }
            Log.d("TAG",weather.basic.cityName);
            return weather;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
