package com.kisese.project_news.storagedb;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.kisese.project_news.R;

public class RegistrationActivity extends ActionBarActivity {
	RegistrationAdapter adapter;
	RegistrationOpenHelper helper;
	EditText fnameEdit, lnameEdit;
	Button submitBtn, resetBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		submitBtn = (Button) findViewById(R.id.btn_submit);

		adapter = new RegistrationAdapter(this);

		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent myIntent = getIntent(); 
				ActionBar bar = getSupportActionBar();
				bar.setIcon(R.drawable.vipi_logo);
				bar.setTitle("Please confirm your action");
				Intent myIntent3 = getIntent(); 
				
				String linkValue =  myIntent3.getStringExtra("link");
				String headlineValue =  myIntent3.getStringExtra("headline");
				String descriptionValue =  myIntent3.getStringExtra("description");
				long val = adapter.insertDetails(linkValue, headlineValue, descriptionValue);
				// Toast.makeText(getApplicationContext(), Long.toString(val),
				// 300).show();
				finish();
			}
		});
	}
}

