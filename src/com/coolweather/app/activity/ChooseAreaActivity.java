package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE=0;
    
    public static final int LEVEL_CITY=1;
    
    public static final int LEVEL_COUNTRY=2;
    
    private ProgressDialog progressDialog;
    
    private TextView textView;
    
    private ListView listView;
    
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList= new ArrayList<String> ();
    
  
    
    //province list
    private List<Province>provinceList;
    
    //city list
    private List<City>cityList;
    
    //country list
    private List<Country>countryList;
    
    //selected province
    private Province selectedProvince;
    
    //selected city
    private City selectedCity;
    
    //selected country
    private Country selectedCountry;
    
    private int currentLevel;
    
    protected void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	
    	SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
    	if(prefs.getBoolean("city_selected", false)){
    		Intent intent=new Intent(this,WeatherActivity.class);
    		startActivity(intent);
    		finish();
    		return;
    	}
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.choose_area);
    	listView=(ListView) findViewById(R.id.list_view);
    	textView=(TextView)findViewById(R.id.text_vew);
    	adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
    	listView.setAdapter(adapter);
    	coolWeatherDB=CoolWeatherDB.getInstance(this);
    	listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(currentLevel==LEVEL_PROVINCE)
				{
					selectedProvince=provinceList.get(position);
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity=cityList.get(position);
					queryCountries();
				}else if(currentLevel==LEVEL_COUNTRY){
					String countryCode=countryList.get(position).getCountryCode();
					Intent intent=new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("country_code",countryCode);
					startActivity(intent);
					finish();
				}
			}
    	});
    	queryProvinces();
    }
    
    //query province information
    private void queryProvinces(){
    	provinceList=coolWeatherDB.loadProvinces();
    	if(provinceList.size()>0){
    		dataList.clear();
    		for( Province province:provinceList){
    			dataList.add(province.getProvinceName());
    			
    		}
    		
    		adapter.notifyDataSetChanged();
    		listView.setSelection(0);
    		textView.setText("中国");
    		currentLevel=LEVEL_PROVINCE;
    		
    	}else{
    		queryFromSever(null,"province");
    	}
    }
    
    //query city information
    private void queryCities(){
    	cityList=coolWeatherDB.loadCities(selectedProvince.getId());
    	if(cityList.size()>0){
    		dataList.clear();
    		for( City city:cityList){
    			dataList.add(city.geteCityName());
    			
    		}
    		adapter.notifyDataSetChanged();
    		listView.setSelection(0);
    		textView.setText(selectedProvince.getProvinceName());
    		currentLevel=LEVEL_CITY;
    		
    	}else{
    		queryFromSever(selectedProvince.getProvinceCode(),"city");
    	}
    }
    
    //query province information
    private void queryCountries(){
    	countryList=coolWeatherDB.loadCountries(selectedCity.getId());
    	if(countryList.size()>0){
    		dataList.clear();
    		for(Country country:countryList){
    			dataList.add(country.geteCountryName());
    			
    		}
    		 
    		adapter.notifyDataSetChanged();
    		listView.setSelection(0);
    		textView.setText(selectedCity.geteCityName());
    		currentLevel=LEVEL_COUNTRY;
    		
    	}else{
    		queryFromSever(selectedCity.getCityCode(),"country");
    	}
    }
    
    
    //根据传入的代号和类型在服务器上查询.
   public  void queryFromSever(final String code,final String type){
    	String address;
    	if(!TextUtils.isEmpty(code)){
    		address="http://www.weather.com.cn/data/list3/city"+code+".xml";
    	}
    	else {
    		address="http://www.weather.com.cn/data/list3/city.xml";
    		//Log.d("coolWeather", address);
    	}
    	showProgressDialog();
    	HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvincesResponse(coolWeatherDB, response);
				}
				if("city".equals(type)){
					result=Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
				}
				if("country".equals(type)){
					result=Utility.handleCountriesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				if(result){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}
							if("city".equals(type)){
								queryCities();
							}
							if("country".equals(type)){
								queryCountries();
							}
						}
						
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT);
					}
					
				});
			}
    		
    	});
    }
    private void showProgressDialog(){
    	if(progressDialog==null){
    		progressDialog=new ProgressDialog(this);
    		progressDialog.setMessage("正在加载...");
    		progressDialog.setCanceledOnTouchOutside(false);
    	}
    	progressDialog.show();
    }
    
    private void closeProgressDialog(){
    	if(progressDialog!=null){
    		progressDialog.dismiss();
    	}
    }
    
    public void onBackPressed(){
    	if(currentLevel==LEVEL_COUNTRY){
    		queryCities();
    	}else if(currentLevel==LEVEL_CITY){
    		queryProvinces();
    	}else{
    		finish();
    	}
    }
}
