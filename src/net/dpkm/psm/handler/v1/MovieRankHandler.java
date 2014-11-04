package net.dpkm.psm.handler.v1;

import java.util.Map;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.model.MovieDetail;
import net.dpkm.psm.repository.ArticleRepository;
import net.dpkm.psm.repository.MovieDetailRepository;

public class MovieRankHandler extends ApiHandler {

	@Override
	public Object handle(Map<String, String> params) {
		int id = Integer.parseInt(params.get("id"));
		MovieDetail detail = MovieDetailRepository.getInstance()
				.findMovieDetailById(id);
		return ArticleRepository.getInstance().findRanksByMovieDetail(detail);
	}
}