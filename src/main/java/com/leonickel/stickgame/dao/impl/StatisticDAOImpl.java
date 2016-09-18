package com.leonickel.stickgame.dao.impl;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.leonickel.stickgame.dao.StatisticDAO;
import com.leonickel.stickgame.model.Statistic;

@Singleton
public class StatisticDAOImpl implements StatisticDAO {

	private final JedisPool jedisPool = createJedisPool();
	private Gson gson = new Gson();
	
	@Override
	public Statistic getStatisticByUser(String userId) {
		return gson.fromJson(jedisPool.getResource().get(appendPrefix(userId)), Statistic.class);
	}

	@Override
	public Statistic updateStatisticByUser(String userId, Statistic statistic) {
		String response = jedisPool.getResource().set(appendPrefix(userId), gson.toJson(statistic));
		System.out.println(response);
		return statistic;
	}

	private String appendPrefix(String key) {
		return new StringBuilder("tsg:").append(key).toString();
	}
	
	private JedisPool createJedisPool() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxWaitMillis(5000);
		poolConfig.setMaxTotal(500);
		poolConfig.setMaxIdle(100);
		poolConfig.setMinIdle(10);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		return new JedisPool(poolConfig, "localhost", 6379);
	}
}
