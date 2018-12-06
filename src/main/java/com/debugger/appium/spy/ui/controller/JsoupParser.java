package com.debugger.appium.spy.ui.controller;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.debugger.appium.spy.ui.NodeVisitorHelper;

import javafx.scene.control.TreeItem;

/**
 * Parses the page source for web context.
 */
public class JsoupParser {
	
	
	public TreeItem<NodeTag> createHTMLTreeNode(String html) throws IOException {
		
		Document doc = Jsoup.parseBodyFragment(html);
		Element  bodyEle = doc.selectFirst("body");
	
		NodeVisitorHelper treeCreator = new NodeVisitorHelper();
		
		bodyEle.traverse(treeCreator);
		TreeItem<NodeTag> root = treeCreator.getTreeNode();
		return root;
		
	}

}