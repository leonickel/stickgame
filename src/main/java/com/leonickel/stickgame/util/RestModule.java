package com.leonickel.stickgame.util;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.leonickel.stickgame.web.StatisticServiceRest;

public class RestModule implements Module {

	@Override
	public void configure(Binder binder) {

		//Dependency Injection creation
		binder.install(new DependencyInjector());
		
		//Rest API binds
		binder.bind(StatisticServiceRest.class);
	}

}
