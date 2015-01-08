package com.kisese.project_news.storagedb;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.kisese.project_news.R;

public class MainActivityList extends ActionBarActivity {
	RegistrationAdapter adapter_ob;
	RegistrationOpenHelper helper_ob;
	SQLiteDatabase db_ob;
	ListView nameList;
	Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_main);
		nameList = (ListView) findViewById(R.id.lv_name);
		adapter_ob = new RegistrationAdapter(this);

		String[] from = { helper_ob.LINK, helper_ob.HEADLINE, helper_ob.DESCRIPTION };
		int[] to = { R.id.linksy, R.id.headeline, R.id.desceription};
		
		cursor = adapter_ob.queryName();
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				R.layout.row, cursor, from, to);
		nameList.setAdapter(cursorAdapter);
		nameList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle passdata = new Bundle();
				Cursor listCursor = (Cursor) arg0.getItemAtPosition(arg2);
				int nameId = listCursor.getInt(listCursor
						.getColumnIndex(helper_ob.KEY_ID));
				// Toast.makeText(getApplicationContext(),
				// Integer.toString(nameId), 500).show();
				passdata.putInt("keyid", nameId);
				Intent passIntent = new Intent(MainActivityList.this,
						EditActivity.class);
				passIntent.putExtras(passdata);
				startActivity(passIntent);
			}
		});

		
		
		ActionBar bar = getSupportActionBar();
		bar.setIcon(R.drawable.vipi_logo);
		bar.setTitle("Your saved articles");

	}

	@Override
	public void onResume() {
		super.onResume();
		cursor.requery();

	}

}

