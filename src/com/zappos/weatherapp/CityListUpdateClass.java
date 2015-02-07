package com.zappos.weatherapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

public class CityListUpdateClass {
	private Context mContext;

	public CityListUpdateClass(Context context) {
		mContext = context;
	}

	public void fillTable() {
		try {
			InputStream is = null;
			is = mContext.getAssets().open("city_list.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				if (parts.length == 4) {
					ContentValues values = new ContentValues();
					values.put(CityListDBHelper.CITY_NAME, parts[0]);
					values.put(CityListDBHelper.STATE_CODE, parts[1]);
					values.put(CityListDBHelper.LATITUDE,
							Double.parseDouble(parts[2]));
					values.put(CityListDBHelper.LONGITUDE,
							Double.parseDouble(parts[3]));
					mContext.getContentResolver().insert(
							CityListContentProvider.CITY_URI, values);
				}
			}
			is.close();
		} catch (Exception e) {
			Log.i("CityListUpdateClass", "Error in creating Json object");
			e.printStackTrace();
		}
	}
}
