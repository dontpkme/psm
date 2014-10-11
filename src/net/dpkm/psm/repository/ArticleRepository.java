package net.dpkm.psm.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private void execute(String sql) {
		System.out.println(sql);
		Connection conn = DbUtil.getInstance().getConnection();
		try {
			conn.createStatement().execute(sql);
			conn.close();
		} catch (SQLException e) {
			System.out.println("something wrong when execute sql");
			throw new RuntimeException(e);
		}
	}

	private ResultSet executeQuery(String sql) {
		System.out.println(sql);
		Connection conn = DbUtil.getInstance().getConnection();
		try {
			ResultSet rs = conn.createStatement().executeQuery(sql);
			conn.close();
			return rs;
		} catch (SQLException e) {
			System.out.println("something wrong when execute sql");
			throw new RuntimeException(e);
		}
	}

	public Article save(Article article) {
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
				+ (article.getWeight() != null ? article.getWeight() : "null")
				+ ")";
		this.execute(sql);
		return article;
	}

	public Map<String, Float> findRanksByNameLike(String name) {
		String sql = "select (select sum(`weight`) from `article` where `title` like '%"
				+ name
				+ "%' and `weight` > 0) as `good`, (select count(`weight`) from `article` where `title` like '%"
				+ name
				+ "%' and `weight` = 0) as `normal`, (select count(`weight`) from `article` where `title` like '%"
				+ name + "%' and `weight` < 0) as `bad`";
		ResultSet rs = executeQuery(sql);
		Map<String, Float> result = new HashMap<String, Float>();
		try {
			while (rs.next()) {
				result.put("good", rs.getFloat("good"));
				result.put("normal", rs.getFloat("normal"));
				result.put("bad", rs.getFloat("bad"));
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Article> findArticlesByNameLikeAndWeight(String name,
			Float weight) {
		String sql = "select * from article where `title` like '%" + name
				+ "%' and `weight` "
				+ (weight == null ? "is null" : "=" + weight);

		ResultSet rs = executeQuery(sql);
		List<Article> result = new ArrayList<Article>();
		try {
			while (rs.next()) {
				Article article = new Article(rs.getString("title"),
						rs.getString("author"), rs.getString("date"),
						rs.getBoolean("marked"), rs.getString("nrec"),
						rs.getString("link"));
				result.add(article);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}
