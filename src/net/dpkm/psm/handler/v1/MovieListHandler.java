package net.dpkm.psm.handler.v1;

import java.util.Map;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.repository.MovieRepository;

public class MovieListHandler extends ApiHandler {

	@Override
	public Object handle(Map<String, String> params) {
		return MovieRepository.getInstance().findMovieDistinctById();
	}
}