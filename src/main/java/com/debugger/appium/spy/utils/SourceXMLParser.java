package com.debugger.appium.spy.utils;


import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openqa.selenium.Point;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SourceXMLParser {

	public SourceXMLParser() {

	}

	private int highestYvalue = 0;
	private String targetTagName;
	private int lastHigheighstYValuedElementHeight = 0;

	public Point getElementBeforeTargetElement(String targetTagName, String xml)
			throws IOException, ParserConfigurationException {

		this.targetTagName = targetTagName;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			saxParser.parse(new InputSource(new StringReader(xml)), new SourceTreeHandler());

		} catch (SAXException e) {
			System.out.println("Best Y Position found = " + highestYvalue + " || Height : "+lastHigheighstYValuedElementHeight);
			return new Point(0, (highestYvalue+lastHigheighstYValuedElementHeight));
		}

		return null;

	}

	private class SourceTreeHandler extends DefaultHandler {

		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {

			if (qName.equalsIgnoreCase(targetTagName)) {

				System.out.println("Webview Found. Exiting..");
				throw new SAXException("Found the Webview.");
			}

			String yValue = attributes.getValue("y");
			
			//Check if the tag has a Y attribute. Else continue.
			if(yValue == null) {
				return;
			}
			int yValueNum = Integer.parseInt(yValue);
			
			if (yValueNum > highestYvalue) {
				highestYvalue = yValueNum;
				String hValue = attributes.getValue("height");
				int hValueNum = Integer.parseInt(hValue);
				lastHigheighstYValuedElementHeight = hValueNum;
			}

			System.out.println(qName + " Y : " + yValue );

		}
	}


}
