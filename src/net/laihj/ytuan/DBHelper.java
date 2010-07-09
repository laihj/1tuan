package net.laihj.ytuan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import net.laihj.ytuan.Site;


public class DBHelper
{
    public static final String LOG_TAG = "DBHelper";
    public static final String DB_NAME = "Sites";
    public static final String DB_TABLE = "sites";
    public static final int DB_VERSION = 3;

    private static final String CLASSNAME = DBHelper.class.getSimpleName();
    private static final String[] COLS = new String[] { "_id", "site_name", "feedurl","title", "summary", "location","showable","readed","pubdate"};

    private SQLiteDatabase db;
    private final DBOpenHelper dbOpenHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE = "CREATE TABLE "
            + DBHelper.DB_TABLE
            + " (_id INTEGER PRIMARY KEY, site_name TEXT,feedurl TEXT, title TEXT, summary  TEXT, location TEXT, showable INTEGER, readed INTEGER, ver INTEGER, backurl TEXT);";

        public DBOpenHelper(final Context context) {

            super(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            try {
                db.execSQL(DBOpenHelper.DB_CREATE);
            } catch (SQLException e) {
                Log.e(LOG_TAG, DBHelper.CLASSNAME, e);
            }
        }

        @Override
        public void onOpen(final SQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBHelper.DB_TABLE);
            onCreate(db);
        }
    }

    public DBHelper(final Context context)
    {
	this.dbOpenHelper = new DBOpenHelper(context);
	establishDb();
    }

    private void establishDb() {
        if (this.db == null) {
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    public long insert(final Site site) {
	ContentValues values = new ContentValues();
	values.put("site_name",site.name);
	values.put("feedurl",site.feedurl);
	values.put("title",site.title);
	values.put("summary",site.summary);
	values.put("location",site.location);
	values.put("showable",site.showable);
	values.put("readed",site.readed);
	values.put("ver",site.version);
	values.put("backurl", site.backurl);
	return this.db.insert(DBHelper.DB_TABLE, null, values);
    }

    public void update(final Site site) {
	ContentValues values = new ContentValues();
	values.put("site_name",site.name);
	values.put("feedurl",site.feedurl);
	values.put("title",site.title);
	values.put("summary",site.summary);
	values.put("location",site.location);
	values.put("showable",site.showable);
	values.put("readed",site.readed);
	values.put("ver",site.version);
	values.put("backurl",site.backurl);
	this.db.update(DBHelper.DB_TABLE,values,"_id=" + site.id, null);
    }

    public void delete(final long id) {
	this.db.delete(DBHelper.DB_TABLE,"_id=" + id,null);
    }

    public long getMaxVersion() {
	Cursor c = null;
	long ret = 0;
	try {
	    c = this.db.rawQuery("select Max(ver) from sites",null);
	    c.moveToFirst();
	    if( 0 == c.getCount() ) {

	    } else {
		ret = c.getLong(0);
	    }
	} catch (SQLException e) {
	} finally {
	    if (c != null && !c.isClosed()) {
		c.close();
	    }
	}
	return ret;
    }

    //_id INTEGER PRIMARY KEY, site_name TEXT,feedurl TEXT, title TEXT, summary  TEXT, location TEXT, showable INTEGER, readed INTEGER, ver INTEGER
    public List<Site> getAll(String location) {
	ArrayList<Site> ret = new ArrayList<Site> ();
	Cursor c = null;
	try {
	    c = this.db.rawQuery("select * from sites where location IN " + location,null);
	    int numRows = c.getCount();
	    c.moveToFirst();
	    for( int i = 0 ;i < numRows ; i ++) {
		Site site = new Site();
		site.id = c.getLong(0);
		site.name = c.getString(1);
		site.feedurl = c.getString(2);
		site.title = c.getString(3);
		site.summary = c.getString(4);
		site.location = c.getString(5);
		site.showable = (c.getInt(6) == 0) ? false:true;
		site.readed = (c.getInt(7) == 0) ? false:true;
		site.version = c.getLong(8);
		site.backurl = c.getString(9);
		ret.add(site);
	        c.moveToNext();
	    }
	} catch (SQLException e) {
	    Log.v(LOG_TAG,DBHelper.CLASSNAME, e);
	} finally {
	    if (c != null && !c.isClosed()) {
		c.close();
	    }
	}
	return ret;
    }

    static private SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public String DateToSqlite(Date date) {
	if(null == date) {
	    return "";
	} else {
	    return mDateTimeFormat.format(date);
	}
    }

    public Date sqliteToDate(String sqldate) {
	Date mdate = null;
	if( "".equals(sqldate)) {
	    return mdate;
	}
	try {
	    mdate = mDateTimeFormat.parse(sqldate);
	} catch(java.text.ParseException e) {

        }
	return mdate;
    }
}