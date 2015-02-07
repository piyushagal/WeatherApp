package com.zappos.weatherapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.zappos.weatherapp.utils.CurrentTempUpdateListener;
import com.zappos.weatherapp.utils.Utils;
import com.zappos.weatherapp.utils.WeatherAPIClass;
import com.zappos.weatherapp.utils.WeatherData;

@SuppressWarnings("deprecation")
public class WeatherActivity extends FragmentActivity implements
		CurrentTempUpdateListener, ConnectionCallbacks,
		OnConnectionFailedListener {

	ViewPager pager;
	WeatherSlidePageAdapter mPagerAdapter;
	SearchView search;
	ListView searchList;
	SlidingDrawer searchDrawer;
	boolean isDrawerOpened = false;
	RelativeLayout weatherLayout;

	SearchListAdapter adapter;
	ListItemClickListener listener;

	static String queryString;

	String mCity = "";
	double[] location = new double[2];

	WeatherAPIClass weatherApi;

	ProgressDialog mLocationLoadingDialog;
	GoogleApiClient mGoogleApiClient;

	DoubleClickReceiver receiver;
	private DoubleClickReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		weatherLayout = (RelativeLayout) findViewById(R.id.weatherMainLayout);
		pager = (ViewPager) findViewById(R.id.pager);
		searchList = (ListView) findViewById(R.id.searchList);
		search = (SearchView) findViewById(R.id.searchText);
		searchDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		weatherApi = WeatherAPIClass.getInstance();
		weatherApi.setListener(this, this);

		mPagerAdapter = new WeatherSlidePageAdapter(
				getSupportFragmentManager(), mCity, this);
		pager.setAdapter(mPagerAdapter);

		// Location
		
		searchViewHandling();
		
		buildGoogleApiClient();
		if (isGPSEnabled()) {
			showLocationDialog();
			mGoogleApiClient.connect();
		} else
			showHomeLocationTemperature();

		mReceiver = new DoubleClickReceiver();
		registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.weather, menu);
		SharedPreferences sharedPrefs = getSharedPreferences("weatherApp",
				Context.MODE_PRIVATE);
		String city = sharedPrefs.getString("city", "City, Country");
		if (mCity.equalsIgnoreCase(city))
			menu.getItem(0).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		if (isDrawerOpened)
			searchDrawer.animateClose();
		else {

			if (pager.getCurrentItem() == 0) {
				try {
					unregisterReceiver(mReceiver);
				} catch (IllegalArgumentException e) {

				}
				super.onBackPressed();
			} else {
				pager.setCurrentItem(pager.getCurrentItem() - 1);
			}
		}

	}

	public void setCurrentFragment(int id) {
		pager.setCurrentItem(id);
	}

	@Override
	public void updateScreen() {
		Bitmap bmp = Utils.getImage(this, WeatherData.getInstance().getIcon());
		Drawable d = new BitmapDrawable(getResources(), bmp);
		weatherLayout.setBackground(d);
		invalidateOptionsMenu();
		mPagerAdapter.notifyDataSetChanged(mCity);
	}

	void readSharedPreferences() {
		SharedPreferences sharedPrefs = getSharedPreferences("weatherApp",
				Context.MODE_PRIVATE);
		location[1] = sharedPrefs.getFloat("lon", 0.0f);
		location[0] = sharedPrefs.getFloat("lat", 0.0f);
		mCity = sharedPrefs.getString("city", "City, Country");
		boolean isMetric = sharedPrefs.getBoolean("isMetric", false);
		WeatherData.getInstance().setMetric(isMetric);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	void searchViewHandling() {
		adapter = new SearchListAdapter(this);
		searchList.setAdapter(adapter);
		listener = new ListItemClickListener();
		search.setQuery("", true);
		search.setIconifiedByDefault(false);

		searchList.setOnItemClickListener(listener);

		int id = search.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) search.findViewById(id);
		textView.setTextColor(Color.WHITE);

		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				queryString = query;
				adapter.notifyDataSetChanged();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				queryString = newText;
				adapter.notifyDataSetChanged();
				return true;
			}
		});

		searchDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				isDrawerOpened = true;
			}
		});

		searchDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				isDrawerOpened = false;
				search.setQuery("", true);
				adapter.notifyDataSetChanged();

			}
		});

	}

	public class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String city = adapter.getItem(position).city_name;
			String state = adapter.getItem(position).country_name;
			ContentResolver resolver = getContentResolver();
			String[] projection = { CityListDBHelper.CITY_NAME,
					CityListDBHelper.STATE_CODE, CityListDBHelper.LONGITUDE,
					CityListDBHelper.LATITUDE };
			String selection = CityListDBHelper.CITY_NAME + " LIKE '" + city
					+ "' AND " + CityListDBHelper.STATE_CODE + " LIKE '"
					+ state + "'";
			Cursor c = resolver.query(CityListContentProvider.CITY_URI,
					projection, selection, null, null);
			showCityTemperature(c);
		}
	}

	public void showCityTemperature(Cursor c) {
		c.moveToFirst();
		location[0] = c.getDouble(3);
		location[1] = c.getDouble(2);
		mCity = c.getString(0) + ", " + c.getString(1);
		weatherApi.updateCurrent(location);
		searchDrawer.animateClose();
		queryString = "";
		search.setQuery(queryString, true);
		c.close();
	}

	public static String getQueryString() {
		return queryString;
	}

	private final LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			double[] loc = new double[2];
			loc[0] = location.getLatitude();
			loc[1] = location.getLongitude();
			weatherApi.updateCurrent(loc);
			mCity = getLocality(location);
			removeLocationDialog();
		}

	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_set_current:
			setCurrentCity(location, mCity);
			invalidateOptionsMenu();
			return true;
		case R.id.action_current_location:
			showLocationDialog();
			startLocationUpdates();
			return true;
		case R.id.action_home_location:
			readSharedPreferences();
			weatherApi.updateCurrent(location);
			return true;

		case R.id.action_about:
			Intent intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
		}
		return false;
	}

	public void setCurrentCity(double[] loc, String city) {
		SharedPreferences preferences = getSharedPreferences("weatherApp",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat("lon", (float) loc[1]);
		editor.putFloat("lat", (float) loc[0]);
		editor.putString("city", city);
		editor.commit();
	}

	void showLocationDialog() {
		if (mLocationLoadingDialog == null){
			mLocationLoadingDialog = new ProgressDialog(this);
			mLocationLoadingDialog.setMessage("Tracking GPS location...");
			mLocationLoadingDialog.show();
			mLocationLoadingDialog.setCanceledOnTouchOutside(true);
		}
		else
			mLocationLoadingDialog.show();
	}

	void removeLocationDialog() {
		if (mLocationLoadingDialog != null
				&& mLocationLoadingDialog.isShowing())
			mLocationLoadingDialog.dismiss();
	}

	private boolean isGPSEnabled() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		String providers[] = { LocationManager.GPS_PROVIDER,
				LocationManager.NETWORK_PROVIDER };
		for (String provider : providers) {
			if (locationManager.isProviderEnabled(provider))
				return true;
		}
		return false;
	}

	void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	public void startLocationUpdates() {
		if (isGPSEnabled()) {
			if (mGoogleApiClient.isConnecting())
				return;
			else if (!mGoogleApiClient.isConnected())
				mGoogleApiClient.connect();
			else {
				LocationRequest mLocationRequest = new LocationRequest();
				mLocationRequest.setNumUpdates(1);
				mLocationRequest
						.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
				LocationServices.FusedLocationApi.requestLocationUpdates(
						mGoogleApiClient, mLocationRequest, mLocationListener);
			}
		} else {
			Toast.makeText(this, "No GPS Connection Available.",
					Toast.LENGTH_SHORT).show();
			removeLocationDialog();
		}

	}

	String getLocality(Location location) {
		String city = "Unknown";
		Geocoder geoCoder = new Geocoder(getApplicationContext(),
				Locale.getDefault());
		try {
			List<Address> l = geoCoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
			int index = l.get(0).getMaxAddressLineIndex();
			city = l.get(0).getAddressLine(index - 1);
			String[] parts = city.split(" ");
			city = parts[0] + " " + parts[1];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			city = "Unknown";
			e.printStackTrace();
		} finally {
			removeLocationDialog();
		}
		return city;
	}

	void showHomeLocationTemperature() {
		readSharedPreferences();
		weatherApi.updateCurrent(location);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.i("GPS", "Connection Failed!!");
		showHomeLocationTemperature();
		removeLocationDialog();
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.i("GPS", "Connected");
		Location loc = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);

		if (loc == null) {
			Log.i("GPS", "Null Location");
			showHomeLocationTemperature();
		} else {
			location[0] = loc.getLatitude();
			location[1] = loc.getLongitude();
			weatherApi.updateCurrent(location);
			mCity = getLocality(loc);
		}

		removeLocationDialog();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Log.i("GPS", "Connection Failed!!");
		removeLocationDialog();
	}

	@Override
	protected void onResume() {
		WeatherAPIClass.getInstance().updateCurrent(location);
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
		removeLocationDialog();
		super.onPause();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
