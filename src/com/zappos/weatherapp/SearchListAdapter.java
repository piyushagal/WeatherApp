package com.zappos.weatherapp;

import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchListAdapter extends BaseAdapter {

	Context mContext;
	Cursor c;
	String prevString = "";

	public SearchListAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		String[] projection = { CityListDBHelper.COLUMN_ID,
				CityListDBHelper.CITY_NAME, CityListDBHelper.STATE_CODE };
		String queryString = WeatherActivity.getQueryString();
		if (queryString != null) {
			queryString = queryString.toUpperCase(Locale.getDefault()).trim();
			if (queryString.length() > 0 && queryString != prevString) {
				String selection = CityListDBHelper.CITY_NAME + " LIKE ?";
				String[] selectionArgs = {queryString+"%"};
				if (c != null)
					c.close();
				c = mContext.getContentResolver().query(
						CityListContentProvider.CITY_URI, projection,
						selection, selectionArgs, null);
				prevString = queryString;
				if (c != null)
					return c.getCount();
				else
					return 0;
			}
		}
		return 0;
		
	}

	@Override
	public Row getItem(int index) {
		Row r = new Row();
		c.moveToPosition(index);
		r.id = c.getLong(0);
		r.city_name = c.getString(1);
		r.country_name = c.getString(2);
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
			v = vi.inflate(R.layout.search_row, viewGroup, false);

		}

		Row p = getItem(index);

		if (p != null) {

			TextView city = (TextView) v.findViewById(R.id.cityName);
			TextView country = (TextView) v.findViewById(R.id.countryName);

			if (city != null) {
				String str = modify(p.city_name);
				city.setText(str);
			}
			if (country != null) {

				country.setText(p.country_name);
			}
		}

		return v;
	}

	String modify(String str) {
		String[] modStr = str.split(" ");
		String out = "";
		for (int i = 0; i < modStr.length; i++) {
			modStr[i] = modStr[i].substring(0, 1).toUpperCase(Locale.getDefault())
					+ modStr[i].substring(1).toLowerCase(Locale.getDefault());
			out += modStr[i] + " ";
		}

		return out;
	}

	class Row {
		long id;
		String city_name;
		String country_name;
	}

}
