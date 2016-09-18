package com.leonickel.stickgame.dao;

import com.leonickel.stickgame.model.Statistic;

public interface StatisticDAO {
	Statistic getStatisticByKey(String key);
	Statistic updateStatisticByKey(String key, Statistic statistic);
}