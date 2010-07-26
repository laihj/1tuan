package net.laihj.ytuan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Resources;
import android.content.Intent;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import java.util.ArrayList;
import android.widget.Toast;

import net.laihj.ytuan.Site;
import net.laihj.ytuan.SiteAdapter;
import net.laihj.ytuan.XmlHelper;

public class ytuan extends Activity
{

    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_ABOUT = Menu.FIRST+ 1;
    /** Called when the activity is first created. */
    private ArrayList<Site> sites;
    private ListView list;
    private Resources res;
    private DBHelper dbHelper;
    private SiteAdapter siteAdapter;
    private ImageView update;
    private ImageView allread;
    private ImageView getnew;
    private ProgressDialog progressDialog;
    private Animation anim;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		Log.i("start","start");
        setContentView(R.layout.ytuan);
	res = getResources();
	list = (ListView) findViewById(R.id.list);

	anim = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
	
	update = (ImageView) findViewById(R.id.update);
	update.setOnClickListener(imageLinsener);
	allread = (ImageView) findViewById(R.id.allread);
	allread.setAnimation(anim);
	allread.setOnClickListener(imageLinsener);
	getnew = (ImageView) findViewById(R.id.getnew);
	getnew.setOnClickListener(imageLinsener);
    }

    @Override
    public void onResume() {
	super.onResume();
	Log.i("resume","begin");
	ytuanApplication application = (ytuanApplication) getApplication();
	dbHelper = application.getDatabase();
	Log.i("resume","i");
	this.sites =(ArrayList<Site>) dbHelper.getAll("('beijing')");
	application.setList(this.sites);
	this.siteAdapter = new SiteAdapter(this,sites);
	list.setAdapter(this.siteAdapter);
	Log.i("resume","end");
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_SETTING, 0, R.string.setting).setIcon(
            android.R.drawable.ic_menu_preferences);
        menu.add(0, MENU_ABOUT, 0, R.string.update).setIcon(
            android.R.drawable.ic_menu_preferences);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	ytuanApplication application = (ytuanApplication) getApplication();
	switch (item.getItemId()) {
        case MENU_SETTING:
            Intent intent = new Intent("net.laihj.ytuan.SETTING");
	    startActivity(intent);
	    return true;
	case MENU_ABOUT:
	    dbHelper = application.getDatabase();
	    updateSites();
	    return true;
	}
        return true;
    }
    

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
	    if(msg.what == 0 ) {
		progressDialog.dismiss();
		ytuan.this.siteAdapter.notifyDataSetChanged();
	    } else {
		progressDialog.setMessage( res.getString(R.string.getting) + ((Site)sites.get(msg.what -1 )).name + res.getString(R.string.gettingend) );
	    }
	}
	};
    
	    

    public void updateSites() {
	this.progressDialog = ProgressDialog.show(this, res.getString(R.string.update) + "...", "", true, false);
        new Thread() {
            @Override
            public void run() {
		ytuanApplication application = (ytuanApplication) getApplication();
		int count = 1;
		dbHelper = application.getDatabase();
		for(Site site:ytuan.this.sites) {
		    handler.sendEmptyMessage(count);
		    XmlHelper.updateFeed(site);
		    if(site.updated) {
			dbHelper.update(site);
			site.updated = false;
		    }
		    count++;
		}
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    public void getnew() {
	ytuanApplication application = (ytuanApplication) getApplication();
	    dbHelper = application.getDatabase();
	    this.sites = application.getList();
	    long ver = dbHelper.getMaxVersion();
	    ArrayList<Site> updateSite = new ArrayList<Site> ();
	    updateSite = XmlHelper.getUpdateSite(res.getString(R.string.siteurl),ver);
	    if ( 0 == updateSite.size() ) {
		Toast.makeText(this,res.getString(R.string.nonewsite),200).show();
	    } else {
		Toast.makeText(this,res.getString(R.string.newsiteb) + updateSite.size() + res.getString(R.string.newsitee),200).show();
	    }
	    this.sites.addAll(updateSite);
	    for (Site site:updateSite) {
		dbHelper.insert(site);
	    }
	    this.siteAdapter.notifyDataSetChanged();

    }

    public void showDetail(Site site) {
	long index;
	Intent intent = new Intent("net.laihj.ytuan.action.DETAIL");
	index = sites.indexOf(site);
	intent.putExtra("siteid",index);
	startActivity(intent);
    }

    public void allRead() {
	ytuanApplication application = (ytuanApplication) getApplication();
	dbHelper = application.getDatabase();
	for(Site site:this.sites) {
	    site.readed = true;
	    dbHelper.update(site);
	}
	this.siteAdapter.notifyDataSetChanged();

    }

    private OnClickListener imageLinsener = new OnClickListener() {
	    public void onClick(View v) {
		((ImageView) v).startAnimation(ytuan.this.anim);
		switch(v.getId()) {
		case R.id.allread:
		    ytuan.this.allRead();
		    break;
		case R.id.update:
		    ytuan.this.updateSites();
		    break;
		case R.id.getnew:
		    ytuan.this.getnew();
		    break;
		}
	       	((ImageView) v).setBackgroundResource(0);
	    }
	};
    
}
