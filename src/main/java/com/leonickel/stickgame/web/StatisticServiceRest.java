package com.leonickel.stickgame.web;

import static com.leonickel.stickgame.util.DefaultProperties.*;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.leonickel.stickgame.exception.GlobalStatisticNotFoundException;
import com.leonickel.stickgame.exception.UserStatisticNotFoundException;
import com.leonickel.stickgame.model.Statistic;
import com.leonickel.stickgame.service.StatisticService;
import com.leonickel.stickgame.util.PropertyFinder;

@Path("/service/statistics")
public class StatisticServiceRest {

	@Inject
	private StatisticService statisticService;

	private final Logger logger = LoggerFactory.getLogger(StatisticServiceRest.class);
	private final Gson gson = new Gson();

	@GET
	@Path("/")
	public Response getGlobalStatistic() {
		try {
			return Response.status(SC_OK)
					.entity(gson.toJson(statisticService.getGlobalStatistic()))
					.header("Content-Type", PropertyFinder.getPropertyValue(SUCCESS_CONTENT_TYPE))
					.build();
		} catch (GlobalStatisticNotFoundException e) {
			return writeErrorResponse(e.getMessage(), SC_NOT_FOUND);
		} catch (Exception e) {
			return writeErrorResponse(e.getMessage(), SC_INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("/{userId}/")
	public Response getByUser(@PathParam("userId") String userId) {
		try {
			return Response.status(SC_OK)
					.entity(gson.toJson(statisticService.getStatisticByUser(userId)))
					.header("Content-Type", PropertyFinder.getPropertyValue(SUCCESS_CONTENT_TYPE))
					.build();
		} catch (UserStatisticNotFoundException e) {
			logger.info("statistics for user: [{}] not found, returning empty response in order to avoid errors on frontend integration", userId);
			return writeEmptyResponse(SC_OK);
		} catch (Exception e) {
			return writeErrorResponse(e.getMessage(), SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	@PUT
	@Path("/{userId}/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStatisticByUser(@PathParam("userId") String userId, String statisticJSON) {
		try {
			final Statistic statistic = statisticService.updateStatisticByUser(userId, gson.fromJson(statisticJSON, Statistic.class));
			return Response.status(SC_OK)
					.entity(gson.toJson(statistic))
					.header("Content-Type", PropertyFinder.getPropertyValue(SUCCESS_CONTENT_TYPE))
					.build();
		} catch (Exception e) {
			return writeErrorResponse(e.getMessage(), SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	private Response writeErrorResponse(String message, int statusCode) {
		return Response
				.status(statusCode)
				.header("Content-Type", PropertyFinder.getPropertyValue(ERROR_CONTENT_TYPE))
				.entity(message)
				.build();
	}
	
	private Response writeEmptyResponse(int statusCode) {
		return Response
				.status(statusCode)
				.header("Content-Type", PropertyFinder.getPropertyValue(SUCCESS_CONTENT_TYPE))
				.entity(gson.toJson(new Statistic()))
				.build();
	}
}