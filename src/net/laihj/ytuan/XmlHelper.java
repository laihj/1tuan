package net.laihj.ytuan;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import android.util.Log;

import net.laihj.ytuan.siteHandler;
import net.laihj.ytuan.Site;
import net.laihj.ytuan.DBHelper;


import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class XmlHelper {
    public XmlHelper() {
    }

    public static ArrayList<Site> getUpdateSite(String feedurl,long ver) {
	ArrayList<Site> results = new ArrayList<Site> ();
       try {
            URL url = new URL(feedurl);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

	    
	    siteHandler handler = new siteHandler(ver);
            xr.setContentHandler(handler);

            xr.parse(new InputSource(url.openStream()));
            // after parsed, get record
            results = handler.getSites();
        } catch (Exception e) {
	   Log.i("get Sites",e.toString());
        }
       return results;

    }

    public static void updateFeed(Site site) {
	StringBuffer result = new StringBuffer();

	try{
	    URL url = new URL(site.feedurl);
	    Log.i("get",site.feedurl);
	    InputStreamReader isr  = new InputStreamReader(url.openStream());
	    BufferedReader in = new BufferedReader(isr);
	    String inputLine;
	    while ((inputLine = in.readLine()) != null){
		result.append(inputLine);
		}
	    result = result.delete(0,result.indexOf("<item>"));
	    result = result.delete(result.indexOf("</item>") + 7,result.length()-1);
	    String title = result.substring(result.indexOf("<title>") + 7,result.indexOf("</title>"));
	    String Summary;
	    if ( -1 == result.indexOf("CDATA") ) {
		Summary = result.substring(result.indexOf("<description>") + 13,result.indexOf("</description>"));
	    } else {
		Summary = result.substring(result.indexOf("<description>") + 22,result.indexOf("</description>") - 3);
	    }
	    if(title.equals(site.title)) {
	    } else {
		site.title = title;
		site.summary = webReplace(Summary);
		site.readed = false;
		site.updated = true;
	    }
	    in.close();
	    isr.close();
	 }catch(Exception ex){
	    result = new StringBuffer("TIMEOUT");
	    
	 }

    }
    public static String webReplace( String sb ) {

	sb = sb.replace("&lt;","<");
	sb = sb.replace("&gt;",">");
	sb = sb.replace("&amp;","&");
	sb = sb.replace("&quot;","\"");
	sb = sb.replace("&nbsp;"," ");
	sb = sb.replace("&lt;","<");
	sb = sb.replace("&lt;","<");
	return sb;
    }
}