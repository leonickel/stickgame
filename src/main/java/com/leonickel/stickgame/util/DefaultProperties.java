package com.leonickel.stickgame.util;

public enum DefaultProperties {
	
	GLOBAL_STATISTIC_KEY("gameduell.global.statistic.key", "globalstatistic"),
	
	REDIS_SERVER_URL("gameduell.redis.server.url", "localhost"),
	REDIS_SERVER_PORT("gameduell.redis.server.port", "6379"),
	REDIS_SERVER_MAX_TOTAL_CONNECTIONS("gameduell.redis.server.max-total-connections", "5000"),
	REDIS_SERVER_MAX_IDLE("gameduell.redis.server.max-idle", "100"),
	REDIS_SERVER_MIN_IDLE("gameduell.redis.server.min-idle", "10"),
	REDIS_SERVER_MAX_WAIT_IN_MILLIS("gameduell.redis.server.max-wait-in-millis", "5000"),
	
	SUCCESS_CONTENT_TYPE("gameduell.success.content-type", "application/json"),
	ERROR_CONTENT_TYPE("gameduell.error.content-type", "text/plain"),
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