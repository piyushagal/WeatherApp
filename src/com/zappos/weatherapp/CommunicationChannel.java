package com.zappos.weatherapp;

import android.database.Cursor;

public interface CommunicationChannel {
	void showCityTemperature(Cursor c);
	void setCurrentCity(double lon, double lat);
	void showSearchFragment();
	void showCurrentTemperatureFragment();	
}
