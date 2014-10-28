package com.example.yelpmilleniumedition.searchers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.yelpmilleniumedition.Business;
import com.example.yelpmilleniumedition.yelpinterface.YelpMaster;

public abstract class Searcher {

	protected double latitude, longitude;

	public abstract ArrayList<Business> getBusinessesInArea(String search);

	protected static Context ctxt;

	
	//Returns iSearcher if internet's up and oSearcher if it's down
	public static Searcher getSearcher(Context context) {
		ctxt = context;
		if (isNetworkAvailable())
			return new iSearcher(context);
		else
			return new oSearcher(context);
	}

	//This returns "true" if network connectivity is there and "false" if it's not
	private static boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	//This is the Searcher that's returned by the factory method if the network's up
	private static class iSearcher extends Searcher {
		
		//Yelp API strings
		private final String consumerKey = "H47EpXNGEfXGvYvHUhGkeA";
		private final String consumerSecret = "eaexxk9xRgmNLAbsgHhvQtjtEBo";
		private final String token = "hxXBIMnL-RgBacQkVvq-M1VBtp1R3Pb2";
		private final String tokenSecret = "bsgyfwNrL-hcG5dTxzU2kKoxgK4";
		//gpst manages GPS
		private GPSTracker gpst;
		//ym manages Yelp api calls
		private YelpMaster ym;

		//Initializes the coordinates
		public iSearcher(Context context) {
			gpst = new GPSTracker(context);
			latitude = gpst.getLatitude();
			longitude = gpst.getLongitude();
			ym = new YelpMaster(consumerKey, consumerSecret, token, tokenSecret);
		}

		//Finds businesses fitting the search string around the already-initialized lat and long 
		//values
		public ArrayList<Business> getBusinessesInArea(String term) {
			String result = ym.search(term, latitude, longitude);
			ArrayList<Business> businessArray = new ArrayList<Business>();

			try {
				JSONObject jo = new JSONObject(result);
				JSONArray businesses = jo.getJSONArray("businesses");
				for (int i = 0; i < businesses.length(); i++) {
					JSONObject jbus = businesses.getJSONObject(i);
					Business b = new Business();
					b.setId(jbus.getString("id"));
					b.setName(jbus.getString("name"));
					JSONObject location = jbus.getJSONObject("location");
					b.setCity(location.getString("city"));
					b.setAddress(location.getJSONArray("address").getString(0));
					b.setCrossStreets(location.getString("cross_streets"));
					b.setImage(location.getString("image_url"));

					JSONArray neighbs = jbus.getJSONArray("neighborhoods");
					b.setNeighborhood(neighbs.getString(0));
					b.setState(location.getString("state_code"));
					businessArray.add(b);

				}
			} catch (Exception e) {
				return new ArrayList<Business>();
			}
			return businessArray;

		}
	}

	//Just dumps the DB values.
	private static class oSearcher extends Searcher {
		USTwoDB u2db;

		public oSearcher(Context ctxt) {
			u2db = new USTwoDB(ctxt);
		}

		public ArrayList<Business> getBusinessesInArea(String search) {
			return u2db.getAllBusinesses();
		}
	}
}
