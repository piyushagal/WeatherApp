package com.zappos.weatherapp.utils;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherData {

	private double[] location;
	private int windSpeed;
	private int pressure;
	private int temperature;
	private int apparentTemperature;
	private int maxTemp;
	private int minTemp;
	private int humidity;
	private int visibility;
	private String summary;
	private String icon;
	private DailyData[] dailyData;
	private HourlyData[] hourlyData;
	private String city;
	private static boolean isMetric = false;
	
	static WeatherData instance;
	
	public static WeatherData getInstance(){
		if(instance == null){
			instance = new WeatherData();
		}
		return instance;
	}

	public void parse(JSONObject weather) {
		try {
			JSONObject main = weather.getJSONObject("currently");
			summary = main.getString("summary");
			temperature = (int) Double.parseDouble(main
					.getString("temperature"));
			humidity = (int) (Double.parseDouble(main.getString("humidity")) * 100);
			pressure = (int) Double.parseDouble(main.getString("pressure"));
			windSpeed = (int) Double.parseDouble(main.getString("windSpeed"));
			icon = main.getString("icon");

			JSONObject daily = weather.getJSONObject("daily");
			JSONArray data = daily.getJSONArray("data");
			dailyData = new DailyData[data.length()];
			for (int i = 0; i < data.length(); i++) {
				dailyData[i] = new DailyData();
				dailyData[i].setMinimumTemp((int) data.getJSONObject(i).getDouble("temperatureMin"));
				dailyData[i].setMaximumTemp((int) data.getJSONObject(i).getDouble("temperatureMax"));
				dailyData[i].setSummary(data.getJSONObject(i).getString("summary"));
				dailyData[i].setIcon(data.getJSONObject(i).getString("icon"));
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(data.getJSONObject(i).getLong("time")*1000L);
				dailyData[i].setDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
				dailyData[i].setDate(c.get(Calendar.DATE));
			}
			maxTemp = (int) data.getJSONObject(0).getDouble("temperatureMin");
			minTemp = (int) data.getJSONObject(0).getDouble("temperatureMax");
			
			JSONObject hourly = weather.getJSONObject("hourly");
			data = hourly.getJSONArray("data");
			hourlyData = new HourlyData[data.length()];
			for (int i = 0; i < data.length(); i++) {
				hourlyData[i] = new HourlyData();
				hourlyData[i].setTemperature((int) data.getJSONObject(i).getDouble("temperature"));
				hourlyData[i].setSummary(data.getJSONObject(i).getString("summary"));
				hourlyData[i].setIcon(data.getJSONObject(i).getString("icon"));
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(data.getJSONObject(i).getLong("time")*1000L);
				hourlyData[i].setHourOfDay(c.get(Calendar.HOUR_OF_DAY));
			}
			
		} catch (JSONException e) {
			// Error in parsing
		}
	}

	public boolean isMetric() {
		return isMetric;
	}

	public void setMetric(boolean value) {
			isMetric = value;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public int getWindSpeed() {
		if(!isMetric)
			return (int)((double)windSpeed/1.60934);
		return windSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

	public int getPressure() {
		if(!isMetric)
			return (int)((double)pressure/33.8637);
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getTemperature() {
		if(isMetric)
			return ((temperature-32)*5)/9;
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getApparentTemperature() {
		if(isMetric)
			return ((apparentTemperature-32)*5)/9;
		return apparentTemperature;
	}

	public void setApparentTemperature(int apparentTemperature) {
		this.apparentTemperature = apparentTemperature;
	}

	public int getMaxTemp() {
		if(isMetric)
			return ((maxTemp-32)*5)/9;
		return maxTemp;
	}

	public void setMaxTemp(int maxTemp) {
		this.maxTemp = maxTemp;
	}

	public int getMinTemp() {
		if(isMetric)
			return ((minTemp-32)*5)/9;
		return minTemp;
	}

	public void setMinTemp(int minTemp) {
		this.minTemp = minTemp;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getVisibility() {
		if(!isMetric)
			return (int)(visibility/1.60934);
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
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

	public DailyData[] getDailyData() {
		return dailyData;
	}

	public void setDailyData(DailyData[] dailyData) {
		this.dailyData = dailyData;
	}

	public HourlyData[] getHourlyData() {
		return hourlyData;
	}

	public void setHourlyData(HourlyData[] hourlyData) {
		this.hourlyData = hourlyData;
	}

	public String getCity() {
		return city;
	}
	
	public void setCity(String city){
		this.city = city; 
	}
	
	
	

}
