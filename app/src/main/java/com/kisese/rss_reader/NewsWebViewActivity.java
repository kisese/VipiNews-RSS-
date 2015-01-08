package com.kisese.rss_reader;

import java.io.File;

import com.kisese.project_news.R;
import com.kisese.project_news.storagedb.RegistrationActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class NewsWebViewActivity extends ActionBarActivity {

	private WebView browser;
	private String url_string;
	private String headline;
	private Button button;
	private String current_url;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		browser = (WebView)findViewById(R.id.webView1);
		button = (Button)findViewById(R.id.button1);
		button.setClickable(false);
		
		Intent myIntentc = getIntent();
		url_string = myIntentc.getStringExtra("link");
	
			
		 DisplayWebViewTask task = new DisplayWebViewTask();
	     task.execute();
	     browser.setClickable(false);
	     browser.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return event.getAction() == MotionEvent.ACTION_MOVE;
			}
		});
	    
	   //add the custom view to the actionBar
			ActionBar actionBar = getSupportActionBar();
			//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ced1")));
			
			actionBar.setIcon(R.drawable.vipi_logo);
			actionBar.show();
			
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		//inflater.notifyAll();
		//Inflate the ActionBar with this menu layout
		inflater.inflate(R.menu.web_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//on actionBar item clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.action_share_channel:
			
			Intent myIntenta = getIntent(); 
			
			String shareBody = myIntenta.getStringExtra("description").toString().replaceAll("\\<.*?\\>", "") + " \n" + myIntenta.getStringExtra("link").toString() + "\n shared from - Vipi \n" +
					" get Vipi on DropBox here \n ";
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Here is your link, enjoy");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
			
			
			break;

		case R.id.action_save_channel:
			Intent myIntent2 = getIntent(); 
			
			Intent intent = new Intent(NewsWebViewActivity.this, RegistrationActivity.class);
            intent.putExtra("link", myIntent2.getStringExtra("link").toString());
            intent.putExtra("headline", myIntent2.getStringExtra("headline").toString());
            intent.putExtra("description", myIntent2.getStringExtra("description").toString());
			NewsWebViewActivity.this.startActivity(intent);
			//open database view n pass intents with data to populate database maybe use a constructor like in lists
			break;
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	


	
	private class MyBrowser extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url == current_url)
			view.loadUrl(url);
			return true;
		}
	}
	
	
	private class DisplayWebViewTask extends AsyncTask<String, Void, String>{

		private ProgressDialog pDialog;
		
		

		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewsWebViewActivity.this);
            pDialog.setMessage("Fetching RSS Information ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.getSecondaryProgress();
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... params) {
			Intent myIntent = getIntent();
			headline = myIntent.getStringExtra("headline");
			browser.getSettings().setLoadsImagesAutomatically(true);
			browser.getSettings().setJavaScriptEnabled(true);
			browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			browser.getSettings().setAppCacheEnabled(true);
			browser.loadUrl(url_string);
			
			
			
			//to be used for disabling links
			current_url = url_string;
			return url_string;
		}
		
	    @Override
				protected void onPostExecute(String result) {
			    	 pDialog.dismiss();

            Toast.makeText(getApplicationContext(), "Article loaded, scroll to read...", Toast.LENGTH_LONG).show();
			    	 button.setText(headline);
			    	browser.setWebViewClient(new MyBrowser());
					super.onPostExecute(result);
				}
	}
	
	  @Override
	  protected void onDestroy()
	  {
	    // Clear the cache (this clears the WebViews cache for the entire application)
	   // browser.clearCache(true);
	     
	    super.onDestroy();
	  }
	   
	  @Override
	  public File getCacheDir()
	  {
	    // NOTE: this method is used in Android 2.1
	     
	    return getApplicationContext().getCacheDir();
	  }
	  
	  //reduce webview memory footprint(
	  @Override
	  public void onDetachedFromWindow(){
		  super.onDetachedFromWindow();
		  
		  browser.removeAllViews();
		  if(browser != null){
			  browser.setTag(null);
			  browser.clearHistory();
			  browser.removeAllViews();
			  browser.destroy();
		  }
	  }
}
