package com.leonickel.stickgame.util;

public enum DefaultProperties {
	
	ERROR_CONTENT_TYPE("movingimage.error.content-type", "text/plain"),
	;
	
	private String property;
	private String defaultValue;
	
	private DefaultProperties(String property, String defaultValue) {
		this.property = property;
		this.defaultValue = defaultValue;
	}
	
	public String property() {
		return property;
	}
	
	public String defaultValue() {
		return defaultValue;
	}
}