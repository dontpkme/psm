package net.dpkm.psm.handler.v1;

import java.util.Map;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.repository.MovieDetailRepository;

public class MovieDetailHandler extends ApiHandler {

	@Override
	public Object handle(Map<String, String> params) {
		return MovieDetailRepository.getInstance().findMovieDetailById(
				Integer.parseInt(params.get("id")));
	}
}