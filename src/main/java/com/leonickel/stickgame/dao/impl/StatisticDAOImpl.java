package com.leonickel.stickgame.dao.impl;

import static com.leonickel.stickgame.util.DefaultProperties.REDIS_SERVER_MAX_IDLE;
import static com.leonickel.stickgame.util.DefaultProperties.REDIS_SERVER_MAX_TOTAL_CONNECTIONS;
import static com.leonickel.stickgame.util.DefaultProperties.REDIS_SERVER_MAX_WAIT_IN_MILLIS;
import static com.leonickel.stickgame.util.DefaultProperties.REDIS_SERVER_MIN_IDLE;
import static com.leonickel.stickgame.util.DefaultProperties.REDIS_SERVER_PORT;
import static com.leonickel.stickgame.util.DefaultProperties.REDIS_SERVER_URL;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.leonickel.stickgame.dao.StatisticDAO;
import com.leonickel.stickgame.model.Statistic;
import com.leonickel.stickgame.util.PropertyFinder;

@Singleton
public class StatisticDAOImpl implements StatisticDAO {

	private final JedisPool jedisPool = createJedisPool();
	private final Gson gson = new Gson();
	
	@Override
	public Statistic getStatisticByKey(String key) {
		return gson.fromJson(jedisPool.getResource().get(appendPrefix(key)), Statistic.class);
	}

	@Override
	public Statistic updateStatisticByKey(String key, Statistic statistic) {
		jedisPool.getResource().set(appendPrefix(key), gson.toJson(statistic)); //Setting key without ttl, which means, store this data forever until redis gets full
		return statistic;
	}

	/**
	 * For unit testing purpose only
	 * @param key key to me removed
	 */
	public void removeKey(String key) {
		jedisPool.getResource().del(appendPrefix(key));
	}

	private String appendPrefix(String key) {
		return new StringBuilder("tsg:").append(key).toString();
	}
	
	private JedisPool createJedisPool() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(Integer.parseInt(PropertyFinder.getPropertyValue(REDIS_SERVER_MAX_TOTAL_CONNECTIONS)));
		poolConfig.setMaxIdle(Integer.parseInt(PropertyFinder.getPropertyValue(REDIS_SERVER_MAX_IDLE)));
		poolConfig.setMinIdle(Integer.parseInt(PropertyFinder.getPropertyValue(REDIS_SERVER_MIN_IDLE)));
		poolConfig.setMaxWaitMillis(Long.parseLong(PropertyFinder.getPropertyValue(REDIS_SERVER_MAX_WAIT_IN_MILLIS)));
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		return new JedisPool(poolConfig, PropertyFinder.getPropertyValue(REDIS_SERVER_URL), Integer.parseInt(PropertyFinder.getPropertyValue(REDIS_SERVER_PORT)));
	}
}