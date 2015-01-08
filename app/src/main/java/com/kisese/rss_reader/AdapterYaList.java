package com.kisese.rss_reader;

import java.util.Collections;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kisese.project_news.R;

public class AdapterYaList extends ArrayAdapter<String>{
	
private final Activity context;
private final List titles;
private final List links;
private final List descriptions;
private final List web;

//String[] image_url;
private int pos;

public AdapterYaList(Activity context,
List headlines, List links, List descriptions, List titles) {
super(context, R.layout.web_list_row, headlines);

this.context = context;
this.web = headlines;
//this.image_url = img_url;
this.descriptions = descriptions;
this.titles = titles;
this.links = links;
}



@Override
public int getCount() {
	// TODO Auto-generated method stub
	return 25;
}



@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.web_list_row, null, true);

 //String img = image_url[position];
try {
    ImageView ico = (ImageView) rowView.findViewById(R.id.web_favicon);
//Picasso.with(context).load(img).into(ico);

    TextView txtTitle = (TextView) rowView.findViewById(R.id.headeline);
    txtTitle.setText(web.get(position).toString());

    TextView txtLink = (TextView) rowView.findViewById(R.id.linksy);
    txtLink.setText(links.get(position).toString());


    TextView txtDescription = (TextView) rowView.findViewById(R.id.desceription);
    txtDescription.setText(descriptions.get(position).toString().replaceAll("\\<.*?\\>", ""));
}catch (ArrayIndexOutOfBoundsException e){
    Toast.makeText(context, "Please turn on your mobile data...", Toast.LENGTH_LONG).show();
}
catch (IndexOutOfBoundsException x){
    Toast.makeText(context, "Please turn on your mobile data...", Toast.LENGTH_LONG).show();
}

return rowView;
}
}