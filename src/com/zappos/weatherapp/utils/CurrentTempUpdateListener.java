package com.zappos.weatherapp.utils;


public interface CurrentTempUpdateListener {
	void updateScreen();
	void setCurrentFragment(int id);
	void startLocationUpdates();
}
