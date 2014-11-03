package net.dpkm.psm.util;

import net.dpkm.psm.enums.ApiName;
import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.handler.v1.MovieCommentHandler;
import net.dpkm.psm.handler.v1.MovieDetailHandler;
import net.dpkm.psm.handler.v1.MovieListHandler;
import net.dpkm.psm.handler.v1.MovieRankHandler;

public class ApiUtil {

	public static ApiHandler getHandler(String method, String pattern) {
		String[] terms = pattern.split("/");

		if (method.equals("GET"))
			return doGetApi(ApiName.valueOf(terms[3]));
		else if (method.equals("POST"))
			return doPostApi(ApiName.valueOf(terms[3]));
		else
			return null;
	}

	private static ApiHandler doGetApi(ApiName name) {
		switch (name) {
		case movie_list:
			return new MovieListHandler();
		case movie_rank:
			return new MovieRankHandler();
		case movie_comment:
			return new MovieCommentHandler();
		case movie_detail:
			return new MovieDetailHandler();
		default:
			return null;
		}
	}

	private static ApiHandler doPostApi(ApiName name) {
		return null;
	}
}