package net.laihj.ytuan;

import java.util.Date;

public class Site
{
    public long id;
    public String feedurl;
    public String backurl;
    public long version;
    public String location;
    public  String name;
    public String title;
    public String summary;
    public boolean showable;
    public boolean readed;
    public Date pubDate;
    public boolean updated;
    public boolean expended;
    public String parse;

    public Site() {
	this.showable = true;
	this.readed = false;
	this.updated = false;
	this.expended = false;
	this.title = "";
	this.parse = "";
    }
    
    public String getSummary() {
	return "[" + this.name + "] " + this.title;
    }
}




