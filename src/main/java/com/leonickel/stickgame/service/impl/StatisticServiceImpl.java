package com.leonickel.stickgame.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leonickel.stickgame.dao.StatisticDAO;
import com.leonickel.stickgame.exception.GlobalStatisticNotFoundException;
import com.leonickel.stickgame.exception.UserStatisticNotFoundException;
import com.leonickel.stickgame.model.GlobalStatistic;
import com.leonickel.stickgame.model.Statistic;
import com.leonickel.stickgame.service.StatisticService;
import com.leonickel.stickgame.util.DefaultProperties;
import com.leonickel.stickgame.util.PropertyFinder;

@Singleton
public class StatisticServiceImpl implements StatisticService {

	@Inject
	private StatisticDAO statisticDAO;

	private final Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);
	private final Lock lock = new ReentrantLock();

	@Override
	public GlobalStatistic getGlobalStatistic() {
		final Statistic statistic = statisticDAO.getStatisticByKey(getGlobalStatisticKey());
		if(statistic == null) {
			logger.error("global statistics not found");
			throw new GlobalStatisticNotFoundException("global statistics not found");
		}
		return new GlobalStatistic(statistic.getTotalWon(), statistic.getTotalLost(), calculateWonRatio(statistic));
	}
	
	@Override
	public Statistic getStatisticByUser(String userId) {
		logger.info("getting statistics for user: [{}]", userId);
		final Statistic statistic = statisticDAO.getStatisticByKey(userId);
		if(statistic != null) {
			return statistic;
		}
		logger.error("statistics for user: [{}] not found", userId);
		throw new UserStatisticNotFoundException("statistics not found for user");
	}

	@Override
	public Statistic updateStatisticByUser(String userId, Statistic statistic) {
		Statistic savedStatistic = statisticDAO.getStatisticByKey(userId);
		if(savedStatistic == null) {
			logger.info("statistics for user: [{}] not found, creating it", userId);
			savedStatistic = new Statistic();
		}
		savedStatistic.setTotalWon(savedStatistic.getTotalWon() + statistic.getTotalWon());
		savedStatistic.setTotalLost(savedStatistic.getTotalLost() + statistic.getTotalLost());
		logger.info("updating statistics for user: [{}] totalWon: [{}] totalLost: [{}]", userId, savedStatistic.getTotalWon(), savedStatistic.getTotalLost());
		savedStatistic = statisticDAO.updateStatisticByKey(userId, savedStatistic);

		updateGlobalStatistic(statistic); //TODO In the future put in a MessageQueue flow to avoid blocking main thread

		return savedStatistic;
	}

	private void updateGlobalStatistic(Statistic statistic) {
		Statistic savedGlobalStatistic = null;
		lock.lock(); //Using this lock to avoid multiple threads getting global statistics and updating it.
		try {
			savedGlobalStatistic = statisticDAO.getStatisticByKey(getGlobalStatisticKey());
			if(savedGlobalStatistic == null) {
				savedGlobalStatistic = new Statistic();
			}
			savedGlobalStatistic.setTotalWon(savedGlobalStatistic.getTotalWon() + statistic.getTotalWon());
			savedGlobalStatistic.setTotalLost(savedGlobalStatistic.getTotalLost() + statistic.getTotalLost());
			logger.info("updating global statistics, totalWon: [{}] totalLost: [{}]", savedGlobalStatistic.getTotalWon(), savedGlobalStatistic.getTotalLost());
			statisticDAO.updateStatisticByKey(getGlobalStatisticKey(), savedGlobalStatistic);
		} finally {
			lock.unlock();
		}
	}

	private double calculateWonRatio(final Statistic statistic) {
		 BigDecimal result = new BigDecimal(((double) statistic.getTotalWon()) / (statistic.getTotalWon() + statistic.getTotalLost()));
		 result = result.multiply(new BigDecimal(100));
		 result = result.setScale(2, RoundingMode.HALF_UP);
		 return result.doubleValue();
	}
	
	private String getGlobalStatisticKey() {
		return PropertyFinder.getPropertyValue(DefaultProperties.GLOBAL_STATISTIC_KEY);
	}
}