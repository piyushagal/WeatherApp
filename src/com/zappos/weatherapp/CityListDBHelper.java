package com.zappos.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class CityListDBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "city";
	private static final int version = 29;

	public static final String COLUMN_ID = "_id";
	public static final String TABLE_NAME = "city_list";
	public static final String CITY_NAME = "city";
	public static final String STATE_CODE = "state";
	public static final String LONGITUDE = "lon";
	public static final String LATITUDE = "lat";

	CityListUpdateClass city;
	private Context mContext;

	private static final String DATABASE_CREATE = " CREATE TABLE " + TABLE_NAME
			+ "(" + COLUMN_ID + " INTEGER AUTO_INCREMENT," + CITY_NAME
			+ " TEXT NOT NULL," + STATE_CODE + " TEXT NOT NULL," + LONGITUDE
			+ " REAL," + LATITUDE + " REAL);";

	public CityListDBHelper(Context context) {
		super(context, DB_NAME, null, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		city = new CityListUpdateClass(mContext);
		SharedPreferences prefs = mContext.getSharedPreferences("weatherApp",
				Context.MODE_PRIVATE);

		new FillDBTask().execute();
		Editor ed = prefs.edit();
		ed.putString("city", "Las vegas, NV");
		ed.putFloat("lat", 36.17372f);
		ed.putFloat("lon", -115.10647f);
		ed.commit();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	class FillDBTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			mContext.getContentResolver().insert(CityListContentProvider.CITY_URI, null);
			return null;
		}

	}

}
