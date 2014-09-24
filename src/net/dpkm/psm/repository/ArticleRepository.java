package net.dpkm.psm.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.dpkm.psm.model.Article;
import net.dpkm.psm.util.DbUtil;

public class ArticleRepository {

	private static ArticleRepository instance = null;

	public static synchronized ArticleRepository getInstance() {
		if (instance == null) {
			instance = new ArticleRepository();
		}
		return instance;
	}

	public Article save(Article article) {
		try {
			Connection conn = DbUtil.getInstance().getConnection();
			String sql = "INSERT INTO `article` (`title`, `author`, `date`, `marked`, `link`, `nrec`, `weight`) VALUES('"
					+ article.getTitle()
					+ "', '"
					+ article.getAuthor()
					+ "', '"
					+ article.getDate()
					+ "', "
					+ (article.isMarked() ? "1" : "0")
					+ ", '"
					+ article.getLink()
					+ "', '"
					+ article.getNrec()
					+ "', "
					+ (article.getWeight() != null ? article.getWeight()
							: "null") + ")";
			System.out.println(sql);
			conn.createStatement().execute(sql);
			conn.close();
			return article;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Article> findArticlesByNameLikeAndWeight(String name,
			Float weight) {
		try {
			Connection conn = DbUtil.getInstance().getConnection();

			String sql = "select * from article where `title` like '%" + name
					+ "%' and `weight` "
					+ (weight == null ? "is null" : "=" + weight);

			System.out.println(sql);
			ResultSet rs = conn.createStatement().executeQuery(sql);
			List<Article> result = new ArrayList<Article>();
			while (rs.next()) {
				Article article = new Article(rs.getString("title"),
						rs.getString("author"), rs.getString("date"),
						rs.getBoolean("marked"), rs.getString("nrec"),
						rs.getString("link"));
				result.add(article);
			}
			conn.close();
			return result;
		} catch (Exception e) {
			return new ArrayList<Article>();
		}
	}
}
