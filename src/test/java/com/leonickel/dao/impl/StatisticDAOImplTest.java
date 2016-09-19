package com.leonickel.dao.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonickel.stickgame.dao.impl.StatisticDAOImpl;
import com.leonickel.stickgame.model.Statistic;

public class StatisticDAOImplTest {

	private StatisticDAOImpl statisticDAO;
	
	@Before
	public void setUp() throws Exception {
		statisticDAO = new StatisticDAOImpl();
		statisticDAO.updateStatisticByKey("test1", createStatistic());
	}

	@After
	public void tearDown() throws Exception {
		statisticDAO.removeKey("test1");
		statisticDAO.removeKey("newUserId");
		statisticDAO = null;
	}
	
	@Test
	public void test_get_statistic_valid_user_id() {
		Statistic statistic = statisticDAO.getStatisticByKey("test1");
		Assert.assertTrue(statistic != null && statistic.getTotalWon() == 2);
	}
	
	@Test
	public void test_get_statistic_invalid_user_id() {
		Statistic statistic = statisticDAO.getStatisticByKey("userNotFound");
		Assert.assertTrue(statistic == null);
	}
	
	@Test
	public void test_update_statistic_exist_user_id() {
		Statistic statistic = statisticDAO.updateStatisticByKey("test1", createStatistic());
		Assert.assertTrue(statistic != null && statistic.getTotalWon() == 2); //DAO layer overwrites data
	}
	
	@Test
	public void test_update_statistic_new_user_id() {
		Statistic statistic = statisticDAO.updateStatisticByKey("newUserId", createStatistic());
		Assert.assertTrue(statistic != null && statistic.getTotalWon() == 2);
	}
	
	private Statistic createStatistic() {
		final Statistic statistic = new Statistic();
		statistic.setTotalWon(2);
		statistic.setTotalLost(1);
		return statistic;
	}
}