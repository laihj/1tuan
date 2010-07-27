package net.laihj.ytuan;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.res.Resources;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Date;
import java.lang.Integer;

import net.laihj.ytuan.ytuanApplication;
import net.laihj.ytuan.DBHelper;
import net.laihj.ytuan.Site;
import net.laihj.ytuan.XmlHelper;

public class UpdateService extends Service {

    private DBHelper dbHelper;
    private ArrayList<Site> sites;
    private SharedPreferences prefs;
    private String location;
    private Timer timer;
    private NotificationManager nm;
    private final long A_DAY_LONG = 24 * 60 * 60 * 1000;
    private boolean update;
    private int hour;
    private NotificationManager mNM;
    private Resources res;

    private TimerTask task = new TimerTask() {
	    public void run() {
		int count = 1;
		Log.i("Service","update");
		for(Site site:UpdateService.this.sites) {
		    XmlHelper.updateFeed(site);
		    if(site.updated) {
			dbHelper.update(site);
			site.updated = false;
		    }
		    count++;
		}
		if( count > 0 ) {
		    showNotification(""+count);
		}
		Log.i("Service","update complete");
	    }
	};
    private void showNotification(String count) {
    
        CharSequence text = res.getString(R.string.noti_begin) + count + res.getString(R.string.noti_end);
        Notification notification = new Notification(R.drawable.ytuan, text,
                System.currentTimeMillis());


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ytuan.class), 0);

        notification.setLatestEventInfo(this, res.getString(R.string.noti_title),
                       text, contentIntent);

        mNM.notify(R.string.update, notification);
    }
    
    @Override
    public void onCreate() {
	Log.i("service","create");
	mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	res = getResources();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	ytuanApplication application = (ytuanApplication) getApplication();
	dbHelper = application.getDatabase();
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	update = prefs.getBoolean("update_enable",true);
	if (update) {
	    location = prefs.getString("loca_preference","beijing");
	    this.hour = Integer.parseInt(prefs.getString("time_select_preference","8"));
	    Date updateTime = new Date();
	    updateTime.setHours(this.hour);
	    updateTime.setMinutes(0);
	    this.sites =(ArrayList<Site>) dbHelper.getAll("('" + location + "')");
	    
	    timer = new Timer();
	    timer.scheduleAtFixedRate(task, updateTime, A_DAY_LONG);
	}
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}