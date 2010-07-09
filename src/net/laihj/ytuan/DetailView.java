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
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	this.setContentView(R.layout.detail);

	width = getWindowManager().getDefaultDisplay().getWidth() - 18;
	titleView = (TextView) findViewById(R.id.title);
	detailView = (WebView) findViewById(R.id.detail);

	Bundle extras = getIntent().getExtras();
        siteId = extras.getLong("siteid");
    }

    @Override
    public void onResume() {
	super.onResume();
	ytuanApplication application = (ytuanApplication) getApplication();
	sites = application.getList();
	site = (Site) sites.get((int) siteId);
	titleView.setText(this.site.title);
	detailView.loadDataWithBaseURL("ablut:blank",procHtml(this.site.summary), "text/html", "utf-8",null);
    }

    private String procHtml(String summary) {
	StringBuilder sb = new StringBuilder();
	sb.append("<html><head>");
	sb.append("<style type=\"text/css\">");
	sb.append("img { max-width:" + width + "px; width:" + width + "px; width:expression(document.body.clientWidth>" + width + "?\"" + width + "px\":\"auto\"); overflow:hidden; } ");
	Log.i("web",sb.toString());
	sb.append("</style>");
	sb.append("</head><body>");
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

    
}
