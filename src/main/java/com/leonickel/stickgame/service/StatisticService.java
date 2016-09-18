package com.leonickel.stickgame.service;

import com.leonickel.stickgame.model.GlobalStatistic;
import com.leonickel.stickgame.model.Statistic;

public interface StatisticService {
	Statistic getStatisticByUser(String userId);
	Statistic updateStatisticByUser(String userId, Statistic statistic);
	GlobalStatistic getGlobalStatistic();
}