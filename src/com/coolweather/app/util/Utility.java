package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

public class Utility {
      //解析处理器返回Province的数据
	public  synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String [] allProvinces=response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p:allProvinces){
					String [] array=p.split("\\|");
					Province province=new Province();
					province.setprovinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	//处理服务器返回的City数据
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String [] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String p:allCities){
					String [] array=p.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	//处理服务器返回的Country数据
	
	public static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String [] allCountries=response.split(",");
			if(allCountries!=null&&allCountries.length>0){
				for(String p:allCountries){
					String [] array=p.split("\\|");
					Country country=new Country();
					country.setCountryCode(array[0]);;
					country.setCountryName(array[1]);;
				    country.setCityId(cityId);
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
	}
	
	//analyse json data and save them to the local 
	 
	 public static void handleWeatherResponse(Context context,String response){
		 try{
			 JSONObject jsonObject=new JSONObject(response);
			 JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
			 String cityName=weatherInfo.getString("city");
			 String weatherCode=weatherInfo.getString("cityid");
			 String temp1=weatherInfo.getString("temp1");
			 String temp2=weatherInfo.getString("temp2");
			 String weatherDesp=weatherInfo.getString("weather");
			 String publishTime=weatherInfo.getString("ptime");
			 saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	 //sava weatherInfo to sharedPreference file
	 public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime){
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		 SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		 editor.putBoolean("city_selected",true);
		 editor.putString("city_name", cityName);
		 editor.putString("weather_code", weatherCode);
	 }

}

   