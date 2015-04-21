package net.dpkm.psm.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import net.dpkm.psm.model.Article;
import net.dpkm.psm.repository.ArticleRepository;
import net.dpkm.psm.util.FetchHtmlUtil;

public class InitArticleDataJob extends TimerTask {

	protected final String host = "https://www.ptt.cc";

	@Override
	public void run() {
		System.out.println("do articles initial.");
		run(1, -1);
	}

	private void run(int min, int max) {
		int latestPage = this.getLatestPage();

		if (max == -1)
			max = latestPage;
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}

		if (max > latestPage)
			max = latestPage;
		if (min < 1)
			min = 1;

		String postYear = String.valueOf(new Date().getYear() + 1900);
		String lastPostDate = "";
		int changeYearPage = latestPage;
		for (int i = max; i >= min; i--) {
			System.out.println("we are now at page " + i);
			List<Article> articles = getArticles(i);

			for (Article article : articles) {
				if (lastPostDate.equals("01/01")
						&& article.getDate().equals("12/31")
						&& changeYearPage - i > 2) {
					postYear = String.valueOf(Integer.parseInt(postYear) - 1);
					changeYearPage = i;
				}
				article.setPostYear(postYear);
				ArticleRepository.getInstance().save(article);
				lastPostDate = article.getDate();
			}
		}
	}

	protected List<Article> getArticles(int page) {
		String url = host + "/bbs/movie/index" + page + ".html";
		String context = FetchHtmlUtil.getHtmlFromUrlString(url);
		String regex = "<div class=\"r-ent\">";
		String[] splited = context.split(regex);
		List<Article> articles = new ArrayList<Article>();

		for (int i = 1; i < splited.length; i++) {
			String articleContext = splited[i];
			String tags[] = articleContext.split("</div>");

			String title, author, date, nrec, link;
			boolean marked;

			// fetch articleId

			// fetch nrec
			nrec = tags[0].substring(tags[0].indexOf(">") + 1);
			if (!nrec.equals("")) {
				nrec = nrec.substring(nrec.indexOf(">") + 1);
				nrec = nrec.substring(0, nrec.indexOf("<"));
			}

			// fetch mark
			marked = tags[1].substring(22).equals("") ? false : true;

			// fetch title and link
			title = tags[2].substring(23);
			if (title.indexOf("<a href") != -1) {
				link = title.substring(title.indexOf("\"") + 1);
				link = host + link.substring(0, link.indexOf("\""));
				title = title.substring(title.indexOf(">") + 1);
				title = title.substring(0, title.indexOf("<"));
			} else {
				title = "";
				link = "";
			}

			// fetch date
			date = tags[3].substring(tags[3].indexOf("date") + 6);
			date = date.replace(" ", "0");

			// fetch author
			author = tags[4].substring(tags[4].indexOf(">") + 1);

			Article article = new Article(title, author, date, marked, nrec,
					link);
			articles.add(article);
			// System.out.println(article);
			// System.out.println("===============================");
		}
		return articles;
	}

	protected int getLatestPage() {
		String url = host + "/bbs/movie/index.html";
		String context = FetchHtmlUtil.getHtmlFromUrlString(url);
		String regex = ".html\">&lsaquo; 上頁</a>";
		String[] splited = context.split(regex);
		String page = splited[0].substring(splited[0].lastIndexOf("/") + 6);
		return Integer.parseInt(page) + 1;
	}
}