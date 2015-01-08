/*
 * Name: Brian Kisese
 * Email: brayokisese@gmail.com
 * Phone: 254728762287
 */

package com.kisese.project_news.news_pages;

import java.io.IOException;
import java.util.ArrayList;



import org.jsoup.Jsoup;

import com.kisese.project_news.R;
import com.kisese.project_news.custom_listview.MainActivity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FinanceActivity extends ActionBarActivity {

	private SharedPreferences financeChannels;
	GridView gv;
	Context context;	  
	ArrayList prgmName;	
	
	//An array of GridView drawables
	//An array of GridView drawables
		public static int [] prgmImages={R.drawable.images};


	//variables to use
	
	//arrays to use
	String[] forUse = new String[25];
	String[] tags = new String[25];
	String[] urls = new String[25];
	
	//EditTexts
	EditText channel;
	EditText url;
	
	//String values
	String defaultValue;
	String final_url;
	String intermediate_url;
	String final_channel;
	
	boolean link_exists = true;
	
	TextView check_url;
	
	
	ImageView i;
	
	private ProgressDialog pDialog;
	private boolean kuna_net;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		//get the sharedPreferences object that contains the users saved searches
		//for the particular perspective, in this case the world news perspective
	
		
		financeChannels = getSharedPreferences("finance", MODE_PRIVATE);
		
		setContentView(R.layout.world_news);
		
		//find the gridview perspective
		TextView title = (TextView)findViewById(R.id.check_channel);
		gv=(GridView) findViewById(R.id.gridView1); 
		
		//get a SharedPreference.Editor to store new tag query pair
		SharedPreferences.Editor preferencesEditor = financeChannels.edit();
			
		//format is putString(url, value)
		//Here I initialize the default values for the app in every news category
		preferencesEditor.putString("Fast Company", "http://feeds.feedburner.com/fastcompany/headlines");
		preferencesEditor.putString("Paul Krugman", "http://krugman.blogs.nytimes.com/feed/");
		preferencesEditor.putString("Mail Online", "http://www.dailymail.co.uk/money/index.rss");
			
		//store current search
		preferencesEditor.commit();//store the updated reference
			
		//String array of shared preferences listing only the 'key' values
		tags = financeChannels.getAll().keySet().toArray(new String[0]);
			
		//create a loop to read through keys
		for(int i = 0; i < tags.length; i++){
				String use = financeChannels.getString(tags[i], null);
				//should be an array of urls
				urls[i] = use;
			}
			
		//set an adapter for the GridView object
	    gv.setAdapter(new FinanceAdapter(this, tags, prgmImages, urls));
	    
	    title.setText("Here are your finance news channels");
	    gv.getDrawableState();
		
		
		//add the custom view to the actionBar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.vipi_logo);
		actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ced1")));
		actionBar.show();
		
		if(!isConnected(this)){
			kuna_net = false;
		}else{
			kuna_net = true;
		}
	}//End of onCreate()
	

		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		
		//Inflate the ActionBar with this menu layout
		inflater.inflate(R.menu.nav_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//on actionBar item clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.action_add_channel:
			Toast.makeText(this, "Opening a dialog", Toast.LENGTH_SHORT).show();
			//add a function t create the dialog
			startDialog();
			break;
			
		case R.id.action_btn_clear:
			Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
			SharedPreferences.Editor prefsDelete = financeChannels.edit();
			prefsDelete.clear();
			prefsDelete.commit();
			Intent intent2 = new Intent(FinanceActivity.this, FinanceActivity.class);
			FinanceActivity.this.startActivity(intent2);
			break;
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private static final int DIALOG_ALERT = 10;

	public void onClick(View view) {
	    showDialog(DIALOG_ALERT);
	}
	
	//Create the add new Channel dialog
	
	private AlertDialog myDialog;
	View alertView;
	public void startDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		alertView = inflater.inflate(R.layout.channel_dialog, null);
				
		builder.setView(alertView);
		
		builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            //Add new Channel
          	channel  = (EditText)alertView.findViewById(R.id.channel_name);
          	check_url = (TextView)alertView.findViewById(R.id.chech_url); 
 			url = (EditText)alertView.findViewById(R.id.url);
 			
 			final String ch = channel.getText().toString();
 			final String ur = url.getText().toString();
            
            	 // check if user entered any data in EditText and url is valid
                if (ur.length() > 0 && ch.length()>0) {
                    check_url.setText("");
                    String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
                    if (ur.matches(urlPattern)) {
                    
                     	intermediate_url = ur;
                     	final_channel = ch;
                     	
                     	//check if url is a direct rss url, no need to confirm it then
                     	if(intermediate_url.contains(".xml") || intermediate_url.contains(".rss")){
                     		SharedPreferences.Editor preferencesEditor = financeChannels.edit();
                     		preferencesEditor.putString(final_channel,intermediate_url);
        					preferencesEditor.commit();	
        					Intent intent = new Intent(FinanceActivity.this,FinanceActivity.class);
        					FinanceActivity.this.startActivity(intent);
                     	}else{
            			//========================================================================================
             			//######################################################################################
                     		//Use the AsyncTask class to check url in background
                     		if(kuna_net == false){
                        		alertYaNetwork();
                        	}else{	
                        		CheckForRss dotask = new CheckForRss();
                     	        dotask.execute();
                        	}
                     	}
                			
                    } else {
                        // URL not valid
                    	alertYangu();
                    }
                    }else {
                        // Please enter url
                    	alertYanguYaPili();
                    }
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FinanceActivity.this.closeOptionsMenu();
              //####################################################################################
                //close the dialog
            }
        }); 
		
		myDialog = builder.create();
		myDialog.show();
	}


	//check if link contains rss, depending on site structure this may work diffferently
	
	private class CheckForRss extends AsyncTask<String, Void, String>{

		@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(FinanceActivity.this);
	            pDialog.setMessage("Fetching your RSS url ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
	 
		
		@Override
		protected String doInBackground(String... params) {
				
			String response = "";
			final SharedPreferences.Editor preferencesEditor = financeChannels.edit();
			try {
				// Using JSoup library to parse the html source code
				org.jsoup.nodes.Document doc = Jsoup.connect(intermediate_url).get();
				// finding rss links which have link[type=application/rss+xml]
				org.jsoup.select.Elements links = doc
						.select("link[type=application/rss+xml]");
				
				Log.d("No of RSS links found", " " + links.size());
				
				// check if urls found or not
				if (links.size() > 0) {
					final_url = links.get(0).attr("href").toString();
					//alertYaSuccess();
					link_exists = true;
					
					preferencesEditor.putString(final_channel, final_url);
					preferencesEditor.commit();	
				} else {
					// finding rss links which have link[type=application/rss+xml]
					org.jsoup.select.Elements links1 = doc
							.select("link[type=application/atom+xml]");
					if(links1.size() > 0){
						final_url = links.get(0).attr("href").toString();
						//alertYaSuccess();
						link_exists = true;
						
						preferencesEditor.putString(final_channel, final_url);
						preferencesEditor.commit();	
					}else{
						link_exists = false;
					}
				}
				
	
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			return response;	
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(link_exists == false){
				alertYaRss();
				pDialog.dismiss();
			}else{
			Intent intent = new Intent(FinanceActivity.this,FinanceActivity.class);
			FinanceActivity.this.startActivity(intent);
			pDialog.dismiss();
			}
			super.onPostExecute(result);
		}
		
	}
	
	
	//create dialogs
		//###########################################################################################
		
		@SuppressWarnings("deprecation")
		private void alertYangu(){
			final AlertDialog alertView = new AlertDialog.Builder(this).create();
			alertView.setTitle("Somethings up!!");
			alertView.setMessage("Looks your url isn't valid, please try another url");
			alertView.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					alertView.dismiss();
				}
			});
			alertView.show();
		}
		
		@SuppressWarnings("deprecation")
		private void alertYaRss(){
			final AlertDialog alertView = new AlertDialog.Builder(this).create();
			alertView.setTitle("Somethings up!!");
			alertView.setMessage("Looks the site has no rss url, please try again");
			alertView.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					alertView.dismiss();
				}
			});
			alertView.show();
		}
		
		@SuppressWarnings("deprecation")
		private void alertYanguYaPili(){
			final AlertDialog alertView = new AlertDialog.Builder(this).create();
			alertView.setTitle("Somethings up!!");
			alertView.setMessage("Looks like you havent entered a url or channel name, please do");
			alertView.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					alertView.dismiss();
				}
			});
			alertView.show();
		}
		
		@SuppressWarnings("deprecation")
		private void alertYaNetwork(){
			final AlertDialog alertView = new AlertDialog.Builder(this).create();
			alertView.setTitle("Somethings up!!");
			alertView.setMessage("Looks like your not connected, please turn on internet connectivity");
			alertView.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					alertView.dismiss();
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName("com.android.phone", "com.android.phone.Settings");
					startActivity(intent);
				}
			});
			alertView.show();
		}
	    
	    

	    
	    public static boolean isConnected(Context context){
	    	ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    	
	    	if(netInfo != null && netInfo.isAvailable() && netInfo.isConnected() && netInfo.isConnectedOrConnecting()){
	    		return true;
	    	}
	    	return false;
	    }


	@Override
	public void onBackPressed() {
		Intent intent = new Intent(FinanceActivity.this,MainActivity.class);
		FinanceActivity.this.startActivity(intent);
		super.onBackPressed();
	}
	
	//Override back button press
	
}
	


