package net.laihj.ytuan;

import android.widget.BaseAdapter;
import android.view.View.OnLongClickListener;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.R;


import java.util.List;

import net.laihj.ytuan.Site;
import net.laihj.ytuan.ytuan;

public class SiteAdapter extends BaseAdapter {
    private static final String CLASSTAG = SiteAdapter.class.getSimpleName();
    private final Context context;
    private final List<Site> sites;

    public SiteAdapter(Context context,List<Site> sites) {
	this.context = context;
	this.sites = sites;
    }

    public int getCount() {
	return this.sites.size();
    }

    public Object getItem(int Position) {
	return this.sites.get(Position);
    }

    public long getItemId(int Position) {
	return Position;
    }

    public View getView(int Position, View convertView, ViewGroup parent) {
	Site site = this.sites.get(Position);
	return new siteListView(this.context, site);
    }

    private class siteListView extends LinearLayout {
	private TextView summary;
	private ImageView ib;
	private Site site;
	private Context context;
	public siteListView(Context context, Site site) {
	    super(context);
	    this.site = site;
	    this.context = context;
	    
	    setOrientation(HORIZONTAL);
	    LinearLayout.LayoutParams rlText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
									     ViewGroup.LayoutParams.FILL_PARENT,100);
      	    this.summary = new TextView(context);
	    this.summary.setText(this.site.getSummary());
	    this.summary.setPadding(3,3,3,3);
	    this.summary.setTextSize(19f);
	    this.summary.setMaxLines(2);
	    if(this.site.readed) {
		this.summary.setTextColor(Color.GRAY);
	    } else {
		this.summary.setTextColor(Color.BLACK);
	    }

	    this.addView(this.summary,rlText);
	    this.summary.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
			TextView tv = (TextView) v;
		      	if ( siteListView.this.site.expended ) {
			    tv.setMaxLines(2);
			    siteListView.this.site.expended = false;
		      	} else {
			    tv.setMaxLines(5);
			    siteListView.this.site.expended = true;
		       }
		    }
		});

	    
	    LinearLayout.LayoutParams rlImage = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
									      ViewGroup.LayoutParams.FILL_PARENT,1);
	    this.ib = new ImageView(context);
	    this.ib.setImageResource(android.R.drawable.ic_input_add);
	    this.addView(this.ib,rlImage);
	    this.ib.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
			((ytuan) siteListView.this.context).showDetail(siteListView.this.site);
		    }
		});
	   

	}
	}

}