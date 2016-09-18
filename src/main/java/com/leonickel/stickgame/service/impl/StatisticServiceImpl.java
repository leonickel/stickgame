package com.leonickel.stickgame.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leonickel.stickgame.dao.StatisticDAO;
import com.leonickel.stickgame.model.Statistic;
import com.leonickel.stickgame.service.StatisticService;

@Singleton
public class StatisticServiceImpl implements StatisticService {

	@Inject
	private StatisticDAO statisticDAO;
	
	@Override
	public Statistic getStatisticByUser(String userId) {
		return statisticDAO.getStatisticByUser(userId);
	}

	@Override
	public Statistic updateStatisticByUser(String userId, Statistic statistic) {
		Statistic savedStatistic = statisticDAO.getStatisticByUser(userId);
		if(savedStatistic == null) {
			savedStatistic = new Statistic();
		}
		savedStatistic.setTotalWon(savedStatistic.getTotalWon() + statistic.getTotalWon());
		savedStatistic.setTotalLost(savedStatistic.getTotalLost() + statistic.getTotalLost());
		return statisticDAO.updateStatisticByUser(userId, savedStatistic);
	}

}
