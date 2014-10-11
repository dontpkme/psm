package net.dpkm.psm.handler.v1;

import java.util.Map;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.repository.ArticleRepository;

public class MovieRankHandler extends ApiHandler {

	@Override
	public Object handle(Map<String, String> params) {
		String name = params.get("name");
		return ArticleRepository.getInstance().findRanksByNameLike(name);
	}
}