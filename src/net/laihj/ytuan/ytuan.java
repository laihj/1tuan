package net.laihj.ytuan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Resources;
    

import java.util.ArrayList;

import net.laihj.ytuan.Site;
import net.laihj.ytuan.SiteAdapter;
import net.laihj.ytuan.XmlHelper;

public class ytuan extends Activity
{

    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_UPDATE = Menu.FIRST+ 1;
    final static private int MENU_ABOUT = Menu.FIRST+ 2;
    /** Called when the activity is first created. */
    private ArrayList<Site> sites;
    private ListView list;
    private Resources res;
    private DBHelper dbHelper;
    private SiteAdapter siteAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ytuan);
	res = getResources();
	list = (ListView) findViewById(R.id.list);
    }

    @Override
    public void onResume() {
	super.onResume();
	ytuanApplication application = (ytuanApplication) getApplication();
	dbHelper = application.getDatabase();
	this.sites =(ArrayList<Site>) dbHelper.getAll("('beijing')");
	application.setList(this.sites);
	this.siteAdapter = new SiteAdapter(this,sites);
	list.setAdapter(this.siteAdapter);
	
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_SETTING, 0, R.string.update).setIcon(
            android.R.drawable.ic_menu_preferences);
	menu.add(0, MENU_UPDATE, 0, R.string.update).setIcon(
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

	    dbHelper = application.getDatabase();
	    updateSites();
	    return true;
	case MENU_UPDATE:

	    dbHelper = application.getDatabase();
	    long ver = dbHelper.getMaxVersion();
	    ArrayList<Site> updateSite = new ArrayList<Site> ();
	    updateSite = XmlHelper.getUpdateSite(res.getString(R.string.siteurl),ver);
	    this.sites.addAll(updateSite);
	    for (Site site:updateSite) {
		dbHelper.insert(site);
	    }
	    this.siteAdapter.notifyDataSetChanged();
	    return true;
	case MENU_ABOUT:
	    return true;
	}
        return true;
    }

    public void updateSites() {
	for(Site site:this.sites) {
	    XmlHelper.updateFeed(site);
	    if(site.updated) {
		dbHelper.update(site);
		site.updated = false;
	    }
	}
    }
    
}
