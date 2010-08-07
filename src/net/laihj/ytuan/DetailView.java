package net.laihj.ytuan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Resources;
import android.webkit.WebView;
import android.widget.TextView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;

import net.youmi.android.AdView;    

import java.util.ArrayList;
import java.lang.StringBuilder;

import net.laihj.ytuan.Site;
import net.laihj.ytuan.SiteAdapter;
import net.laihj.ytuan.XmlHelper;

public class DetailView extends Activity
{

    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_UPDATE = Menu.FIRST+ 1;
    final static private int MENU_ABOUT = Menu.FIRST+ 2;
    private long siteId;
    private ArrayList<Site> sites;
    private Site site;
    private TextView titleView;
    private WebView detailView;
    private long width;
    private ImageView pre;
    private ImageView buy;
    private ImageView next;
    private Animation anim;    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	this.setContentView(R.layout.detail);

	anim = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);

	width = getWindowManager().getDefaultDisplay().getWidth() - 18;
	titleView = (TextView) findViewById(R.id.title);
	detailView = (WebView) findViewById(R.id.detail);
	pre = (ImageView) findViewById(R.id.pre);
	pre.setOnClickListener(imageLinsener);
	buy = (ImageView) findViewById(R.id.buy);
	buy.setOnClickListener(imageLinsener);
	next = (ImageView) findViewById(R.id.next);
	next.setOnClickListener(imageLinsener);


	Bundle extras = getIntent().getExtras();
        siteId = extras.getLong("siteid");
    }

    @Override
    public void onResume() {
	super.onResume();
	ytuanApplication application = (ytuanApplication) getApplication();
	sites = application.getList();
	Log.i("de","de");
	site = (Site) sites.get((int) siteId);
	Log.i("end","end");
	updateDView();
    }

    void updateDView() {
	titleView.setText(this.site.title);
	setTitle(this.site.name);
	detailView.loadDataWithBaseURL("ablut:blank",procHtml(this.site.title,this.site.summary), "text/html", "utf-8",null);
	if( this.site.readed == false) {
	    this.site.readed = true;
	    ytuanApplication application = (ytuanApplication) getApplication();
	    application.getDatabase().update(this.site);
	}
    }

    private String procHtml(String title,String summary) {
	StringBuilder sb = new StringBuilder();
	sb.append("<html><head>");
	sb.append("<style type=\"text/css\">");
	sb.append("img { max-width:" + width + "px; width:" + width + "px; width:expression(document.body.clientWidth>" + width + "?\"" + width + "px\":\"auto\"); overflow:hidden; } ");
	Log.i("web",sb.toString());
	sb.append("</style>");
	sb.append("</head><body>");
	sb.append("<div><b>" + title + "<b></div><br/>");
	sb.append(summary);
	sb.append("</body></html>");
	return sb.toString();
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }
    private void pre() {
	if ( this.sites.indexOf(this.site) - 1 >= 0) {
	    this.site = (Site) this.sites.get( this.sites.indexOf(this.site) - 1 );
	    updateDView();
	}
	
    }

    private void next() {
	if ( this.sites.indexOf(this.site) + 1 <= this.sites.size() - 1) {
	    this.site = (Site) this.sites.get( this.sites.indexOf(this.site) + 1 );
	    updateDView();
	}
    }

    private void buy() {
	Intent i = new Intent(Intent.ACTION_VIEW);
	i.setData(Uri.parse(this.site.backurl));
	startActivity(i);
    }
    
    private OnClickListener imageLinsener = new OnClickListener() {
	    public void onClick(View v) {
		((ImageView) v).startAnimation(DetailView.this.anim);
		switch(v.getId()) {

		case R.id.pre:
		    DetailView.this.pre();
		    break;
		case R.id.buy:
		    DetailView.this.buy();
		    break;
		case R.id.next:
		    DetailView.this.next();
		    break;
		}
	    }
	};

    
}
