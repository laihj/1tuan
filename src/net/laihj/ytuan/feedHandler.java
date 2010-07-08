package net.laihj.ytuan;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.ext.DefaultHandler2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.lang.StringBuilder;

import net.laihj.ytuan.Site;

public class feedHandler extends DefaultHandler2 {

	final int RSS_TITLE = 1;
	final int RSS_LINK = 2;
	final int RSS_DESCRIPTION = 3;
	final int RSS_CATEGORY = 4;
	final int RSS_PUBDATE = 5;
	
	int depth = 0;
	int currentstate = 0;

    private long version;
    private Site site;

    private StringBuilder sumString;

    public feedHandler(Site site) {
	this.site = site;
    }

    @Override
    public void startDocument() throws SAXException {

    }

        @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		depth++;
		if (localName.equals("channel"))
		{
			currentstate = 0;
			return;
		}
		if (localName.equals("item"))
		{
		    
			return;
		}
		if (localName.equals("title"))
		{
			currentstate = RSS_TITLE;
			return;
		}
		if (localName.equals("description"))
		{
			currentstate = RSS_DESCRIPTION;
			return;
		}
		if (localName.equals("link"))
		{
			currentstate = RSS_LINK;
			return;
		}
		if (localName.equals("category"))
		{
			currentstate = RSS_CATEGORY;
			return;
		}
		if (localName.equals("pubDate"))
		{
			currentstate = RSS_PUBDATE;
			return;
		}

		currentstate = 0;

    }
    @Override
     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	depth--;
     }

    @Override
    public void characters(char[] ch, int start, int length) {
	String chString = "";
        if (ch != null) {
            chString = new String(ch, start, length);
        }
		switch (currentstate)
		{
			case RSS_TITLE:
			    this.site.title = chString;
			    this.site.updated = true;
				currentstate = 0;
				break;
			case RSS_DESCRIPTION:
			    Log.i("des",chString);
			    this.site.summary = chString;

				currentstate = 0;
				break;
			case RSS_CATEGORY:

				currentstate = 0;
				break;
			case RSS_PUBDATE:
				currentstate = 0;
				break;
			default:
				return;
		}
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