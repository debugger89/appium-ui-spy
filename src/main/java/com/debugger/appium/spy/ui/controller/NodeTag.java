package com.debugger.appium.spy.ui.controller;

import java.util.HashMap;
import java.util.Map;

import com.debugger.appium.spy.constants.Constants;

public class NodeTag {

	private String tagName;

	private Map<String, String> attributes;

	public NodeTag() {
		attributes = new HashMap<String, String>();
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void addAttributes(String attrName, String attrValue) {
		this.attributes.put(attrName, attrValue);
	}

	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(tagName);
		builder.append(" [");
		for (String attr : attributes.keySet()) {

			if (!attr.equals(Constants.APPIUM_COORDINATE_ATTRIBUTE)) {

				builder.append(" " + attr + "=" + attributes.get(attr));
			}
		}

		builder.append("]");

		return builder.toString();

	}
}
