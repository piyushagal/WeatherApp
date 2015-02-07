package com.zappos.weatherapp.utils;

public class DailyData {
	private int minimumTemp;
	private int maximumTemp;
	private String summary;
	private String icon;
	private int dayOfWeek;
	private int date;
	public int getMinimumTemp() {
		if(WeatherData.getInstance().isMetric())
			return ((minimumTemp-32)*5)/9;
		return minimumTemp;
	}
	public void setMinimumTemp(int minimumTemp) {
		this.minimumTemp = minimumTemp;
	}
	public int getMaximumTemp() {
		if(WeatherData.getInstance().isMetric())
			return ((maximumTemp-32)*5)/9;
		return maximumTemp;
	}
	public void setMaximumTemp(int maximumTemp) {
		this.maximumTemp = maximumTemp;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	
	
}
