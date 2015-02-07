package com.zappos.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zappos.weatherapp.utils.CurrentTempUpdateListener;
import com.zappos.weatherapp.utils.WeatherAPIClass;
import com.zappos.weatherapp.utils.WeatherData;

public class CurrentWeatherFragment extends Fragment {
	TextView weather;
	TextView city;
	TextView clarity;
	TextView minTemp;
	TextView maxTemp;
	TextView humidity;
	TextView pressure;
	TextView wind;

	TextView measureC;
	TextView measureF;

	TextView now, hourly, daily;

	ImageView refreshButton;

	private String tempCity;

	WeatherData weatherData;

	CurrentTempUpdateListener listener;
	
	public CurrentWeatherFragment(){
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		View view = inflater.inflate(R.layout.current_weather_fragment,
				container, false);
		init(view);
		updateScreen();
		setHasOptionsMenu(true);
		return view;

	}

	public static CurrentWeatherFragment newInstance(String address) {
		CurrentWeatherFragment myFragment = new CurrentWeatherFragment();
		return myFragment;
	}

	void init(View view) {
		weather = (TextView) view.findViewById(R.id.temp);
		city = (TextView) view.findViewById(R.id.city);
		clarity = (TextView) view.findViewById(R.id.clear);
		minTemp = (TextView) view.findViewById(R.id.mintemp);
		maxTemp = (TextView) view.findViewById(R.id.maxtemp);
		humidity = (TextView) view.findViewById(R.id.humidity);
		pressure = (TextView) view.findViewById(R.id.pressure);
		wind = (TextView) view.findViewById(R.id.wind);
		refreshButton = (ImageView) view.findViewById(R.id.refresh);

		measureC = (TextView) view.findViewById(R.id.measure);
		measureF = (TextView) view.findViewById(R.id.measureF);
		measureC.setOnClickListener(mUnitChangeListener);
		measureF.setOnClickListener(mUnitChangeListener);

		now = (TextView) view.findViewById(R.id.nowText);
		hourly = (TextView) view.findViewById(R.id.hourlyText);
		daily = (TextView) view.findViewById(R.id.dailyText);

		now.setOnClickListener(mSetFragmentListener);
		hourly.setOnClickListener(mSetFragmentListener);
		daily.setOnClickListener(mSetFragmentListener);

		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WeatherAPIClass.getInstance().updateCurrent(null);
			}
		});

	}

	View.OnClickListener mUnitChangeListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			boolean isMetric = tv.getText().toString().equalsIgnoreCase("C") ? true
					: false;
			WeatherData.getInstance().setMetric(isMetric);
			listener = (WeatherActivity) getActivity();
			listener.updateScreen();

			SharedPreferences preferences = getActivity().getSharedPreferences(
					"weatherApp", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();

			editor.putBoolean("isMetric", isMetric);
			editor.commit();

		}
	};

	void setMeasure(boolean isMetric) {
		if (isMetric) {
			measureC.setTextColor(getResources().getColor(R.color.white_milk));
			measureF.setTextColor(getResources().getColor(
					R.color.white_disabled));
		} else {
			measureC.setTextColor(getResources().getColor(
					R.color.white_disabled));
			measureF.setTextColor(getResources().getColor(R.color.white_milk));
		}
	}

	View.OnClickListener mSetFragmentListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			listener = (WeatherActivity) getActivity();
			int id = v.getId();
			if (id == R.id.dailyText) {
				listener.setCurrentFragment(2);
			} else if (id == R.id.hourlyText) {
				listener.setCurrentFragment(1);
			}
		}
	};

	public void updateScreen() {
		weatherData = WeatherData.getInstance();
		if (tempCity == null)
			tempCity = "Unknown";
		city.setText(tempCity);
		weather.setText(String.format("%02d", weatherData.getTemperature()));
		minTemp.setText("" + weatherData.getMinTemp());
		maxTemp.setText("" + weatherData.getMaxTemp());
		humidity.setText("" + weatherData.getHumidity() + "%");
		pressure.setText("" + weatherData.getPressure()
				+ (weatherData.isMetric() ? "mb" : "in"));
		wind.setText("" + weatherData.getWindSpeed()
				+ (weatherData.isMetric() ? "kmph" : "mph"));
		clarity.setText(weatherData.getSummary());
		if(isAdded())
			setMeasure(weatherData.isMetric());
	}

	public void setCityTemp(String city) {
		tempCity = city;
	}

	double[] getCurrentCity() {
		double[] location = new double[2];
		getActivity();
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"weatherApp", Context.MODE_PRIVATE);
		location[0] = preferences.getFloat("lon", 0.0f);
		location[1] = preferences.getFloat("lon", 0.0f);
		return location;
	}

}
