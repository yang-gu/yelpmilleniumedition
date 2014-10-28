package com.example.yelpmilleniumedition;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yelpmilleniumedition.searchers.Searcher;
import com.example.yelpmilleniumedition.searchers.USTwoDB;

public class SearchScreen extends ActionBarActivity {

	ListView lv;
	ArrayList<Business> businesses;
	EditText et;
	Button b;
	ActionBarActivity aba;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        lv = (ListView)findViewById(R.id.list);
        et = (EditText)findViewById(R.id.searchField);
        b = (Button)findViewById(R.id.search_button);
        aba = this;
        
        
    }
    
    //If there's no network connectivity then we disable the search field and populate from the DB
    //If there's network connectivity then we repopulate the DB with a background search
    @Override
    protected void onResume(){
    	if(!isNetworkAvailable()){
    		et.setEnabled(false);
    		new GetResults().execute("food", null, null);
    	}
    	else{
    		new PopulateDB().execute(null, null, null);
    	}
    	
    }
    
	private  boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
    
    public void populate(View v){
    	String terms = et.getText().toString();
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class GetResults extends AsyncTask<String, Void, ArrayList<Business>>{
    	Searcher s;
    	
    	@Override
    	public ArrayList<Business> doInBackground(String... search){
    		s = Searcher.getSearcher(aba);
    		return s.getBusinessesInArea(search[0]);

    	}
    	
    	@Override
    	public void onPostExecute(ArrayList<Business> alb){
    		final ArrayList<Business> alb2 = alb;
    		BusinessesAdapter ba = new BusinessesAdapter(aba, 0, alb);
    		lv.setAdapter(ba);
    		lv.setOnItemClickListener(new OnItemClickListener() {
      		  @Override
      		  public void onItemClick(AdapterView<?> parent, View view,
      		    int position, long id) {
      		    Business b = alb2.get(position);
      		   
      		    
      		    Intent i = new Intent("MapActivity.class");
      		    i.putExtra("latitude", b.getLatitude());
      		    i.putExtra("longitude", b.getLongitude());
      		    i.putExtra("name", b.getName());
      		    i.putExtra("address", b.getAddress());
      		    startActivity(i);
      		    
      		  }
      		}); 
    		
    	}
    }
    
    private class PopulateDB extends AsyncTask<Void, Void, ArrayList<Business>>{
    	Searcher s;
    	
    	@Override
    	public ArrayList<Business> doInBackground(Void... v){
    		s = Searcher.getSearcher(aba);
    		return s.getBusinessesInArea("food");

    	}
    	
    	@Override
    	public void onPostExecute(ArrayList<Business> alb){
    		USTwoDB u2db = new USTwoDB(aba);
    		u2db.initBusinesses(alb);    	
    		
    	}
    }
}
