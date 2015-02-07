package com.zappos.weatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zappos.weatherapp.utils.CurrentTempUpdateListener;

public class DailyWeatherFragment extends Fragment{
		ListView dailyList;
		DailyListAdapter adapter;
		
		CurrentTempUpdateListener listener;
		private TextView now;
		private TextView hourly;
		private TextView daily;
		
		public DailyWeatherFragment() {
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				 ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.daily_weather_fragment, container, false);
			dailyList = (ListView)view.findViewById(R.id.dailyList);
			adapter = new DailyListAdapter(getActivity());
			dailyList.setAdapter(adapter);
			
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
			if(adapter!=null)
				adapter.notifyDataSetChanged();
		}
		
		View.OnClickListener mSetFragmentListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener = (WeatherActivity)getActivity();
				int id = v.getId();
				if(id == R.id.nowText){
					listener.setCurrentFragment(0);
				}else if(id == R.id.hourlyText){
					listener.setCurrentFragment(1);
				}
			}
		};
}
