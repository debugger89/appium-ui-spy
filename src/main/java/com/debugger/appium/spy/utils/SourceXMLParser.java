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

	public static void main(String argv[]) throws IOException, ParserConfigurationException {

		/*

			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<XCUIElementTypeApplication type=\"XCUIElementTypeApplication\" name=\"Safari\" label=\"Safari\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "   <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"568\" />\n"
					+ "   <XCUIElementTypeWindow type=\"XCUIElementTypeWindow\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "         <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "               <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"548\">\n"
					+ "                  <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"548\">\n"
					+ "                     <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"20\">\n"
					+ "                        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"40\">\n"
					+ "                           <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"40\" />\n"
					+ "                           <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"40\" />\n"
					+ "                        </XCUIElementTypeOther>\n"
					+ "                        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"20\">\n"
					+ "                           <XCUIElementTypeButton type=\"XCUIElementTypeButton\" value=\"‎ebay.com, secure and validated connection\" name=\"URL\" label=\"Address\" enabled=\"true\" visible=\"true\" x=\"54\" y=\"17\" width=\"212\" height=\"21\">\n"
					+ "                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"‎ebay.com, secure and validated connection\" name=\"URL\" label=\"Address\" enabled=\"true\" visible=\"true\" x=\"56\" y=\"17\" width=\"208\" height=\"21\" />\n"
					+ "                           </XCUIElementTypeButton>\n"
					+ "                        </XCUIElementTypeOther>\n"
					+ "                        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"39\" width=\"320\" height=\"1\" />\n"
					+ "                     </XCUIElementTypeOther>\n"
					+ "                     <XCUIElementTypeScrollView type=\"XCUIElementTypeScrollView\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "                        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "                           <XCUIElementTypeWebView type=\"XCUIElementTypeWebView\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"-88\" width=\"320\" height=\"892\">\n"
					+ "                                 <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"0\" height=\"0\">\n"
					+ "                                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"0\" height=\"0\">\n"
					+ "                                       <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-88\" width=\"320\" height=\"569\">\n"
					+ "                                          <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"Electronics, Cars, Fashion, Collectibles, Coupons and More | eBay\" label=\"Electronics, Cars, Fashion, Collectibles, Coupons and More | eBay\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"-88\" width=\"320\" height=\"892\">\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"eBay Message, region\" label=\"eBay Message, region\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-88\" width=\"320\" height=\"82\">\n"
					+ "                                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"View\" label=\"View\" enabled=\"true\" visible=\"false\" x=\"275\" y=\"-57\" width=\"37\" height=\"21\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"View\" name=\"View\" label=\"View\" enabled=\"true\" visible=\"false\" x=\"275\" y=\"-57\" width=\"37\" height=\"21\" />\n"
					+ "                                                </XCUIElementTypeLink>\n"
					+ "                                                <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Dismiss eBay Message\" label=\"Dismiss eBay Message\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-88\" width=\"32\" height=\"81\" />\n"
					+ "                                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"96\" y=\"-88\" width=\"127\" height=\"81\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Faster, Easier, eBay.\" name=\"Faster, Easier, eBay.\" label=\"Faster, Easier, eBay.\" enabled=\"true\" visible=\"false\" x=\"104\" y=\"-65\" width=\"119\" height=\"16\" />\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"It's all in the app\" name=\"It's all in the app\" label=\"It's all in the app\" enabled=\"true\" visible=\"false\" x=\"104\" y=\"-46\" width=\"87\" height=\"15\" />\n"
					+ "                                                </XCUIElementTypeOther>\n"
					+ "                                             </XCUIElementTypeOther>\n"
					+ "                                             <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Skip to main content\" label=\"Skip to main content\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-3\" width=\"127\" height=\"17\">\n"
					+ "                                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Skip to main content\" name=\"Skip to main content\" label=\"Skip to main content\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-3\" width=\"127\" height=\"17\" />\n"
					+ "                                             </XCUIElementTypeLink>\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"Account, navigation\" label=\"Account, navigation\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-7\" width=\"320\" height=\"47\">\n"
					+ "                                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"9\" y=\"-7\" width=\"70\" height=\"44\">\n"
					+ "                                                   <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"1\" name=\"eBay\" label=\"eBay\" enabled=\"true\" visible=\"false\" x=\"9\" y=\"1\" width=\"70\" height=\"31\">\n"
					+ "                                                      <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"1\" name=\"eBay\" label=\"eBay\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-10\" width=\"77\" height=\"37\">\n"
					+ "                                                         <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"1\" name=\"eBay\" label=\"eBay\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"-10\" width=\"77\" height=\"37\" />\n"
					+ "                                                      </XCUIElementTypeLink>\n"
					+ "                                                   </XCUIElementTypeOther>\n"
					+ "                                                </XCUIElementTypeOther>\n"
					+ "                                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"239\" y=\"-4\" width=\"80\" height=\"38\">\n"
					+ "                                                   <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Go to My eBay page\" label=\"Go to My eBay page\" enabled=\"true\" visible=\"false\" x=\"239\" y=\"-4\" width=\"40\" height=\"38\" />\n"
					+ "                                                   <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Go to Shopping cart page\" label=\"Go to Shopping cart page\" enabled=\"true\" visible=\"false\" x=\"279\" y=\"-4\" width=\"40\" height=\"38\" />\n"
					+ "                                                </XCUIElementTypeOther>\n"
					+ "                                             </XCUIElementTypeOther>\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"web dialog\" label=\"web dialog\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"39\" width=\"377\" height=\"49\">\n"
					+ "                                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"39\" width=\"377\" height=\"44\">\n"
					+ "                                                   <XCUIElementTypeSearchField type=\"XCUIElementTypeSearchField\" name=\"Enter your search keyword\" label=\"Enter your search keyword\" enabled=\"true\" visible=\"true\" x=\"9\" y=\"39\" width=\"249\" height=\"44\" />\n"
					+ "                                                   <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Search\" label=\"Search\" enabled=\"true\" visible=\"true\" x=\"258\" y=\"39\" width=\"52\" height=\"44\" />\n"
					+ "                                                </XCUIElementTypeOther>\n"
					+ "                                             </XCUIElementTypeOther>\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"main\" label=\"main\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"103\" width=\"320\" height=\"378\">\n"
					+ "                                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"2\" name=\"Explore popular categories  \" label=\"Explore popular categories  \" enabled=\"true\" visible=\"true\" x=\"17\" y=\"113\" width=\"286\" height=\"29\">\n"
					+ "                                                   <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"2\" name=\"Explore popular categories  \" label=\"Explore popular categories  \" enabled=\"true\" visible=\"true\" x=\"17\" y=\"115\" width=\"269\" height=\"25\">\n"
					+ "                                                      <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"2\" name=\"Explore popular categories  \" label=\"Explore popular categories  \" enabled=\"true\" visible=\"true\" x=\"17\" y=\"115\" width=\"269\" height=\"25\" />\n"
					+ "                                                   </XCUIElementTypeLink>\n"
					+ "                                                </XCUIElementTypeOther>\n"
					+ "                                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"167\" width=\"288\" height=\"314\">\n"
					+ "                                                   <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"167\" width=\"288\" height=\"139\">\n"
					+ "                                                      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"167\" width=\"96\" height=\"115\">\n"
					+ "                                                         <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Under $10\" label=\"Under $10\" enabled=\"true\" visible=\"true\" x=\"26\" y=\"259\" width=\"76\" height=\"20\">\n"
					+ "                                                            <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Under $10\" label=\"Under $10\" enabled=\"true\" visible=\"true\" x=\"26\" y=\"259\" width=\"76\" height=\"20\">\n"
					+ "                                                               <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Under $10\" label=\"Under $10\" enabled=\"true\" visible=\"true\" x=\"26\" y=\"259\" width=\"76\" height=\"20\" />\n"
					+ "                                                            </XCUIElementTypeLink>\n"
					+ "                                                         </XCUIElementTypeLink>\n"
					+ "                                                      </XCUIElementTypeOther>\n"
					+ "                                                      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"112\" y=\"167\" width=\"96\" height=\"139\">\n"
					+ "                                                         <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Health &amp; Beauty\" label=\"Health &amp; Beauty\" enabled=\"true\" visible=\"true\" x=\"129\" y=\"259\" width=\"62\" height=\"44\">\n"
					+ "                                                            <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Health &amp; Beauty\" label=\"Health &amp; Beauty\" enabled=\"true\" visible=\"true\" x=\"129\" y=\"259\" width=\"62\" height=\"44\">\n"
					+ "                                                               <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Health &amp; Beauty\" label=\"Health &amp; Beauty\" enabled=\"true\" visible=\"true\" x=\"129\" y=\"259\" width=\"62\" height=\"44\" />\n"
					+ "                                                            </XCUIElementTypeLink>\n"
					+ "                                                         </XCUIElementTypeLink>\n"
					+ "                                                      </XCUIElementTypeOther>\n"
					+ "                                                      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"208\" y=\"167\" width=\"96\" height=\"139\">\n"
					+ "                                                         <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Parts and Accessories\" label=\"Parts and Accessories\" enabled=\"true\" visible=\"true\" x=\"212\" y=\"259\" width=\"88\" height=\"44\">\n"
					+ "                                                            <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Parts and Accessories\" label=\"Parts and Accessories\" enabled=\"true\" visible=\"true\" x=\"212\" y=\"259\" width=\"88\" height=\"44\">\n"
					+ "                                                               <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Parts and Accessories\" label=\"Parts and Accessories\" enabled=\"true\" visible=\"true\" x=\"212\" y=\"259\" width=\"88\" height=\"44\" />\n"
					+ "                                                            </XCUIElementTypeLink>\n"
					+ "                                                         </XCUIElementTypeLink>\n"
					+ "                                                      </XCUIElementTypeOther>\n"
					+ "                                                   </XCUIElementTypeOther>\n"
					+ "                                                   <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"318\" width=\"288\" height=\"163\">\n"
					+ "                                                      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"318\" width=\"96\" height=\"115\">\n"
					+ "                                                         <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Sneakers\" label=\"Sneakers\" enabled=\"true\" visible=\"true\" x=\"30\" y=\"410\" width=\"68\" height=\"20\">\n"
					+ "                                                            <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Sneakers\" label=\"Sneakers\" enabled=\"true\" visible=\"true\" x=\"30\" y=\"410\" width=\"68\" height=\"20\">\n"
					+ "                                                               <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Sneakers\" label=\"Sneakers\" enabled=\"true\" visible=\"true\" x=\"30\" y=\"410\" width=\"68\" height=\"20\" />\n"
					+ "                                                            </XCUIElementTypeLink>\n"
					+ "                                                         </XCUIElementTypeLink>\n"
					+ "                                                      </XCUIElementTypeOther>\n"
					+ "                                                      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"112\" y=\"318\" width=\"96\" height=\"163\">\n"
					+ "                                                         <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Smartphones &amp; Accessories\" label=\"Smartphones &amp; Accessories\" enabled=\"true\" visible=\"true\" x=\"116\" y=\"410\" width=\"88\" height=\"68\">\n"
					+ "                                                            <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Smartphones &amp; Accessories\" label=\"Smartphones &amp; Accessories\" enabled=\"true\" visible=\"true\" x=\"116\" y=\"410\" width=\"88\" height=\"68\">\n"
					+ "                                                               <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Smartphones &amp; Accessories\" label=\"Smartphones &amp; Accessories\" enabled=\"true\" visible=\"true\" x=\"116\" y=\"410\" width=\"88\" height=\"68\" />\n"
					+ "                                                            </XCUIElementTypeLink>\n"
					+ "                                                         </XCUIElementTypeLink>\n"
					+ "                                                      </XCUIElementTypeOther>\n"
					+ "                                                      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"208\" y=\"318\" width=\"96\" height=\"115\">\n"
					+ "                                                         <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Electronics\" label=\"Electronics\" enabled=\"true\" visible=\"true\" x=\"216\" y=\"410\" width=\"80\" height=\"20\">\n"
					+ "                                                            <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Electronics\" label=\"Electronics\" enabled=\"true\" visible=\"true\" x=\"216\" y=\"410\" width=\"80\" height=\"20\">\n"
					+ "                                                               <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Electronics\" label=\"Electronics\" enabled=\"true\" visible=\"true\" x=\"216\" y=\"410\" width=\"80\" height=\"20\" />\n"
					+ "                                                            </XCUIElementTypeLink>\n"
					+ "                                                         </XCUIElementTypeLink>\n"
					+ "                                                      </XCUIElementTypeOther>\n"
					+ "                                                   </XCUIElementTypeOther>\n"
					+ "                                                </XCUIElementTypeOther>\n"
					+ "                                             </XCUIElementTypeOther>\n"
					+ "                                             <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Home\" label=\"Home\" enabled=\"true\" visible=\"true\" x=\"55\" y=\"538\" width=\"35\" height=\"17\">\n"
					+ "                                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Home\" name=\"Home\" label=\"Home\" enabled=\"true\" visible=\"true\" x=\"55\" y=\"538\" width=\"35\" height=\"17\" />\n"
					+ "                                             </XCUIElementTypeLink>\n"
					+ "                                             <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"My eBay\" label=\"My eBay\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"578\" width=\"51\" height=\"17\">\n"
					+ "                                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"My eBay\" name=\"My eBay\" label=\"My eBay\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"578\" width=\"51\" height=\"17\" />\n"
					+ "                                             </XCUIElementTypeLink>\n"
					+ "                                             <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Sign in / Register\" label=\"Sign in / Register\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"618\" width=\"100\" height=\"17\">\n"
					+ "                                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Sign in / Register\" name=\"Sign in / Register\" label=\"Sign in / Register\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"618\" width=\"100\" height=\"17\" />\n"
					+ "                                             </XCUIElementTypeLink>\n"
					+ "                                             <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Help &amp; Contact\" label=\"Help &amp; Contact\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"658\" width=\"89\" height=\"17\">\n"
					+ "                                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Help &amp; Contact\" name=\"Help &amp; Contact\" label=\"Help &amp; Contact\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"658\" width=\"89\" height=\"17\" />\n"
					+ "                                             </XCUIElementTypeLink>\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"10\" y=\"711\" width=\"300\" height=\"27\">\n"
					+ "                                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"About\" label=\"About\" enabled=\"true\" visible=\"false\" x=\"26\" y=\"716\" width=\"35\" height=\"16\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"About\" name=\"About\" label=\"About\" enabled=\"true\" visible=\"false\" x=\"26\" y=\"716\" width=\"35\" height=\"16\" />\n"
					+ "                                                </XCUIElementTypeLink>\n"
					+ "                                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"User Agreement\" label=\"User Agreement\" enabled=\"true\" visible=\"false\" x=\"70\" y=\"716\" width=\"96\" height=\"16\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"User Agreement\" name=\"User Agreement\" label=\"User Agreement\" enabled=\"true\" visible=\"false\" x=\"70\" y=\"716\" width=\"96\" height=\"16\" />\n"
					+ "                                                </XCUIElementTypeLink>\n"
					+ "                                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Privacy\" label=\"Privacy\" enabled=\"true\" visible=\"false\" x=\"175\" y=\"716\" width=\"43\" height=\"16\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Privacy\" name=\"Privacy\" label=\"Privacy\" enabled=\"true\" visible=\"false\" x=\"175\" y=\"716\" width=\"43\" height=\"16\" />\n"
					+ "                                                </XCUIElementTypeLink>\n"
					+ "                                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Classic site\" label=\"Classic site\" enabled=\"true\" visible=\"false\" x=\"227\" y=\"716\" width=\"67\" height=\"16\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Classic site\" name=\"Classic site\" label=\"Classic site\" enabled=\"true\" visible=\"false\" x=\"227\" y=\"716\" width=\"67\" height=\"16\" />\n"
					+ "                                                </XCUIElementTypeLink>\n"
					+ "                                             </XCUIElementTypeOther>\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"10\" y=\"737\" width=\"300\" height=\"27\">\n"
					+ "                                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Cookies\" label=\"Cookies\" enabled=\"true\" visible=\"false\" x=\"95\" y=\"742\" width=\"48\" height=\"16\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Cookies\" name=\"Cookies\" label=\"Cookies\" enabled=\"true\" visible=\"false\" x=\"95\" y=\"742\" width=\"48\" height=\"16\" />\n"
					+ "                                                </XCUIElementTypeLink>\n"
					+ "                                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"eBay mobile\" label=\"eBay mobile\" enabled=\"true\" visible=\"false\" x=\"152\" y=\"742\" width=\"73\" height=\"16\">\n"
					+ "                                                   <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"eBay mobile\" name=\"eBay mobile\" label=\"eBay mobile\" enabled=\"true\" visible=\"false\" x=\"152\" y=\"742\" width=\"73\" height=\"16\" />\n"
					+ "                                                </XCUIElementTypeLink>\n"
					+ "                                             </XCUIElementTypeOther>\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"773\" width=\"320\" height=\"30\">\n"
					+ "                                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"© 1995 - 2019 eBay Inc.\" name=\"© 1995 - 2019 eBay Inc.\" label=\"© 1995 - 2019 eBay Inc.\" enabled=\"true\" visible=\"false\" x=\"100\" y=\"777\" width=\"120\" height=\"14\" />\n"
					+ "                                             </XCUIElementTypeOther>\n"
					+ "                                             <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"802\" width=\"320\" height=\"2\" />\n"
					+ "                                          </XCUIElementTypeOther>\n"
					+ "                                       </XCUIElementTypeOther>\n"
					+ "                                    </XCUIElementTypeOther>\n"
					+ "                                 </XCUIElementTypeOther>\n"
					+ "                              </XCUIElementTypeOther>\n"
					+ "                           </XCUIElementTypeWebView>\n"
					+ "                        </XCUIElementTypeOther>\n"
					+ "                     </XCUIElementTypeScrollView>\n"
					+ "                  </XCUIElementTypeOther>\n"
					+ "                  <XCUIElementTypeToolbar type=\"XCUIElementTypeToolbar\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"568\" width=\"320\" height=\"45\">\n"
					+ "                     <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"568\" width=\"320\" height=\"1\" />\n"
					+ "                     <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"back\" label=\"Back\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"570\" width=\"43\" height=\"41\" />\n"
					+ "                     <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Forward\" label=\"Forward\" enabled=\"false\" visible=\"false\" x=\"71\" y=\"570\" width=\"42\" height=\"41\" />\n"
					+ "                     <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Share\" label=\"Share\" enabled=\"true\" visible=\"false\" x=\"133\" y=\"569\" width=\"52\" height=\"41\" />\n"
					+ "                     <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Show Bookmarks\" label=\"Show Bookmarks\" enabled=\"true\" visible=\"false\" x=\"197\" y=\"570\" width=\"57\" height=\"41\" />\n"
					+ "                     <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Pages\" label=\"Pages\" enabled=\"true\" visible=\"false\" x=\"263\" y=\"570\" width=\"58\" height=\"41\" />\n"
					+ "                  </XCUIElementTypeToolbar>\n" + "               </XCUIElementTypeOther>\n"
					+ "            </XCUIElementTypeOther>\n" + "         </XCUIElementTypeOther>\n"
					+ "         <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"319\" y=\"0\" width=\"2\" height=\"568\">\n"
					+ "            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"319\" y=\"20\" width=\"2\" height=\"548\" />\n"
					+ "         </XCUIElementTypeOther>\n" + "      </XCUIElementTypeOther>\n"
					+ "   </XCUIElementTypeWindow>\n"
					+ "   <XCUIElementTypeWindow type=\"XCUIElementTypeWindow\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"568\" />\n"
					+ "   </XCUIElementTypeWindow>\n"
					+ "   <XCUIElementTypeWindow type=\"XCUIElementTypeWindow\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n"
					+ "      <XCUIElementTypeStatusBar type=\"XCUIElementTypeStatusBar\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"20\">\n"
					+ "         <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"20\" />\n"
					+ "         <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"20\">\n"
					+ "            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"6\" y=\"0\" width=\"39\" height=\"20\" />\n"
					+ "            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"SSID\" name=\"3 of 3 Wi-Fi bars\" label=\"3 of 3 Wi-Fi bars\" enabled=\"true\" visible=\"true\" x=\"50\" y=\"0\" width=\"13\" height=\"20\" />\n"
					+ "            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"3:37 PM\" label=\"3:37 PM\" enabled=\"true\" visible=\"true\" x=\"138\" y=\"0\" width=\"49\" height=\"20\" />\n"
					+ "            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"-100% battery power\" label=\"-100% battery power\" enabled=\"true\" visible=\"true\" x=\"290\" y=\"0\" width=\"25\" height=\"20\" />\n"
					+ "         </XCUIElementTypeOther>\n" + "      </XCUIElementTypeStatusBar>\n"
					+ "   </XCUIElementTypeWindow>\n" + "</XCUIElementTypeApplication>";
			
			*/
			
			String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><AppiumAUT><XCUIElementTypeApplication type=\"XCUIElementTypeApplication\" name=\"Safari\" label=\"Safari\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"  <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"568\"/>\n" + 
					"  <XCUIElementTypeWindow type=\"XCUIElementTypeWindow\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"          <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"548\">\n" + 
					"            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"548\">\n" + 
					"              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"44\">\n" + 
					"                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"64\">\n" + 
					"                  <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"64\"/>\n" + 
					"                  <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"64\"/>\n" + 
					"                </XCUIElementTypeOther>\n" + 
					"                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"20\" width=\"320\" height=\"44\">\n" + 
					"                  <XCUIElementTypeButton type=\"XCUIElementTypeButton\" value=\"‎ebay.com, secure and validated connection\" name=\"URL\" label=\"Address\" enabled=\"true\" visible=\"true\" x=\"9\" y=\"24\" width=\"302\" height=\"30\">\n" + 
					"                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"‎ebay.com, secure and validated connection\" name=\"URL\" label=\"Address\" enabled=\"true\" visible=\"true\" x=\"11\" y=\"24\" width=\"298\" height=\"30\"/>\n" + 
					"                    <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"ReloadButton\" label=\"reload\" enabled=\"true\" visible=\"true\" x=\"281\" y=\"24\" width=\"30\" height=\"30\"/>\n" + 
					"                  </XCUIElementTypeButton>\n" + 
					"                  <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Cancel\" label=\"Cancel\" enabled=\"true\" visible=\"false\" x=\"324\" y=\"24\" width=\"57\" height=\"30\">\n" + 
					"                    <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Cancel\" name=\"Cancel\" label=\"Cancel\" enabled=\"true\" visible=\"false\" x=\"324\" y=\"28\" width=\"57\" height=\"22\"/>\n" + 
					"                  </XCUIElementTypeButton>\n" + 
					"                </XCUIElementTypeOther>\n" + 
					"                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"64\" width=\"320\" height=\"1\"/>\n" + 
					"              </XCUIElementTypeOther>\n" + 
					"              <XCUIElementTypeScrollView type=\"XCUIElementTypeScrollView\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"                  <XCUIElementTypeWebView type=\"XCUIElementTypeWebView\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"64\" width=\"320\" height=\"891\">\n" + 
					"                      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"0\" height=\"0\">\n" + 
					"                        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"0\" height=\"0\">\n" + 
					"                          <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"64\" width=\"320\" height=\"568\">\n" + 
					"                            <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"Electronics, Cars, Fashion, Collectibles, Coupons and More | eBay\" label=\"Electronics, Cars, Fashion, Collectibles, Coupons and More | eBay\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"64\" width=\"320\" height=\"891\">\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"eBay Message, region\" label=\"eBay Message, region\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"64\" width=\"320\" height=\"81\">\n" + 
					"                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"View\" label=\"View\" enabled=\"true\" visible=\"true\" x=\"275\" y=\"95\" width=\"37\" height=\"20\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"View\" name=\"View\" label=\"View\" enabled=\"true\" visible=\"true\" x=\"275\" y=\"95\" width=\"37\" height=\"20\"/>\n" + 
					"                                </XCUIElementTypeLink>\n" + 
					"                                <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Dismiss eBay Message\" label=\"Dismiss eBay Message\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"64\" width=\"32\" height=\"80\"/>\n" + 
					"                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"96\" y=\"64\" width=\"127\" height=\"80\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Faster, Easier, eBay.\" name=\"Faster, Easier, eBay.\" label=\"Faster, Easier, eBay.\" enabled=\"true\" visible=\"true\" x=\"104\" y=\"87\" width=\"119\" height=\"15\"/>\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"It's all in the app\" name=\"It's all in the app\" label=\"It's all in the app\" enabled=\"true\" visible=\"true\" x=\"104\" y=\"106\" width=\"87\" height=\"14\"/>\n" + 
					"                                </XCUIElementTypeOther>\n" + 
					"                              </XCUIElementTypeOther>\n" + 
					"                              <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Skip to main content\" label=\"Skip to main content\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"149\" width=\"127\" height=\"16\">\n" + 
					"                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Skip to main content\" name=\"Skip to main content\" label=\"Skip to main content\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"149\" width=\"127\" height=\"16\"/>\n" + 
					"                              </XCUIElementTypeLink>\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"Account, navigation\" label=\"Account, navigation\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"145\" width=\"320\" height=\"46\">\n" + 
					"                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"9\" y=\"145\" width=\"70\" height=\"43\">\n" + 
					"                                  <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"1\" name=\"eBay\" label=\"eBay\" enabled=\"true\" visible=\"false\" x=\"9\" y=\"153\" width=\"70\" height=\"30\">\n" + 
					"                                    <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"1\" name=\"eBay\" label=\"eBay\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"142\" width=\"77\" height=\"36\">\n" + 
					"                                      <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"1\" name=\"eBay\" label=\"eBay\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"142\" width=\"77\" height=\"36\"/>\n" + 
					"                                    </XCUIElementTypeLink>\n" + 
					"                                  </XCUIElementTypeOther>\n" + 
					"                                </XCUIElementTypeOther>\n" + 
					"                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"239\" y=\"148\" width=\"80\" height=\"37\">\n" + 
					"                                  <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Go to My eBay page\" label=\"Go to My eBay page\" enabled=\"true\" visible=\"true\" x=\"239\" y=\"148\" width=\"40\" height=\"37\"/>\n" + 
					"                                  <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Go to Shopping cart page\" label=\"Go to Shopping cart page\" enabled=\"true\" visible=\"true\" x=\"279\" y=\"148\" width=\"40\" height=\"37\"/>\n" + 
					"                                </XCUIElementTypeOther>\n" + 
					"                              </XCUIElementTypeOther>\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"web dialog\" label=\"web dialog\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"191\" width=\"377\" height=\"48\">\n" + 
					"                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"191\" width=\"377\" height=\"43\">\n" + 
					"                                  <XCUIElementTypeSearchField type=\"XCUIElementTypeSearchField\" name=\"Enter your search keyword\" label=\"Enter your search keyword\" enabled=\"true\" visible=\"true\" x=\"9\" y=\"191\" width=\"249\" height=\"43\"/>\n" + 
					"                                  <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Search\" label=\"Search\" enabled=\"true\" visible=\"true\" x=\"258\" y=\"191\" width=\"52\" height=\"43\"/>\n" + 
					"                                </XCUIElementTypeOther>\n" + 
					"                              </XCUIElementTypeOther>\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"main\" label=\"main\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"255\" width=\"320\" height=\"377\">\n" + 
					"                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"2\" name=\"Explore popular categories  \" label=\"Explore popular categories  \" enabled=\"true\" visible=\"true\" x=\"17\" y=\"265\" width=\"286\" height=\"28\">\n" + 
					"                                  <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"2\" name=\"Explore popular categories  \" label=\"Explore popular categories  \" enabled=\"true\" visible=\"true\" x=\"17\" y=\"267\" width=\"269\" height=\"24\">\n" + 
					"                                    <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"2\" name=\"Explore popular categories  \" label=\"Explore popular categories  \" enabled=\"true\" visible=\"true\" x=\"17\" y=\"267\" width=\"269\" height=\"24\"/>\n" + 
					"                                  </XCUIElementTypeLink>\n" + 
					"                                </XCUIElementTypeOther>\n" + 
					"                                <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"319\" width=\"288\" height=\"313\">\n" + 
					"                                  <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"319\" width=\"288\" height=\"138\">\n" + 
					"                                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"319\" width=\"96\" height=\"114\">\n" + 
					"                                      <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Under $10\" label=\"Under $10\" enabled=\"true\" visible=\"true\" x=\"26\" y=\"411\" width=\"76\" height=\"19\">\n" + 
					"                                        <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Under $10\" label=\"Under $10\" enabled=\"true\" visible=\"true\" x=\"26\" y=\"411\" width=\"76\" height=\"19\">\n" + 
					"                                          <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Under $10\" label=\"Under $10\" enabled=\"true\" visible=\"true\" x=\"26\" y=\"411\" width=\"76\" height=\"19\"/>\n" + 
					"                                        </XCUIElementTypeLink>\n" + 
					"                                      </XCUIElementTypeLink>\n" + 
					"                                    </XCUIElementTypeOther>\n" + 
					"                                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"112\" y=\"319\" width=\"96\" height=\"138\">\n" + 
					"                                      <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Health &amp; Beauty\" label=\"Health &amp; Beauty\" enabled=\"true\" visible=\"true\" x=\"129\" y=\"411\" width=\"62\" height=\"43\">\n" + 
					"                                        <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Health &amp; Beauty\" label=\"Health &amp; Beauty\" enabled=\"true\" visible=\"true\" x=\"129\" y=\"411\" width=\"62\" height=\"43\">\n" + 
					"                                          <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Health &amp; Beauty\" label=\"Health &amp; Beauty\" enabled=\"true\" visible=\"true\" x=\"129\" y=\"411\" width=\"62\" height=\"43\"/>\n" + 
					"                                        </XCUIElementTypeLink>\n" + 
					"                                      </XCUIElementTypeLink>\n" + 
					"                                    </XCUIElementTypeOther>\n" + 
					"                                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"208\" y=\"319\" width=\"96\" height=\"138\">\n" + 
					"                                      <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Parts and Accessories\" label=\"Parts and Accessories\" enabled=\"true\" visible=\"true\" x=\"212\" y=\"411\" width=\"88\" height=\"43\">\n" + 
					"                                        <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Parts and Accessories\" label=\"Parts and Accessories\" enabled=\"true\" visible=\"true\" x=\"212\" y=\"411\" width=\"88\" height=\"43\">\n" + 
					"                                          <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Parts and Accessories\" label=\"Parts and Accessories\" enabled=\"true\" visible=\"true\" x=\"212\" y=\"411\" width=\"88\" height=\"43\"/>\n" + 
					"                                        </XCUIElementTypeLink>\n" + 
					"                                      </XCUIElementTypeLink>\n" + 
					"                                    </XCUIElementTypeOther>\n" + 
					"                                  </XCUIElementTypeOther>\n" + 
					"                                  <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"470\" width=\"288\" height=\"162\">\n" + 
					"                                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"16\" y=\"470\" width=\"96\" height=\"114\">\n" + 
					"                                      <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Sneakers\" label=\"Sneakers\" enabled=\"true\" visible=\"true\" x=\"30\" y=\"562\" width=\"68\" height=\"19\">\n" + 
					"                                        <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Sneakers\" label=\"Sneakers\" enabled=\"true\" visible=\"true\" x=\"30\" y=\"562\" width=\"68\" height=\"19\">\n" + 
					"                                          <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Sneakers\" label=\"Sneakers\" enabled=\"true\" visible=\"true\" x=\"30\" y=\"562\" width=\"68\" height=\"19\"/>\n" + 
					"                                        </XCUIElementTypeLink>\n" + 
					"                                      </XCUIElementTypeLink>\n" + 
					"                                    </XCUIElementTypeOther>\n" + 
					"                                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"112\" y=\"470\" width=\"96\" height=\"162\">\n" + 
					"                                      <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Smartphones &amp; Accessories\" label=\"Smartphones &amp; Accessories\" enabled=\"true\" visible=\"true\" x=\"116\" y=\"562\" width=\"88\" height=\"67\">\n" + 
					"                                        <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Smartphones &amp; Accessories\" label=\"Smartphones &amp; Accessories\" enabled=\"true\" visible=\"true\" x=\"116\" y=\"562\" width=\"88\" height=\"67\">\n" + 
					"                                          <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Smartphones &amp; Accessories\" label=\"Smartphones &amp; Accessories\" enabled=\"true\" visible=\"true\" x=\"116\" y=\"562\" width=\"88\" height=\"67\"/>\n" + 
					"                                        </XCUIElementTypeLink>\n" + 
					"                                      </XCUIElementTypeLink>\n" + 
					"                                    </XCUIElementTypeOther>\n" + 
					"                                    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"208\" y=\"470\" width=\"96\" height=\"114\">\n" + 
					"                                      <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Electronics\" label=\"Electronics\" enabled=\"true\" visible=\"true\" x=\"216\" y=\"562\" width=\"80\" height=\"19\">\n" + 
					"                                        <XCUIElementTypeLink type=\"XCUIElementTypeLink\" value=\"3\" name=\"Electronics\" label=\"Electronics\" enabled=\"true\" visible=\"true\" x=\"216\" y=\"562\" width=\"80\" height=\"19\">\n" + 
					"                                          <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"3\" name=\"Electronics\" label=\"Electronics\" enabled=\"true\" visible=\"true\" x=\"216\" y=\"562\" width=\"80\" height=\"19\"/>\n" + 
					"                                        </XCUIElementTypeLink>\n" + 
					"                                      </XCUIElementTypeLink>\n" + 
					"                                    </XCUIElementTypeOther>\n" + 
					"                                  </XCUIElementTypeOther>\n" + 
					"                                </XCUIElementTypeOther>\n" + 
					"                              </XCUIElementTypeOther>\n" + 
					"                              <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Home\" label=\"Home\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"690\" width=\"35\" height=\"16\">\n" + 
					"                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Home\" name=\"Home\" label=\"Home\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"690\" width=\"35\" height=\"16\"/>\n" + 
					"                              </XCUIElementTypeLink>\n" + 
					"                              <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"My eBay\" label=\"My eBay\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"730\" width=\"51\" height=\"16\">\n" + 
					"                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"My eBay\" name=\"My eBay\" label=\"My eBay\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"730\" width=\"51\" height=\"16\"/>\n" + 
					"                              </XCUIElementTypeLink>\n" + 
					"                              <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Sign in / Register\" label=\"Sign in / Register\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"770\" width=\"100\" height=\"16\">\n" + 
					"                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Sign in / Register\" name=\"Sign in / Register\" label=\"Sign in / Register\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"770\" width=\"100\" height=\"16\"/>\n" + 
					"                              </XCUIElementTypeLink>\n" + 
					"                              <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Help &amp; Contact\" label=\"Help &amp; Contact\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"810\" width=\"89\" height=\"16\">\n" + 
					"                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Help &amp; Contact\" name=\"Help &amp; Contact\" label=\"Help &amp; Contact\" enabled=\"true\" visible=\"false\" x=\"55\" y=\"810\" width=\"89\" height=\"16\"/>\n" + 
					"                              </XCUIElementTypeLink>\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"10\" y=\"863\" width=\"300\" height=\"26\">\n" + 
					"                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"About\" label=\"About\" enabled=\"true\" visible=\"false\" x=\"26\" y=\"868\" width=\"35\" height=\"15\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"About\" name=\"About\" label=\"About\" enabled=\"true\" visible=\"false\" x=\"26\" y=\"868\" width=\"35\" height=\"15\"/>\n" + 
					"                                </XCUIElementTypeLink>\n" + 
					"                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"User Agreement\" label=\"User Agreement\" enabled=\"true\" visible=\"false\" x=\"70\" y=\"868\" width=\"96\" height=\"15\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"User Agreement\" name=\"User Agreement\" label=\"User Agreement\" enabled=\"true\" visible=\"false\" x=\"70\" y=\"868\" width=\"96\" height=\"15\"/>\n" + 
					"                                </XCUIElementTypeLink>\n" + 
					"                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Privacy\" label=\"Privacy\" enabled=\"true\" visible=\"false\" x=\"175\" y=\"868\" width=\"43\" height=\"15\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Privacy\" name=\"Privacy\" label=\"Privacy\" enabled=\"true\" visible=\"false\" x=\"175\" y=\"868\" width=\"43\" height=\"15\"/>\n" + 
					"                                </XCUIElementTypeLink>\n" + 
					"                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Classic site\" label=\"Classic site\" enabled=\"true\" visible=\"false\" x=\"227\" y=\"868\" width=\"67\" height=\"15\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Classic site\" name=\"Classic site\" label=\"Classic site\" enabled=\"true\" visible=\"false\" x=\"227\" y=\"868\" width=\"67\" height=\"15\"/>\n" + 
					"                                </XCUIElementTypeLink>\n" + 
					"                              </XCUIElementTypeOther>\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"10\" y=\"889\" width=\"300\" height=\"26\">\n" + 
					"                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"Cookies\" label=\"Cookies\" enabled=\"true\" visible=\"false\" x=\"95\" y=\"894\" width=\"48\" height=\"15\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"Cookies\" name=\"Cookies\" label=\"Cookies\" enabled=\"true\" visible=\"false\" x=\"95\" y=\"894\" width=\"48\" height=\"15\"/>\n" + 
					"                                </XCUIElementTypeLink>\n" + 
					"                                <XCUIElementTypeLink type=\"XCUIElementTypeLink\" name=\"eBay mobile\" label=\"eBay mobile\" enabled=\"true\" visible=\"false\" x=\"152\" y=\"894\" width=\"73\" height=\"15\">\n" + 
					"                                  <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"eBay mobile\" name=\"eBay mobile\" label=\"eBay mobile\" enabled=\"true\" visible=\"false\" x=\"152\" y=\"894\" width=\"73\" height=\"15\"/>\n" + 
					"                                </XCUIElementTypeLink>\n" + 
					"                              </XCUIElementTypeOther>\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"925\" width=\"320\" height=\"29\">\n" + 
					"                                <XCUIElementTypeStaticText type=\"XCUIElementTypeStaticText\" value=\"© 1995 - 2019 eBay Inc.\" name=\"© 1995 - 2019 eBay Inc.\" label=\"© 1995 - 2019 eBay Inc.\" enabled=\"true\" visible=\"false\" x=\"100\" y=\"929\" width=\"120\" height=\"13\"/>\n" + 
					"                              </XCUIElementTypeOther>\n" + 
					"                              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"954\" width=\"320\" height=\"1\"/>\n" + 
					"                            </XCUIElementTypeOther>\n" + 
					"                          </XCUIElementTypeOther>\n" + 
					"                        </XCUIElementTypeOther>\n" + 
					"                      </XCUIElementTypeOther>\n" + 
					"                    </XCUIElementTypeOther>\n" + 
					"                  </XCUIElementTypeWebView>\n" + 
					"                </XCUIElementTypeOther>\n" + 
					"              </XCUIElementTypeScrollView>\n" + 
					"            </XCUIElementTypeOther>\n" + 
					"            <XCUIElementTypeToolbar type=\"XCUIElementTypeToolbar\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"524\" width=\"320\" height=\"44\">\n" + 
					"              <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"523\" width=\"320\" height=\"1\"/>\n" + 
					"              <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"back\" label=\"Back\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"526\" width=\"43\" height=\"40\"/>\n" + 
					"              <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Forward\" label=\"Forward\" enabled=\"false\" visible=\"true\" x=\"71\" y=\"526\" width=\"42\" height=\"40\"/>\n" + 
					"              <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Share\" label=\"Share\" enabled=\"true\" visible=\"true\" x=\"133\" y=\"525\" width=\"52\" height=\"40\"/>\n" + 
					"              <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Show Bookmarks\" label=\"Show Bookmarks\" enabled=\"true\" visible=\"true\" x=\"197\" y=\"526\" width=\"57\" height=\"40\"/>\n" + 
					"              <XCUIElementTypeButton type=\"XCUIElementTypeButton\" name=\"Pages\" label=\"Pages\" enabled=\"true\" visible=\"true\" x=\"263\" y=\"526\" width=\"58\" height=\"40\"/>\n" + 
					"            </XCUIElementTypeToolbar>\n" + 
					"          </XCUIElementTypeOther>\n" + 
					"        </XCUIElementTypeOther>\n" + 
					"      </XCUIElementTypeOther>\n" + 
					"      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"319\" y=\"0\" width=\"2\" height=\"568\">\n" + 
					"        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"319\" y=\"20\" width=\"2\" height=\"548\"/>\n" + 
					"      </XCUIElementTypeOther>\n" + 
					"    </XCUIElementTypeOther>\n" + 
					"  </XCUIElementTypeWindow>\n" + 
					"  <XCUIElementTypeWindow type=\"XCUIElementTypeWindow\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"    <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"568\"/>\n" + 
					"  </XCUIElementTypeWindow>\n" + 
					"  <XCUIElementTypeWindow type=\"XCUIElementTypeWindow\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"568\">\n" + 
					"    <XCUIElementTypeStatusBar type=\"XCUIElementTypeStatusBar\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"20\">\n" + 
					"      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"false\" x=\"0\" y=\"0\" width=\"320\" height=\"20\"/>\n" + 
					"      <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"0\" y=\"0\" width=\"320\" height=\"20\">\n" + 
					"        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" enabled=\"true\" visible=\"true\" x=\"6\" y=\"0\" width=\"39\" height=\"20\"/>\n" + 
					"        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" value=\"SSID\" name=\"3 of 3 Wi-Fi bars\" label=\"3 of 3 Wi-Fi bars\" enabled=\"true\" visible=\"true\" x=\"50\" y=\"0\" width=\"13\" height=\"20\"/>\n" + 
					"        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"5:29 PM\" label=\"5:29 PM\" enabled=\"true\" visible=\"true\" x=\"137\" y=\"0\" width=\"50\" height=\"20\"/>\n" + 
					"        <XCUIElementTypeOther type=\"XCUIElementTypeOther\" name=\"-100% battery power\" label=\"-100% battery power\" enabled=\"true\" visible=\"true\" x=\"290\" y=\"0\" width=\"25\" height=\"20\"/>\n" + 
					"      </XCUIElementTypeOther>\n" + 
					"    </XCUIElementTypeStatusBar>\n" + 
					"  </XCUIElementTypeWindow>\n" + 
					"</XCUIElementTypeApplication></AppiumAUT>";
			
			SourceXMLParser parser = new SourceXMLParser();
			parser.getElementBeforeTargetElement("XCUIElementTypeWebView", xml2);
			//saxParser.parse(new InputSource(new StringReader(xml)), handler);

		

	}
}
