package com.zappos.weatherapp.utils;

public class HourlyData {
	private int temperature;
	private String summary;
	private String icon;
	private int hourOfDay;
	public int getTemperature() {
		if(WeatherData.getInstance().isMetric())
			return ((temperature-32)*5)/9;
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
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
	public int getHourOfDay() {
		return hourOfDay;
	}
	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}
	
}
