package com.debugger.appium.spy.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import com.debugger.appium.spy.ui.NodeVisitorHelper;

import javafx.scene.control.TreeItem;
import net.sourceforge.htmlunit.corejs.javascript.NodeTransformer;

/**
 * Example program to list links from a URL.
 */
public class JsoupParser {
	
	
	public TreeItem<NodeTag> createHTMLTreeNode(String html) throws IOException {
		//Validate.isTrue(args.length == 1, "usage: supply url to fetch");
		
		//String html = FileUtils.readFileToString(new File("testjsoup.html"));

		Document doc = Jsoup.parseBodyFragment(html);
		Element  bodyEle = doc.selectFirst("body");
	
		NodeVisitorHelper treeCreator = new NodeVisitorHelper();
		
		bodyEle.traverse(treeCreator);
		TreeItem<NodeTag> root = treeCreator.getTreeNode();
		return root;
		
	}

}