package activity;


import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

import com.example.coolweather.R;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity{
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDesp;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weatherInfoLayout=(LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText=(TextView) findViewById(R.id.city_name);
		publishText=(TextView) findViewById(R.id.publish_text);
		weatherDesp=(TextView) findViewById(R.id.weather_desp);
		temp1Text=(TextView) findViewById(R.id.temp1);
		temp2Text=(TextView) findViewById(R.id.temp2);
		currentDateText=(TextView) findViewById(R.id.current_date);
		
		String countyCode=getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//有县级代号时就去查询天气
			publishText.setText("同步中。。。");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}
	}
	/**
	 * 查询县级代号所对应的天气代号
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {		
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		Log.d("qqqs","dxd"+address+"vvvv"+countyCode);
		queryFromServer(address,"countyCode");
	}
	/**
	 * 查询天气代号所对应的天气
	 * @param address
	 * @param string
	 */
	private void queryWeatherInfo(String weatherCode){
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		Log.d("qqqs","ccc"+address+"bbbb"+weatherCode);
		queryFromServer(address,"weatherCode");
	}
	/**
	 * 根据传入的地址和类型去想服务器查询天气代号或者天气信息
	 * @param address
	 * @param string
	 */
	private void queryFromServer(final String address, final String type) {
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				Log.d("qqqs","fffff");
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						Log.d("qqqs","mmmm");
						String array[]=response.split("\\|");
						if(array!=null&&array.length==2){
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					 }				
				  }else if("weatherCode".equals(type)){
					Log.d("qqqs","oooooo");
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					
					//通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
						showWeather();
						}				
					});
				}

			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void showWeather() {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("qqqs","dnnnnn");
		cityNameText.setText(prefs.getString("city_name",""));
		temp1Text.setText(prefs.getString("temp1",""));
		temp2Text.setText(prefs.getString("temp2",""));
		weatherDesp.setText(prefs.getString("weather_desp",""));
		publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
		currentDateText.setText(prefs.getString("current_date",""));
		
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
}
