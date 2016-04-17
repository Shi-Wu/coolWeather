package model;

public class City {
	 private int id;
     private String cityName;
     private String cityCode;
     
     
     public int getId(){
    	 return id;
     }
     public void setId(int id){
    	 this.id=id;
     }
     public String geteCityName(){
    	 return cityName;
     }
     public void setCityName(String cityName){
    	 this.cityName=cityName;
     }
     public void setCityCode(String cityCode){
    	 this.cityCode=cityCode;
     }
     public String getCityCode(){
    	 return cityCode;
     }
}
