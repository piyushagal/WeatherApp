package com.zappos.weatherapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.zappos.weatherapp.utils.WeatherAPIClass;
import com.zappos.weatherapp.utils.WeatherData;

public class DoubleClickReceiver extends BroadcastReceiver implements TextToSpeech.OnInitListener {
	boolean screenOn = true;
	long time = 0;
	final long thres = 1000;
	TextToSpeech tts;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("PiyushBroadCast",""+time);
		if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)||intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
			if(time == 0)
				time = System.currentTimeMillis();
			else if((System.currentTimeMillis()-time)<thres){
				speakUpTemperature(context);
				time = 0;
			}else{
				time = System.currentTimeMillis();
			}
		}else if(intent.getAction().equals("com.zappos.weatherapp.ACTION_UPDATE_WEATHER")){
			WeatherAPIClass.getInstance().updateCurrent(null);
		}
	}
	
	void speakUpTemperature(Context context){
		tts = new TextToSpeech(context, this);
		
	}

	@Override
	public void onInit(int status) {
		if(status == TextToSpeech.SUCCESS){
			int temp = WeatherData.getInstance().getTemperature();
			String text = "The temperature is " + temp;
			if(WeatherData.getInstance().isMetric())
				text+=" degree Celsius";
			else
				text+=" degree Fahrenheit";
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
		}
	};

}
