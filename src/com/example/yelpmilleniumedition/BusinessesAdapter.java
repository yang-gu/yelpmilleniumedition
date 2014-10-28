package com.example.yelpmilleniumedition;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BusinessesAdapter extends ArrayAdapter {

	ArrayList<Business> ab;
	Context context;

	public BusinessesAdapter(Context context, int resource, ArrayList businesses) {
		super(context, resource, businesses);
		ab = businesses;
		this.context = context;
	}

	public Business getItem(int index){
		return ab.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		Business b = ab.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_row, parent, false);

		TextView nameView = (TextView) rowView.findViewById(R.id.name);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
		TextView addressView = (TextView) rowView.findViewById(R.id.address);

		nameView.setText(b.getName());
		Picasso.with(context).load(b.getImage()).into(imageView);
		addressView.setText(b.getAddress());

		return rowView;
	}

}