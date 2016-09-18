package com.leonickel.stickgame.util;

public class PropertyFinder {

	public static String getPropertyValue(DefaultProperties property) {
		return System.getProperty(property.property(), property.defaultValue());
	}
}