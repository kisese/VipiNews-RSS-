package com.kisese.project_news.custom_listview;


import com.kisese.project_news.R;
import com.kisese.project_news.storagedb.MainActivityList;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomePage extends ActionBarActivity {

	private Button openSaved;
	private Button openMain;
	private ImageView help;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_page);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ActionBar bar = getSupportActionBar();
		bar.hide();
		
		help = (ImageView)findViewById(R.id.help_btn);
		openSaved = (Button)findViewById(R.id.open_saved);
		openMain = (Button)findViewById(R.id.open_main);
		
		final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		openSaved.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openSaved.startAnimation(animationFadeOut);
				Intent intent = new Intent(WelcomePage.this, MainActivityList.class);
    			WelcomePage.this.startActivity(intent);			
    			}
		});
	
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog myDialog;
				View alertView;
				AlertDialog.Builder builder = new AlertDialog.Builder(WelcomePage.this);
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				alertView = inflater.inflate(R.layout.help, null);
				
				builder.setView(alertView);
				myDialog = builder.create();
				myDialog.show();
			}
		});
		
		openMain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openMain.startAnimation(animationFadeOut);
				Intent intent = new Intent(WelcomePage.this, MainActivity.class);
    			WelcomePage.this.startActivity(intent);	
			}
		});
    }



	

}

