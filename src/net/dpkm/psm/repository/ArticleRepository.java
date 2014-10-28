package net.dpkm.psm.repository;

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
		DbUtil.getInstance().execute(sql);
		return article;
	}

	private List<String> getRelativeAliasName(String name) {
		List<String> alias = new ArrayList<String>();
		alias.add(name);
		// break main title and sub title
		if (name.split("：").length > 1) {
			alias.add(name.split("：")[0]);
			alias.add(name.split("：")[1]);
		}

		// replace punctuations to %
		for (int i = 0; i < alias.size(); i++) {
			alias.set(i, alias.get(i).replaceAll("，", "%"));
			alias.set(i, alias.get(i).replaceAll("。", "%"));
			alias.set(i, alias.get(i).replaceAll("！", "%"));
		}
		return alias;
	}

	public Map<String, Float> findRanksByNameLike(String name) {
		List<String> alias = getRelativeAliasName(name);
		String like = "";
		for (String subname : alias) {
			if (!like.equals(""))
				like += " or ";
			like += " (`title` like '%" + subname + "%') ";
		}
		like = " (" + like + ") ";
		String sql = "select (select sum(`weight`) from `article` where "
				+ like
				+ " and `weight` > 0) as `good`, (select count(`weight`) from `article` where "
				+ like
				+ " and `weight` = 0) as `normal`, (select count(`weight`) from `article` where "
				+ like + " and `weight` < 0) as `bad`";
		ResultSet rs = DbUtil.getInstance().executeQuery(sql);
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
		String sql = "select * from `article` where `title` like '%" + name
				+ "%' and `weight` "
				+ (weight == null ? "is null" : "=" + weight);

		ResultSet rs = DbUtil.getInstance().executeQuery(sql);
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

	public Article findTop1ArticleOrderByLinkDesc() {
		String sql = "select * from `article` order by `link` desc limit 0, 1";
		ResultSet rs = DbUtil.getInstance().executeQuery(sql);
		Article result = null;
		try {
			while (rs.next()) {
				result = new Article(rs.getString("title"),
						rs.getString("author"), rs.getString("date"),
						rs.getBoolean("marked"), rs.getString("nrec"),
						rs.getString("link"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}