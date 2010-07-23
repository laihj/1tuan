package net.laihj.ytuan;

import android.app.Application;

import java.util.ArrayList;

import net.laihj.ytuan.DBHelper;
import net.laihj.ytuan.Site;

public class ytuanApplication extends Application {
    private DBHelper dbHelper;
    private ArrayList<Site> list;

    public ytuanApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public DBHelper getDatabase() {
	if (null == this.dbHelper) {
	    this.dbHelper = new DBHelper(this);
	}
	return this.dbHelper;
    }

    public ArrayList<Site> getList() {
	if(null == this.list) {
	    this.list = new ArrayList<Site> ();
	}
	return this.list;
    }

    public void setList(ArrayList<Site> sites) {
	this.list = sites;
    }

}