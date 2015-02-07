package com.zappos.weatherapp;

import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zappos.weatherapp.utils.DailyData;
import com.zappos.weatherapp.utils.WeatherData;

public class DailyListAdapter extends BaseAdapter {

	Context mContext;
	DailyData data[];

	public DailyListAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		data = WeatherData.getInstance().getDailyData();
		if (data == null)
			return 0;
		return data.length;
	}

	@Override
	public Row getItem(int index) {
		Row r = new Row();
		r.date = data[index].getDate();
		r.icon = data[index].getIcon();
		r.low_temp = data[index].getMinimumTemp();
		r.high_temp = data[index].getMaximumTemp();
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
			v = vi.inflate(R.layout.daily_row, viewGroup, false);

		}

		Row p = getItem(index);

		if (p != null) {

			TextView date = (TextView) v.findViewById(R.id.date);
			ImageView icon = (ImageView) v.findViewById(R.id.day_icon);
			TextView low_temp = (TextView) v.findViewById(R.id.low_temp);
			TextView high_temp = (TextView) v.findViewById(R.id.high_temp);

			if (date != null) {
				date.setText(p.getDate());
			}
			if (low_temp != null) {
				low_temp.setText(p.low_temp + "");
			}
			if (high_temp != null) {
				high_temp.setText(p.high_temp + "");
			}
			if (icon != null) {
				icon.setImageResource(p.getIcon());
			}
		}

		return v;
	}

	class Row {
		int date;
		String icon;
		int low_temp;
		int high_temp;

		public String getDate() {
			String str = "";
			Calendar cal = Calendar.getInstance();
			str = String.format("%02d/%02d", cal.get(Calendar.MONTH)+1, date);
			return str;
		}

		int getIcon() {
			int image_id;
			if (icon == null)
				image_id = R.drawable.hourly_day;

			if (icon.equals("clear-night"))
				image_id = R.drawable.hourly_night;
			else if (icon.equals("rain"))
				image_id = R.drawable.hourly_rain;
			else if (icon.equals("cloudy"))
				image_id = R.drawable.hourly_cloudy_day;
			else if (icon.equals("partly-cloudy-day"))
				image_id = R.drawable.hourly_cloudy_day;
			else if (icon.equals("partly-cloudy-night"))
				image_id = R.drawable.hourly_night;
			else
				image_id = R.drawable.hourly_day;

			return image_id;
		}
	}

}
