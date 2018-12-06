package com.debugger.appium.spy.ui;

import java.util.Arrays;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import com.debugger.appium.spy.constants.Constants;
import com.debugger.appium.spy.driver.ElementCoordinates;
import com.debugger.appium.spy.ui.controller.NodeTag;
import com.google.gson.Gson;

import javafx.scene.control.TreeItem;

public class NodeVisitorHelper implements NodeVisitor {

	private TreeItem<NodeTag> treeNode = new TreeItem<NodeTag>();

	@Override
	public void head(Node node, int depth) {
		// start a new node and use it as the current item
		if (Arrays.asList(Constants.IGNORING_TAGS_JSOUP).contains(node.nodeName().toLowerCase())) {
			return;
		}

		if (isNodeHidden(node)) {
			return;
		}

		if (node instanceof TextNode) {
			String textNodeVlue = ((TextNode) node).text();
			if (!textNodeVlue.trim().isEmpty()) {
				System.out.println(((TextNode) node).text());
			}
			return;
		}

		TreeItem<NodeTag> item = new TreeItem<NodeTag>(getNodeTagForTreeNode(node));
		this.treeNode.getChildren().add(item);
		this.treeNode = item;

	}

	@Override
	public void tail(Node node, int depth) {
		if (Arrays.asList(Constants.IGNORING_TAGS_JSOUP).contains(node.nodeName().toLowerCase())) {
			return;
		}

		if (isNodeHidden(node)) {
			return;
		}

		if (node instanceof TextNode) {
			String textNodeVlue = ((TextNode) node).text();
			if (!textNodeVlue.trim().isEmpty()) {
				treeNode.getValue().addAttributes("_textvalue", ((TextNode) node).text());
			}
			return;
		}
		// finish this node by going back to the parent
		this.treeNode = this.treeNode.getParent();

	}

	private boolean isNodeHidden(Node node) {
		String coordinateJson = node.attributes().get(Constants.APPIUM_COORDINATE_ATTRIBUTE);
		ElementCoordinates coordinates = new Gson().fromJson(coordinateJson, ElementCoordinates.class);
		if (coordinates == null) {
			return true;
		}if (coordinates.getHeight() == 0 && coordinates.getWidth() == 0) {
			return true;
		} else {
			return false;
		}

	}

	private NodeTag getNodeTagForTreeNode(Node node) {

		NodeTag tag = new NodeTag();
		tag.setTagName(node.nodeName());
		for (Attribute attr : node.attributes()) {

			tag.addAttributes(attr.getKey(), attr.getValue());

		}

		return tag;
	}

	public TreeItem<NodeTag> getTreeNode() {
		return treeNode;
	}

}
