package com.zappos.weatherapp;

import com.zappos.weatherapp.utils.CurrentTempUpdateListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class HourlyWeatherFragment extends Fragment {
	ListView hourlyList;
	HourlyListAdapter adapter;
	
	CurrentTempUpdateListener listener;
	private TextView daily;
	private TextView hourly;
	private TextView now;

	public HourlyWeatherFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hourly_weather_fragment,
				container, false);
		hourlyList = (ListView) view.findViewById(R.id.hourlyList);
		adapter = new HourlyListAdapter(getActivity());
		hourlyList.setAdapter(adapter);
		now = (TextView)view.findViewById(R.id.nowText);
		hourly = (TextView)view.findViewById(R.id.hourlyText);
		daily = (TextView)view.findViewById(R.id.dailyText);
		
		now.setOnClickListener(mSetFragmentListener);
		hourly.setOnClickListener(mSetFragmentListener);
		daily.setOnClickListener(mSetFragmentListener);
		
		updateScreen();
		return view;
	}

	public void updateScreen() {
		adapter.notifyDataSetChanged();
	}

	View.OnClickListener mSetFragmentListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			listener = (WeatherActivity)getActivity();
			int id = v.getId();
			if (id == R.id.dailyText) {
				listener.setCurrentFragment(2);
			} else if (id == R.id.nowText) {
				listener.setCurrentFragment(0);
			}
		}
	};

}
