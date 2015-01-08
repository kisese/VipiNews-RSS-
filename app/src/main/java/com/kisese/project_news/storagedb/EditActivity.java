package com.kisese.project_news.storagedb;

import com.kisese.project_news.R;
import com.kisese.rss_reader.NewsWebViewActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class EditActivity extends ActionBarActivity {
	RegistrationAdapter regadapter;
	RegistrationOpenHelper openHelper;
	int rowId;
	Cursor c;
	String fNameValue, lNameValue;
	Button editSubmit, btnDelete;
	
	
	private TextView description;
	private TextView headline;
	private TextView link;
	
	String url_str;
	String head_str;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editregister);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.vipi_logo);
		
		link = (TextView) findViewById(R.id.read_link);
		headline = (TextView) findViewById(R.id.read_head);
		description = (TextView) findViewById(R.id.read_desc);
		
		editSubmit = (Button) findViewById(R.id.btn_update);
		btnDelete = (Button) findViewById(R.id.btn_delete);

		Bundle showData = getIntent().getExtras();
		rowId = showData.getInt("keyid");
		// Toast.makeText(getApplicationContext(), Integer.toString(rowId),
		// 500).show();
		regadapter = new RegistrationAdapter(this);

		c = regadapter.queryAll(rowId);

		if (c.moveToFirst()) {
			do {
				link.setText(c.getString(1));
				headline.setText(c.getString(2));
				description.setText(c.getString(3));
				
				url_str = c.getString(1);
				head_str = c.getString(2);
				
			} while (c.moveToNext());
		}

		editSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intentYangu = new Intent(EditActivity.this, NewsWebViewActivity.class);
                intentYangu.putExtra("link", url_str);
                intentYangu.putExtra("headline", head_str);
                startActivity(intentYangu);
			}
		});
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				regadapter.deletOneRecord(rowId);
				finish();
			}
		});
	}
}