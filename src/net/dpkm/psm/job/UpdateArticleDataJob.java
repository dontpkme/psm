package net.dpkm.psm.job;

import java.util.List;

import net.dpkm.psm.model.Article;
import net.dpkm.psm.repository.ArticleRepository;

public class UpdateArticleDataJob extends InitArticleDataJob {
	
	private int latestTimestamp = 0;

	@Override
	public void run() {
		System.out.println("do articles update.");
		ArticleRepository articleRepository = ArticleRepository.getInstance();
		Article latestArticle = articleRepository.findTop1ArticleOrderByLinkDesc();
		latestTimestamp = fetchTimestampFromLink(latestArticle.getLink());
		doUpdate();
	}
	
	private void doUpdate() {
		int nowPage = this.getLatestPage();

		while (nowPage > 0) {
			System.out.println("we are now at page " + nowPage);
			List<Article> articles = getArticles(nowPage);

			for (Article article : articles) {
				if(fetchTimestampFromLink(article.getLink()) > latestTimestamp) {
					System.out.println("found new article: " + article.getTitle());
					ArticleRepository.getInstance().save(article);
				} else {
					nowPage = -1;
				}
			}
			nowPage--;
		}
		System.out.println("update end.");
	}
 
	private int fetchTimestampFromLink(String link) {
		return Integer.parseInt(link.substring(31, 41));
	}
	
	public static void main(String[] args) {
		UpdateArticleDataJob job = new UpdateArticleDataJob();
		job.run();
	}
}