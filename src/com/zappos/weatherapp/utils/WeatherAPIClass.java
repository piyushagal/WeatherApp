package com.zappos.weatherapp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class WeatherAPIClass {
	static final String URL = "https://api.forecast.io/forecast/82e94ecad2f622344a5d0c62de4148d9/";
	CurrentTempUpdateListener updateListener;

	private Context mContext;

	WeatherData weatherData;
	
	static WeatherAPIClass instance;

	public static WeatherAPIClass getInstance(){
		if(instance == null)
			instance = new WeatherAPIClass();
		return instance;
	}
	
	public void setListener(CurrentTempUpdateListener listener, Context context){
		updateListener = listener;
		mContext = context;
	}
	
	private WeatherAPIClass(CurrentTempUpdateListener listener, Context context) {
		mContext = context;
		updateListener = listener;
	}

	private WeatherAPIClass() {
			//No parameters for service
	}

	public void updateCurrent(double[] location) {
		if(location == null){
			SharedPreferences sharedPrefs = mContext.getSharedPreferences("weatherApp",Context.MODE_PRIVATE);
			location = new double[2];
			location[1] = sharedPrefs.getFloat("lon", 0.0f);
			location[0] = sharedPrefs.getFloat("lat", 0.0f);
		}
		new LoadWeather().execute(location);
	}

	class LoadWeather extends AsyncTask<double[], Void, String> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(mContext, "",
					"Loading. Please wait...", true);
			super.onPreExecute();
		}

		protected String doInBackground(double[]... params) {
			double[] loc = params[0];
			Log.i("key", loc[0] + "," + loc[1]);
			String url = URL + loc[0] + "," + loc[1];
			Log.i("WeatherAddress", url);
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			String result = "";
			try {
				HttpResponse response = client.execute(httpget);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
				// JSONTokener tokener = new JSONTokener(builder.toString());
				JSONObject finalResult = new JSONObject(builder.toString());
				weatherData = WeatherData.getInstance();
				weatherData.parse(finalResult);			

			} catch (UnknownHostException e) {
				return "Error";
			} catch (Exception e) {
				e.printStackTrace();
				return new String("Error");
			}
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equalsIgnoreCase("Error")) {
				// No internet connection detected.
				Toast.makeText(mContext, "No Internet Connection !!",
						Toast.LENGTH_LONG).show();
			}

			if (dialog != null
					&& dialog.isShowing())
				dialog.dismiss();
			if(updateListener!=null)
				updateListener.updateScreen();
		}
	}

}
