package net.laihj.ytuan;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.util.Log;

public class siteHandler extends DefaultHandler {
    private ArrayList<Site> sites = null;
    private Site site = null;

    private boolean startSite;
    private boolean loca;
    private boolean title;
    private boolean url;
    private boolean backurl;
    private boolean parse;

    private long version;

    public siteHandler(long ver) {
	this.sites = new ArrayList<Site> ();
	this.version = ver;
    }

    @Override
    public void startDocument() throws SAXException {
    }

        @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
	long version;
	if( localName.equals("site") ) {
	    version = Integer.parseInt(getAttributeValue("ver",atts));
	    if (version > this.version) {
		this.startSite = true;
		this.site = new Site();
		this.site.version = version;
	    }
	}

	if (this.startSite) {
	    if ( localName.equals("loca") ) {
		this.loca = true;
	    } else if ( localName.equals("title") ) {
		this.title = true;
	    } else if ( localName.equals("url")) {
		this.url = true;
	    } else if ( localName.equals("backurl")) {
		this.backurl = true;
	    } else if ( localName.equals("parse") ) {
		this.parse = true;
		}
	}
	    
    }
    @Override
     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	if(localName.equals("site")) {
	    if(this.startSite) {
		this.sites.add(this.site);
		this.startSite = false;
	    }
	}

	if (this.startSite) {
	    if ( localName.equals("loca") ) {
		this.loca = false;
	    } else if ( localName.equals("title") ) {
		this.title = false;
	    } else if ( localName.equals("url")) {
		this.url = false;
	    } else if ( localName.equals("backurl") ) {
		this.backurl = false;
	    } else if ( localName.equals("parse") ) {
		this.parse = false;
	    }
	}
    }

    @Override
    public void characters(char[] ch, int start, int length) {
	String chString = "";
        if (ch != null) {
            chString = new String(ch, start, length);
        }

        if (this.startSite) {
            if (this.loca) {
		this.site.location = chString;
	    } else if(this.title) {
		this.site.name = chString;
	    } else if(this.url) {
		this.site.feedurl = chString;
	    } else if(this.backurl) {
		this.site.backurl = chString;
	    } else if (this.parse) {
		this.site.parse = chString;
	    }
	}
    }
    
    public ArrayList<Site> getSites() {
	return this.sites;
    }

    private String getAttributeValue(String attName, Attributes atts) {
        String result = null;
        for (int i = 0; i < atts.getLength(); i++) {
            String thisAtt = atts.getLocalName(i);
            if (attName.equals(thisAtt)) {
                result = atts.getValue(i);
                break;
            }
        }
        return result;
    }


}