package com.kisese.project_news.news_pages;

import java.io.IOException;
import org.jsoup.Jsoup;


import com.kisese.project_news.R;
import com.kisese.project_news.news_pages.LocalNewsAdapter.Holder;
import com.kisese.rss_reader.SimpleRSSReaderActivity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherAdapter extends BaseAdapter{

	String [] result;
	Context context;
	String[] urlsz = new String[25];
	
	String values;
	
	SharedPreferences sharedPref;
	
	
	int pos;
	
	EditText channel;
	TextView check_url;
	EditText url;
	String ur;
	String ch;
	 
	boolean link_exists = true;
	 
	ProgressDialog pDialog;
	
	int [] imageId;
	
	private static LayoutInflater inflater=null;
	
	//constructor for the adapter
	public WeatherAdapter(WeatherActivity mainActivity, String[] prgmNameList, int[] prgmImages, String[] urls) {
		result=prgmNameList;
		context=mainActivity;
		imageId=prgmImages;
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		urlsz = urls;	
	}

	@Override
	public int getCount() {
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class Holder
	{
		TextView tv;
		TextView label;
		ImageView img;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		View rowView;
		holder=new Holder();
		
		rowView = inflater.inflate(R.layout.program_list_gridviews, null);
		holder.tv=(TextView) rowView.findViewById(R.id.check_channel);
		holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
		holder.label=(TextView) rowView.findViewById(R.id.img_label);
		holder.tv.setText(result[position]);
		

	
		holder.label.setText(result[position].substring(0, Math.min(result[position].length(),1)).toUpperCase());
		//int posi  = (int) (Math.random()*24);
		//holder.img.setImageResource(imageId[posi]);
		holder.img.setImageResource(R.drawable.images);
		//==============================================================================================
		

		rowView.setLongClickable(true);
		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
				Toast.makeText(context, "URL "+ urlsz[position], Toast.LENGTH_LONG).show();
			
				Intent myIntent = new Intent(context.getApplicationContext(), SimpleRSSReaderActivity.class);
				myIntent.putExtra("url", urlsz[position]);
				context.startActivity(myIntent);
				pos = position;
			}
		});
		 

	 rowView.setOnLongClickListener(new View.OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Pick an option please");
			builder.setItems(R.array.options, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            // The 'which' argument contains the index position
	            // of the selected item
	            	
	            	if(which == 0){
	            //=============================================================================================
	            		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	            		LayoutInflater inflater = LayoutInflater.from(context);
	     
	            		alertView = inflater.inflate(R.layout.channel_dialog, null);
				
	            		builder.setView(alertView);
	            		channel  = (EditText)alertView.findViewById(R.id.channel_name);
                      	check_url = (TextView)alertView.findViewById(R.id.chech_url); 
             			url = (EditText)alertView.findViewById(R.id.url);
             			channel.setText(result[position]);
             			url.setText(urlsz[position]);
	            		
	            		
	            		builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
	                      
	            			@Override
	                        public void onClick(DialogInterface dialog, int id) {
	                         
	            			
	            				ch = channel.getText().toString();
	                 			ur = url.getText().toString();
	                        	 // check if user entered any data in EditText and validate it
	                            if (ur.length() > 0 && ch.length()>0) {
	                                check_url.setText("");
	                                String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
	                                if (ur.matches(urlPattern)) {
	                                
	                                	sharedPref = context.getSharedPreferences("weather", Context.MODE_PRIVATE);
	            	            		SharedPreferences.Editor prefs = sharedPref.edit();
	            	            		prefs.remove(result[position]);
	            	            		prefs.commit();
	            	            		//start services
	            	            		
	            	            		if(ur.contains(".xml") || ur.contains(".rss")){
	                                 		prefs.putString(ch, ur);
	                    					prefs.commit();	
	                    					Intent myIntent = new Intent(context.getApplicationContext(), WeatherActivity.class);
	                	    				context.startActivity(myIntent);
	                                 	}else{
	                                 		if(!isConnected(context)){
	                                    		alertYaNetwork();
	                                    	}else{	
	                                    		CheckAgain dotaskagain = new CheckAgain();
	    	                         	        dotaskagain.execute();
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
	                            //context.getApplicationContext().closeOptionsMenu();
	                          //####################################################################################
	                        }
	                    }); 
	            		
	            		myDialog = builder.create();
	            		myDialog.show();
	           //================================================================================================
	            	}
	            	else if(which == 1){
	            		//action for deleting
	            		sharedPref = context.getSharedPreferences("weather", Context.MODE_PRIVATE);
	            		SharedPreferences.Editor prefs = sharedPref.edit();
	            		prefs.remove(result[position]);
	            		prefs.commit();
	            		Intent myIntent = new Intent(context.getApplicationContext(), WeatherActivity.class);
	    				context.startActivity(myIntent);	
	            	}
	        }
	 });
	  builder.create();
	  builder.show();

			return false;
		}
	});
	
		return rowView;
	}
	
	private AlertDialog myDialog;
	View alertView;
	//check link for rss
		private class CheckAgain extends AsyncTask<String, Void, String>{

			@Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            pDialog = new ProgressDialog(context);
		            pDialog.setMessage("Fetching your RSS url ...");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		        }
		 		
			@Override
			protected String doInBackground(String... params) {
					
				String response = "";
				sharedPref = context.getSharedPreferences("weather", Context.MODE_PRIVATE);
        		SharedPreferences.Editor prefs = sharedPref.edit();
				try {
					// Using JSoup library to parse the html source code
					org.jsoup.nodes.Document doc = Jsoup.connect(ur).get();
					// finding rss links which have link[type=application/rss+xml]
					org.jsoup.select.Elements links = doc
							.select("link[type=application/rss+xml]");
					
					Log.d("No of RSS links found", " " + links.size());
					
					// check if urls found or not
					if (links.size() > 0) {
						ur = links.get(0).attr("href").toString();
						//alertYaSuccess();
						link_exists = true;
						
						prefs.putString(ch, ur);
						prefs.commit();	
					} else {
						// finding rss links which have link[type=application/rss+xml]
						org.jsoup.select.Elements links1 = doc
								.select("link[type=application/atom+xml]");
						if(links1.size() > 0){
							ur = links.get(0).attr("href").toString();
							//alertYaSuccess();
							link_exists = true;
							
							prefs.putString(ch, ur);
							prefs.commit();	
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
				Intent intent = new Intent(context,WeatherActivity.class);
				context.startActivity(intent);
				pDialog.dismiss();
				}
				super.onPostExecute(result);
			}
			
		}
		
		//create dialog boxes
		
		//create dialog boxes
		
				@SuppressWarnings("deprecation")
				private void alertYangu(){
					final AlertDialog alertView = new AlertDialog.Builder(context).create();
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
					final AlertDialog alertView = new AlertDialog.Builder(context).create();
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
					final AlertDialog alertView = new AlertDialog.Builder(context).create();
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
					final AlertDialog alertView = new AlertDialog.Builder(context).create();
					alertView.setTitle("Somethings up!!");
					alertView.setMessage("Looks like your not connected, please turn on internet connectivity");
					alertView.setButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							alertView.dismiss();
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.setClassName("com.android.phone", "com.android.phone.Settings");
							context.startActivity(intent);
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

}