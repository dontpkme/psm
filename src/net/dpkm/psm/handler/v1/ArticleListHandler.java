package net.dpkm.psm.handler.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.model.Article;
import net.dpkm.psm.model.MovieDetail;
import net.dpkm.psm.repository.ArticleRepository;
import net.dpkm.psm.repository.MovieDetailRepository;

public class ArticleListHandler extends ApiHandler {

	@Override
	public Object handle(Map<String, String> params) {
		int id = Integer.parseInt(params.get("id"));
		MovieDetail detail = MovieDetailRepository.getInstance()
				.findMovieDetailById(id);
		List<Article> articles = ArticleRepository.getInstance()
				.findArticlesByMovieDetail(detail);

		Map<String, List<Article>> result = new HashMap<String, List<Article>>();
		List<Article> goodArticles = new ArrayList<Article>();
		List<Article> normalArticles = new ArrayList<Article>();
		List<Article> badArticles = new ArrayList<Article>();
		List<Article> otherArticles = new ArrayList<Article>();

		for (Article article : articles) {
			if (article.getWeight() == null)
				otherArticles.add(article);
			else if (article.getWeight() > 0)
				goodArticles.add(article);
			else if (article.getWeight() < 0)
				badArticles.add(article);
			else if (article.getWeight() == 0)
				normalArticles.add(article);

		}
		result.put("good", goodArticles);
		result.put("normal", normalArticles);
		result.put("bad", badArticles);
		result.put("other", otherArticles);

		return result;
	}
}
