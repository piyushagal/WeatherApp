package com.zappos.weatherapp;

import com.zappos.weatherapp.utils.CurrentTempUpdateListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WeatherSlidePageAdapter extends FragmentStatePagerAdapter {

	String mCity;
	CurrentWeatherFragment mCurrentWeatherFragment;
	HourlyWeatherFragment mHourlyWeatherFragment;
	DailyWeatherFragment mDailyWeatherFragment;
	
	public WeatherSlidePageAdapter(FragmentManager fm, String city, CurrentTempUpdateListener l){
		super(fm);
		mCity = city;
		mCurrentWeatherFragment = new CurrentWeatherFragment();
		mHourlyWeatherFragment = new HourlyWeatherFragment();
		mDailyWeatherFragment = new DailyWeatherFragment();
	}

	@Override
	public Fragment getItem(int position) {
		switch(position){
		case 0 :
			return mCurrentWeatherFragment;
		case 1 :
			return mHourlyWeatherFragment;
		case 2 :
			return mDailyWeatherFragment;
		default :
			return mCurrentWeatherFragment;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	public void notifyDataSetChanged(String city){
		mCity = city;
		mCurrentWeatherFragment.setCityTemp(mCity);
		mCurrentWeatherFragment.updateScreen();
		mHourlyWeatherFragment.updateScreen();
		mDailyWeatherFragment.updateScreen();
	}


}
