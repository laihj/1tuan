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

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import java.util.ArrayList;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import net.laihj.ytuan.Site;
import net.laihj.ytuan.SiteAdapter;
import net.laihj.ytuan.XmlHelper;
import net.laihj.ytuan.UpdateService;

public class ytuan extends Activity
{

    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_ABOUT = Menu.FIRST+ 1;

    final static private int SETTING = 999;
    
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
    private SharedPreferences prefs;
    private String location;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ytuan);
	res = getResources();
	list = (ListView) findViewById(R.id.list);

	anim = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
	
	update = (ImageView) findViewById(R.id.update);
	update.setOnClickListener(imageLinsener);
	allread = (ImageView) findViewById(R.id.allread);
	allread.setOnClickListener(imageLinsener);
	getnew = (ImageView) findViewById(R.id.setting);
	getnew.setOnClickListener(imageLinsener);
    }

    @Override
    public void onResume() {
	super.onResume();

	ytuanApplication application = (ytuanApplication) getApplication();
	dbHelper = application.getDatabase();

	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	location = prefs.getString("loca_preference","beijing");
	Log.i("location",location);
	this.sites =(ArrayList<Site>) dbHelper.getAll("('" + location + "')");
	application.setList(this.sites);
	this.siteAdapter = new SiteAdapter(this,sites);
	list.setAdapter(this.siteAdapter);
	Log.i("resume","end");
	//	startService(new Intent(this, UpdateService.class)); 
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
	/*        menu.add(0, MENU_SETTING, 0, R.string.setting).setIcon(
            android.R.drawable.ic_menu_preferences);
        menu.add(0, MENU_ABOUT, 0, R.string.update).setIcon(
            android.R.drawable.ic_menu_preferences);
	*/
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode,
             Intent data) {
         if (requestCode == SETTING) {
	     Log.i("Service","start service");
	     stopService(new Intent(this, UpdateService.class));
	     startService(new Intent(this, UpdateService.class));

         }
     }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	ytuanApplication application = (ytuanApplication) getApplication();
	switch (item.getItemId()) {
        case MENU_SETTING:

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
	this.progressDialog = ProgressDialog.show(this, res.getString(R.string.update) + "...", res.getString(R.string.getwebset), true, false);
        new Thread() {
            @Override
            public void run() {
		ytuanApplication application = (ytuanApplication) getApplication();
		getnew();
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
	    
	    for (Site site:updateSite) {
		if (site.version > ver) {
		    if(this.location.equals(site.location)) {
			this.sites.add(site);
		    }
		    site.id = dbHelper.insert(site);
		} else {
		    dbHelper.updateByLocation(site);
		}
	    }
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
		case R.id.setting:
		    Intent intent = new Intent("net.laihj.ytuan.SETTING");
		    startActivityForResult(intent,SETTING);		    
		    break;
		}
	    }
	};
    
}
