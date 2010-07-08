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


import java.util.List;

import net.laihj.ytuan.Site;

public class SiteAdapter extends BaseAdapter {
    private static final String CLASSTAG = siteAdapter.class.getSimpleName();
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
	private Site site;
	public siteListView(Context context, Site site) {
	    super(context);
	    this.site = site;
	    setOrientation(VERTICAL);
	    LinearLayout.LayoutParams rlText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
      	    this.summary = new TextView(context);
	    this.summary.setText(this.site.getSummary());
	    this.summary.setTextSize(19f);
	    this.summary.setMaxLines(2);
	    this.addView(this.summary,rlText);
	    LinearLayout.LayoutParams rlImage = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    /*	    this.event = new TextView(context);
	    this.event.setId(1);
	    this.event.setText(myEvent.event);
	    this.event.setTextSize(19f);
	    this.event.setTextColor(Color.WHITE);
	    this.addView(this.event,rlEvent);*/


	}
	}

}