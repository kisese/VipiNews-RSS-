package com.kisese.rss_reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.kisese.project_news.R;
import com.kisese.project_news.custom_listview.MainActivity;
import com.kisese.project_news.news_pages.LocalNewsActivity;
import com.kisese.project_news.news_pages.SportsActivity;
import com.kisese.project_news.storagedb.MainActivityList;
import com.kisese.project_news.storagedb.RegistrationActivity;


public class SimpleRSSReaderActivity extends ActionBarActivity implements OnQueryTextListener{
	 
    List headlines;
    List links;
    List dates;
    List descriptions;
    List titles;
    String[] img_url;
    String[] image_url;
    
    Element image;
    Document doc;
    
    String url_string;
    String[] web;
    int i2 = 0;
    int pos;
    ImageView refresh;
    AdapterYaList adapter;
	ImageView ico;
	private ListView list;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
     
        
        ico = (ImageView)findViewById(R.id.web_favicon);
        headlines = new ArrayList<String>();
        links = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        titles = new ArrayList<String>();
        //img_url = new ArrayList<String>();
        //st = new ArrayList();
        
    
    	adapter = new
				AdapterYaList(SimpleRSSReaderActivity.this, headlines ,links, descriptions, titles);
    	list = (ListView)findViewById(R.id.web_list);
    	
    	list.setTextFilterEnabled(true);
    	//list.setLongClickable(true);
    	

    	
    	/*
    	 * Check whether kuna network and prevent the app from force closing
    	 *
    	 */
    	list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    Uri uri = Uri.parse((String) links.get(position).toString());
                    Intent intentYangu = new Intent(SimpleRSSReaderActivity.this, NewsWebViewActivity.class);
                    intentYangu.putExtra("link", uri.toString());
                    intentYangu.putExtra("headline", titles.get(position).toString());
                    intentYangu.putExtra("description", descriptions.get(position).toString());
                    startActivity(intentYangu);
                }catch (IndexOutOfBoundsException e){
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT);
                }
            	}
            });
    	
    	
        
    	if(!isConnected(this)){
    		alertYaNetwork();
    	}else{	
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute();
    	}
  
    	
    	   ActionBar actionBar = getSupportActionBar();
   		//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ced1")));
   		actionBar.getThemedContext();
   		actionBar.addOnMenuVisibilityListener(null);
   		actionBar.setDisplayHomeAsUpEnabled(true);
   		actionBar.setIcon(R.drawable.vipi_logo);
   		actionBar.show();
    }

    
    private class DownloadWebPageTask extends AsyncTask<String, Void, String>{
    	
    	private ProgressDialog pDialog;
		


		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SimpleRSSReaderActivity.this);
            pDialog.setMessage("Fetching RSS Information ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

		@Override
		protected String doInBackground(String... params) {
			 try {
		           // URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");
		           // URL url = new URL("http://www.standardmedia.co.ke/rss");
		            //URL url = new URL("http://www.nation.co.ke/rss");
				
				//Get the url passed through shared preferences
				Intent myIntent = getIntent();
				url_string = myIntent.getStringExtra("url");

		        URL url = new URL(url_string);
		         
		        SyndFeedInput input = new SyndFeedInput();
		        SyndFeed feed = input.build(new XmlReader(url));
		        
		        headlines = feed.getEntries();
		        //img_url[i] = feed.getImage().getLink().toString();
		        //links = feed.getLinks();
		 
		        
		        Iterator h_lines = headlines.listIterator();
		        //Iterator lnks = links.listIterator();
		       
		        
		        while (h_lines.hasNext())
		        { 	
		        SyndEntry ent = (SyndEntry) h_lines.next();
		        
		        String title = ent.getTitle();
		        
		        adapter.add(title);
		        links.add(ent.getLink());
		        descriptions.add(ent.getDescription().getValue());
		        titles.add(ent.getTitle());
		        }
		        adapter.notifyDataSetChanged();
		        
		        } catch (MalformedURLException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FeedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return null;	        
		    }
		    
		    public InputStream getInputStream(URL url) {
		    	   try {
		    	       return url.openConnection().getInputStream();
		    	   } catch (IOException e) {
		    	       return null;
		    	     }
		    	}
		    
		    
		    @Override
			protected void onPostExecute(String result) {
		    	   // Binding data
		    	pDialog.dismiss();
		    	list.setAdapter(adapter);
		    	adapter.notifyDataSetChanged();	
		    	Toast.makeText(getApplicationContext(), "#Feeds retrieved: " + headlines.size(), Toast.LENGTH_SHORT);
				super.onPostExecute(result);
			}

    }
    
  

		    
		    @SuppressWarnings("deprecation")
			private void alertYaNetwork(){
				final AlertDialog alertView = new AlertDialog.Builder(this).create();
				alertView.setTitle("Somethings up!!");
				alertView.setMessage("Looks like your not connected to the internet, please turn on mobile data or wifi");
				alertView.setButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
                        Intent intent = new Intent(SimpleRSSReaderActivity.this, MainActivity.class);
                        SimpleRSSReaderActivity.this.startActivity(intent);
						alertView.dismiss();

					}
				});
				alertView.show();
			}
		    
		    
		
		    
		    public static boolean isConnected(Context context){
		    	ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    	NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    	
		    	if(netInfo != null && netInfo.isAvailable() && netInfo.isConnected() && netInfo.isConnectedOrConnecting()
		    			&& !netInfo.isFailover()){
		    		return true;
		    	}
		    	
		   
		    	return false;
		    }
		    
			@Override
			public void onBackPressed() {
				Intent intent = new Intent(SimpleRSSReaderActivity.this,MainActivity.class);
				SimpleRSSReaderActivity.this.startActivity(intent);
				super.onBackPressed();
			}
			
			
			@SuppressLint("NewApi")
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				MenuInflater inflater = getMenuInflater();
				//inflater.notifyAll();
				//Inflate the ActionBar with this menu layout
				inflater.inflate(R.menu.web_list, menu);
				return super.onCreateOptionsMenu(menu);
			}

			//on actionBar item clicked
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				
				switch(item.getItemId()){
				case R.id.saved_channels:
					Intent intent = new Intent(SimpleRSSReaderActivity.this, MainActivityList.class);
					startActivity(intent);
					break;	
				default:
					break;
				}
				
				return super.onOptionsItemSelected(item);
			}
			
			@Override
			 public boolean onQueryTextChange(String newText)
			 {
			  // this is your adapter that will be filtered
			      if (TextUtils.isEmpty(newText))
			      {
			            list.clearTextFilter();
			        }
			      else
			      {
			            list.setFilterText(newText.toString());
			        }
			         
			      return true;
			 }
			 
			 @Override
			 public boolean onQueryTextSubmit(String query) {
			  // TODO Auto-generated method stub
			  return false;
			 }
			 
	}
			

  
       