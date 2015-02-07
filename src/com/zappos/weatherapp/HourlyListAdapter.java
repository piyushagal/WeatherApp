package com.zappos.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zappos.weatherapp.utils.HourlyData;
import com.zappos.weatherapp.utils.WeatherData;

public class HourlyListAdapter extends BaseAdapter {

	Context mContext;
	HourlyData data[];
	public HourlyListAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		data= WeatherData.getInstance().getHourlyData();
		if(data == null)
			return 0;
		return data.length;
	}

	@Override
	public Row getItem(int index) {
		Row r = new Row();
		r.time = data[index].getHourOfDay();
		r.icon = data[index].getIcon();
		r.temp = data[index].getTemperature();
		return r;
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup viewGroup) {
		View v = view;

		if (v == null) {

			LayoutInflater vi;
			vi = LayoutInflater.from(mContext);
			v = vi.inflate(R.layout.hourly_row, viewGroup,false);

		}

		Row p = getItem(index);

		if (p != null) {

			TextView time = (TextView) v.findViewById(R.id.time);
			ImageView icon = (ImageView)v.findViewById(R.id.hourly_icon);
			TextView temp= (TextView)v.findViewById(R.id.hourly_temp);

			if (time != null) {
				time.setText(p.getTime());
			}
			if (temp != null) {
				temp.setText(p.temp + "");
			}
			if(icon!=null){
				icon.setImageResource(p.getIcon());
			}
		}

		return v;
	}
	
	class Row {
		int time;
		String icon;
		int temp;
		
		String getTime(){
			String str = "";
			String am_pm = "AM";
			if(time>=12)
				am_pm = "PM";
			time= time%12;
			if(time == 0)
				time = 12;
			
			str = String.format("%02d:00 ", time);
			return str + am_pm;
		}
		
		int getIcon(){
			int image_id;
			if(icon == null)
				image_id = R.drawable.hourly_day;
			
			if(icon.equals("clear-night"))
				image_id = R.drawable.hourly_night;
			else if(icon.equals("rain"))
				image_id = R.drawable.hourly_rain;
			else if(icon.equals("cloudy"))
				image_id = R.drawable.hourly_cloudy_day;
			else if(icon.equals("partly-cloudy-day"))
				image_id = R.drawable.hourly_cloudy_day;
			else if(icon.equals("partly-cloudy-night"))
				image_id = R.drawable.hourly_night;
			else
				image_id = R.drawable.hourly_day;
			
			return image_id;
		}
	}

}
