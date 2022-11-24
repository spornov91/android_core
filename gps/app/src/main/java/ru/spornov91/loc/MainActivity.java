package ru.spornov91.loc;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.location.*;
import java.util.*;
import android.content.pm.*;
import android.*;
import android.content.*;
import android.view.*;

public class MainActivity extends Activity 
{
	private LocationManager locationManager;
	TextView tvEnabledGPS;
	TextView tvStatusGPS;
	TextView tvLocationGPS;
	TextView tvEnabledNet;
	TextView tvStatusNet;
	TextView tvLocationNet;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		tvEnabledGPS  = findViewById(R.id.tvEnabledGPS);
		tvStatusGPS   = findViewById(R.id.tvStatusGPS);
		tvLocationGPS = findViewById(R.id.tvLocationGPS);
		tvEnabledNet  = findViewById(R.id.tvEnabledNet);
		tvStatusNet   = findViewById(R.id.tvStatusNet);
		tvLocationNet = findViewById(R.id.tvLocationNet);
		
		int TAG_CODE_PERMISSION_LOCATION = 1;
		if (checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE) ==
			PackageManager.PERMISSION_GRANTED 
			&& checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
			PackageManager.PERMISSION_GRANTED
		    && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
			PackageManager.PERMISSION_GRANTED){
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			if(
				!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
				){
				startActivity(new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			
			
			}
		} else {
			requestPermissions(new String[] {
			Manifest.permission.ACCESS_NETWORK_STATE,
		    Manifest.permission.ACCESS_FINE_LOCATION, 
			Manifest.permission.ACCESS_COARSE_LOCATION }, 
			TAG_CODE_PERMISSION_LOCATION);
		}
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
											   1000 * 10, 10, locationListener);
		locationManager.requestLocationUpdates(
			LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
			locationListener);
		checkEnabled();
	}
		
	
	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			showLocation(location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			if (provider.equals(LocationManager.GPS_PROVIDER)) {
				tvStatusGPS.setText("Status: " + String.valueOf(status));
			} else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
				tvStatusNet.setText("Status: " + String.valueOf(status));
			}
		}
	};
	
	private void showLocation(Location location) {
		if (location == null)
			return;
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			tvLocationGPS.setText(formatLocation(location));
		} else if (location.getProvider().equals(
					   LocationManager.NETWORK_PROVIDER)) {
			tvLocationNet.setText(formatLocation(location));
		}
	}

	private String formatLocation(Location location) {
		if (location == null)
			return "";
		return String.format(
			"Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
			location.getLatitude(), location.getLongitude(), new Date(
				location.getTime()));
	}

	private void checkEnabled() {
		tvEnabledGPS.setText("Enabled: " + locationManager
							 .isProviderEnabled(LocationManager.GPS_PROVIDER));
		tvEnabledNet.setText("Enabled: " + locationManager
							 .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
	}
}
