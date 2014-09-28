package net.dpkm.psm.handler.v1;

import java.util.ArrayList;
import java.util.List;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.model.Article;

public class MovieCommentHandler extends ApiHandler {

	@Override
	public Object run(String queryString) {
		List<Article> result = new ArrayList<Article>();
		return result;
	}
}
