package com.kisese.project_news.custom_listview;



import com.kisese.project_news.R;
import com.kisese.project_news.news_pages.EntertainmentActivity;
import com.kisese.project_news.news_pages.FashionActivity;
import com.kisese.project_news.news_pages.FinanceActivity;
import com.kisese.project_news.news_pages.SportsActivity;
import com.kisese.project_news.news_pages.FunnyActivity;
import com.kisese.project_news.news_pages.LifeActivity;
import com.kisese.project_news.news_pages.LocalNewsActivity;
import com.kisese.project_news.news_pages.PoliticsActivity;
import com.kisese.project_news.news_pages.TechnologyActivity;
import com.kisese.project_news.news_pages.TravelActivity;
import com.kisese.project_news.news_pages.WeatherActivity;
import com.kisese.project_news.news_pages.WorkActivity;
import com.kisese.project_news.news_pages.WorldNewsActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	ListView list;

	String[] web = {
			"Local News",
			"World News",
			"Life",
			"Finance",
			"Sports",
			"Entertainment",
			"Technology",
			"Funny",
			"Fashion",
			"Politics",
			"Travel",
			"Work",
			"Weather"
	} ;
	
	
	String[] description = {
			"The latest local news just in",
			"Whats going on in the world",
			"Trends and lifestyle",
			"Know your money",
			"Latest sports updates",
			"The latest on the entertainment scene",
			"Whats trending in the technology world",
			"Laugh out loud",
			"Whats hot and whats not in fashion",
			"The world of politics",
			"Travel around the world",
			"Work and jobs, what you need to know",
			"Rainy, sunny or just plain"
	};
	Integer[] imageId = {
			R.drawable.local_news,
			R.drawable.world_news,
			R.drawable.life,
			R.drawable.finance,
			R.drawable.football,
			R.drawable.entertainment,
			R.drawable.technology,
			R.drawable.funny,
			R.drawable.fashion,
			R.drawable.politics,
			R.drawable.travel,
			R.drawable.work,
			R.drawable.weather,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_listview);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.vipi_logo);
	    actionBar.setDisplayHomeAsUpEnabled(true);

		
		CustomList adapter = new
				CustomList(MainActivity.this, web, description,imageId);
		list=(ListView)findViewById(R.id.list);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					
					
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view,
		                                    int position, long id) {
		            	//open specific activities for specific list items
		        		if(web[position].equalsIgnoreCase("Local News")){
		        			Intent intent = new Intent(MainActivity.this, LocalNewsActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("World News")){
		        			Intent intent = new Intent(MainActivity.this, WorldNewsActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Life")){
		        			Intent intent = new Intent(MainActivity.this, LifeActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Finance")){
		        			Intent intent = new Intent(MainActivity.this, FinanceActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Sports")){
		        			Intent intent = new Intent(MainActivity.this, SportsActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Entertainment")){
			        		Intent intent = new Intent(MainActivity.this, EntertainmentActivity.class);
			        		MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Technology")){
			        		Intent intent = new Intent(MainActivity.this, TechnologyActivity.class);
			        		MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Funny")){
		        			Intent intent = new Intent(MainActivity.this, FunnyActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Fashion")){
		        			Intent intent = new Intent(MainActivity.this, FashionActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Politics")){
		        			Intent intent = new Intent(MainActivity.this, PoliticsActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Travel")){
		        			Intent intent = new Intent(MainActivity.this, TravelActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Work")){
		        			Intent intent = new Intent(MainActivity.this, WorkActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}else if(web[position].equalsIgnoreCase("Weather")){
		        			Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
		        			MainActivity.this.startActivity(intent);
		        		}
		        		 
		                Toast.makeText(MainActivity.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();

		            }
		        });

				
				
			
	}
	@Override
	public void onBackPressed() {
		Intent intent3 = new Intent(MainActivity.this, WelcomePage.class);
		MainActivity.this.startActivity(intent3);
		super.onBackPressed();
	}	
			
}
