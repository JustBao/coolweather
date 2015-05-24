package util;

import model.City;
import model.County;
import model.Province;
import android.text.TextUtils;
import android.util.Log;
import db.CoolWeatherDB;

public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		Log.d("res","4444"+response);

		if(!TextUtils.isEmpty(response)){
			Log.d("res","333"+response);
			String[] allProvinces=response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p:allProvinces){
					String [] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
					
				}
				return true;
			}
			
		}
		return false;
		
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
	 */
	
	public static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			Log.d("res","eee");
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String [] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);	
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					Log.d("res","eee"+city.getCityName());
					coolWeatherDB.saveCity(city);
					
				}
				return true;
			}
			
		}
		return false;
		
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
	 */
	
	public  static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for(String c:allCounties){
					String [] array=c.split("\\|");
					County county=new County();		
					county.setCountyCode(array[0]);	
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
					
				}
				return true;
			}
			
		}
		return false;
		
	}
	
	
}