package com.zappos.weatherapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CityListContentProvider extends ContentProvider {
	public final static Uri CITY_URI = Uri
			.parse("content://com.zappos.weatherapp.CityListContentProvider");
	CityListDBHelper dbHelper;
	SQLiteDatabase db;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (values == null) {
			insertInBulk();
			return null;
		} else {
			long rowID = db.insert(CityListDBHelper.TABLE_NAME, null, values);
			if (rowID > 0) {
				Uri _uri = ContentUris.withAppendedId(CITY_URI, rowID);
				getContext().getContentResolver().notifyChange(_uri, null);
				return _uri;

			}
			return null;
		}
	}
	
	private void insertInBulk(){
		try {
			InputStream is = null;
			is = getContext().getAssets().open("city_list.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			db.beginTransaction();
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
					db.insert(CityListDBHelper.TABLE_NAME, null, values);
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			is.close();
		} catch (Exception e) {
			db.endTransaction();
			Log.i("CityListUpdateClass", "Error in creating Json object");
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreate() {
		dbHelper = new CityListDBHelper(getContext());
		db = dbHelper.getWritableDatabase();
		return db != null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (sortOrder == null || sortOrder.length() < 1) {
			sortOrder = CityListDBHelper.CITY_NAME;
		}
		Cursor c = db.query(CityListDBHelper.TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectioArgs) {
		int count = db.update(CityListDBHelper.TABLE_NAME, values, selection,
				selectioArgs);
		return count;
	}

}
