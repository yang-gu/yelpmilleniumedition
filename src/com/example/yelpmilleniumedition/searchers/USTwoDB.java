package com.example.yelpmilleniumedition.searchers;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yelpmilleniumedition.Business;

public class USTwoDB extends SQLiteOpenHelper {
	public static final String TABLE_STORED = "stored";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LOCATION = "location";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_CROSS_STREETS = "cross_streets";
	public static final String COLUMN_NEIGHBORHOOD = "neighborhood";
	public static final String COLUMN_STATE = "state";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitde";

	private static final String DATABASE_NAME = "storage.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_STORED + "(" + COLUMN_ID + " text primary key not null, "
			+ COLUMN_NAME + " text not null, " + COLUMN_CITY + " text not null, "
			+ COLUMN_CROSS_STREETS + " text not null, " + COLUMN_NEIGHBORHOOD
			+ " text not null, " + COLUMN_STATE + " text not null, "
			+ COLUMN_ADDRESS + " text not null, "
			+ COLUMN_LATITUDE + " real not null, "
			+ COLUMN_LONGITUDE + " real not null);";

	public USTwoDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORED);
		onCreate(db);
	}

	public void initBusinesses(ArrayList<Business> aListB) {
		SQLiteDatabase database = getWritableDatabase();
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_STORED);
		database.execSQL(DATABASE_CREATE);

		// inputting the businesses
		for (Business b : aListB) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_ID, b.getId());
			values.put(COLUMN_NAME, b.getName());
			values.put(COLUMN_CITY, b.getCity());
			values.put(COLUMN_CROSS_STREETS, b.getCrossStreets());
			values.put(COLUMN_NEIGHBORHOOD, b.getNeighborhood());
			values.put(COLUMN_STATE, b.getState());
			values.put(COLUMN_ADDRESS, b.getAddress());
			values.put(COLUMN_LATITUDE, b.getLatitude());
			values.put(COLUMN_LONGITUDE, b.getLongitude());
			database.insert(TABLE_STORED, null, values);
		}
		database.close();

	}

	public ArrayList<Business> getAllBusinesses() {
		ArrayList<Business> lB = new ArrayList<Business>();
		String query = "SELECT  * FROM " + TABLE_STORED;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Business b = null;
		if (cursor.moveToFirst()) {
			do {
				b = new Business();
				b.setId(cursor.getString(0));
				b.setName(cursor.getString(1));
				b.setCity(cursor.getString(2));
				b.setCrossStreets(cursor.getString(3));
				b.setNeighborhood(cursor.getString(4));
				b.setState(cursor.getString(5));
				b.setAddress(cursor.getString(6));
				b.setLatitude(cursor.getDouble(7));
				b.setLongitude(cursor.getDouble(8));

				lB.add(b);
			} while (cursor.moveToNext());
		}
		
		return lB;

	}
}
