package com.coolweather.app.db;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

public class CoolWeatherDB {
   //DataBase Name
	public static final String DB_NAME="cool_weather";
	
	//Database Edition
	public static int VERSION=1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	//create
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db=dbHelper.getWritableDatabase();
	}
	
	//get coolWeatherDB Instance
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	//save province instance to database
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
		
	//get province informtion from database
		
		public  List<Province> loadProvinces(){
			List<Province> list=new ArrayList<Province>();
			Cursor cursor=db.query("Province", null, null, null, null, null, null);
			if(cursor.moveToFirst()){
				do{
					Province province=new Province();
					province.setId(cursor.getInt(cursor.getColumnIndex("id")));
					province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
					province.setprovinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
					list.add(province);
				}while(cursor.moveToNext());
			}
			return list;
		}
	
	//save city instance to database
     public void saveCity(City city){
    	 if(city!=null){
    		 ContentValues values=new ContentValues();
    		 values.put("city_name", city.geteCityName());
    		 values.put("city_code", city.getCityCode());
    		 values.put("province_id", city.getProvinceId());
    		 db.insert("City", null, values);
    	 }
     }
     
     // get city information from database
     
     public List<City> loadCities(int provinceId){
    	 List<City> list=new ArrayList<City>();
	     Cursor cursor=db.query("City", null,"province_id = ?",new String[]{String.valueOf(provinceId)}, null, null, null);
	     //Cursor cursor=db.quer
    	// Cursor cursor=db.query("City", null,null,null, null, null, null);
    	// Cursor cursor=db.rawQuery("select * from City where province_id=?", new String{provinceId});
			if(cursor.moveToFirst()){
				do{
					City city=new City();
					city.setId(cursor.getInt(cursor.getColumnIndex("id")));
					city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
					city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
					city.setProvinceId(provinceId);
					list.add(city);
				}while(cursor.moveToNext());
			}
			return list;
     }
     
     
     //save country instance to database
     public void saveCountry(Country country){
    	 if(country!=null){
    		 ContentValues values=new ContentValues();
    		 values.put("country_name", country.geteCountryName());
    		 values.put("country_code", country.getCountryCode());
    		 values.put("city_id", country.getCityId());
    		 db.insert("Country", null, values);
    	 }
     }
     
     
     //get country information from database
     public List<Country> loadCountries(int cityId){
    	 List<Country> list=new ArrayList<Country>();
			Cursor cursor=db.query("Country", null,"city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
			if(cursor.moveToFirst()){
				do{
					Country country=new Country();
					country.setId(cursor.getInt(cursor.getColumnIndex("id")));
					country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
					country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
					country.setCityId(cityId);
					list.add(country);
				}while(cursor.moveToNext());
			}
			return list;
       }
}

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
   
