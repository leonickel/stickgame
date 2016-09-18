package com.leonickel.stickgame.web;

import static com.leonickel.stickgame.util.DefaultProperties.ERROR_CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.leonickel.stickgame.model.Statistic;
import com.leonickel.stickgame.service.StatisticService;
import com.leonickel.stickgame.util.PropertyFinder;

@Path("/service/statistics")
public class StatisticServiceRest {

	@Inject
	private StatisticService statisticService;
	
	private final Gson gson = new Gson();
	
	@GET
	@Path("/{userId}/")
	public Response listByUser(@PathParam("userId") String userId) {
		try {
			System.out.println("GET chegou");
			return Response.status(SC_OK)
					.entity(gson.toJson(statisticService.getStatisticByUser(userId)))
					.header("Content-Type", "application/json")
					.build();
		} catch (Exception e) { //TODO write custom exceptions
			return writeErrorResponse(e.getMessage(), SC_NOT_FOUND);
		}
	}
	
	@PUT
	@Path("/{userId}/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStatisticByUser(@PathParam("userId") String userId, String statisticJSON) {
		try {
			System.out.println("PUT chegou");
			Statistic statistic = statisticService.updateStatisticByUser(userId, gson.fromJson(statisticJSON, Statistic.class));
			return Response.status(SC_OK)
					.entity(gson.toJson(statistic))
					.header("Content-Type", "application/json")
					.build();
		} catch (Exception e) { //TODO write custom exceptions
			return writeErrorResponse(e.getMessage(), SC_NOT_FOUND);
		}
	}
	
//	private Response writeImageResponse(VideoDetailDTO videoDetails, String dimension) throws Exception {
//		return Response
//				.status(SC_OK)
//				.header("Content-Type", PropertyFinder.getPropertyValue(IMAGE_CONTENT_TYPE))
//				.entity(responseService.getImageResponse(videoDetails, dimension))
//				.build();
//	}
//	
//	private Response writeHtmlResponse(VideoDetailDTO videoDetails) throws Exception {
//		return Response
//				.status(SC_OK)
//				.header("Content-Type", PropertyFinder.getPropertyValue(HTML_CONTENT_TYPE))
//				.entity(responseService.getHtmlResponse(videoDetails))
//				.build();
//	}
//	
	private Response writeErrorResponse(String message, int statusCode) {
		return Response
				.status(statusCode)
				.header("Content-Type", PropertyFinder.getPropertyValue(ERROR_CONTENT_TYPE))
				.entity(message)
				.build();
	}
}