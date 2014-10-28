package com.example.yelpmilleniumedition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

	// Google Map
	private GoogleMap googleMap;
	private String name;
	private String address;
	private double latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);
		Intent i = getIntent();
		name = i.getStringExtra("name");
		address = i.getStringExtra("address");
		latitude = i.getDoubleExtra("latitude", 0.0);
		longitude = i.getDoubleExtra("longitude", 0.0);

		try {
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(latitude, longitude)).title(name);

			googleMap.addMarker(marker);

			googleMap.setMyLocationEnabled(true);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

}
