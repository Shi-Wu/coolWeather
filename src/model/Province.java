package model;

public class Province {
     private int id;
     private String provinceName;
     private String provinceCode;
     
     
     public int getId(){
    	 return id;
     }
     public void setId(int id){
    	 this.id=id;
     }
     public String getProvinceName(){
    	 return provinceName;
     }
     public void setProvinceName(String provinceName){
    	 this.provinceName=provinceName;
     }
     public void setprovinceCode(String provinceCode){
    	 this.provinceCode=provinceCode;
     }
     public String getProvinceCode(){
    	 return provinceCode;
     }
     
}