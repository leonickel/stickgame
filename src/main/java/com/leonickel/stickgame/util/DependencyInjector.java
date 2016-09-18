package com.leonickel.stickgame.util;

import com.google.inject.AbstractModule;
import com.leonickel.stickgame.dao.StatisticDAO;
import com.leonickel.stickgame.dao.impl.StatisticDAOImpl;
import com.leonickel.stickgame.service.StatisticService;
import com.leonickel.stickgame.service.impl.StatisticServiceImpl;

public class DependencyInjector extends AbstractModule {

	@Override
	protected void configure() {
		//Service and DAO classes
		bind(StatisticService.class).to(StatisticServiceImpl.class);
		bind(StatisticDAO.class).to(StatisticDAOImpl.class);
		
		//Response implementation
	}
}