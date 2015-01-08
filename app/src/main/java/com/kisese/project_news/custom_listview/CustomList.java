package com.kisese.project_news.custom_listview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisese.project_news.R;

public class CustomList extends ArrayAdapter<String>{
	
private final Activity context;
private final String[] web;
private final String[] description;
private final Integer[] imageId;

public CustomList(Activity context,
String[] web,String[] description,Integer[] imageId) {
super(context, R.layout.list_row, web);
this.context = context;
this.web = web;
this.description = description;
this.imageId = imageId;
}
@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.list_row, null, true);

TextView txtTitle = (TextView) rowView.findViewById(R.id.headeline);
txtTitle.setText(web[position]);

TextView txtDescription = (TextView) rowView.findViewById(R.id.linksy);
txtDescription.setText(description[position]);

ImageView imageView = (ImageView) rowView.findViewById(R.id.web_favicon);
imageView.setImageResource(imageId[position]);
return rowView;
}
}