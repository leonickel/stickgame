package com.leonickel.stickgame.dao;

import com.leonickel.stickgame.model.Statistic;

public interface StatisticDAO {

	Statistic getStatisticByUser(String userId);
	
	Statistic updateStatisticByUser(String userId, Statistic statistic);
}
