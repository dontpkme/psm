package net.dpkm.psm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dpkm.psm.model.Article;
import net.dpkm.psm.model.MovieDetail;
import net.dpkm.psm.util.DbUtil;

public class ArticleRepository extends Repository {

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
				+ article.getPostYear()
				+ "/"
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

	private List<String> getRelativeAliasName(MovieDetail detail) {
		String name = detail.getCname();
		List<String> alias = new ArrayList<String>();
		alias.add(name);
		// break main title and sub title
		if (name.split("：").length > 1) {
			alias.add(name.split("：")[0]);
			alias.add(name.split("：")[1]);
		}
		int as = alias.size();
		for (int i = 0; i < as; i++) {
			if (alias.get(i).split("─").length > 1) {
				alias.add(name.split("─")[0]);
				alias.add(name.split("─")[1]);
			}
		}
		as = alias.size();
		for (int i = 0; i < as; i++) {
			alias.add(name.split("─")[0]);
		}

		for (int i = 0; i < alias.size(); i++) {
			// replace punctuations to %
			alias.set(i, alias.get(i).replaceAll("，", "%"));
			alias.set(i, alias.get(i).replaceAll("。", "%"));
			alias.set(i, alias.get(i).replaceAll("！", "%"));
			alias.set(i, alias.get(i).replaceAll("前篇", "%"));
			alias.set(i, alias.get(i).replaceAll("後篇", "%"));
			alias.set(i, alias.get(i).replaceAll("上篇", "%"));
			alias.set(i, alias.get(i).replaceAll("下篇", "%"));

			// remove English and number character if necessary
			String oname = alias.get(i);
			String nname = " " + oname + " ";
			nname = nname.replaceAll("[A-Za-z0-9]", "%");
			float l = oname.length();
			float e = nname.split("%").length - 1;
			if (e / l < 0.4)
				alias.set(i, alias.get(i).replaceAll("[A-Za-z0-9]", "%"));

		}
		return alias;
	}

	private String getWhereClause(List<String> alias, String ename,
			String ondate) {
		String where = "";
		int onDateNum = Integer.parseInt(ondate.split("-")[0]
				+ ondate.split("-")[1] + ondate.split("-")[2]);
		onDateNum -= 200;
		if (onDateNum % 10000 > 1300)
			onDateNum -= 8800;

		for (String subname : alias) {
			where += " (`title` like '%" + subname + "%') or ";
		}
		if (!ename.equals("")) {
			if (ename.indexOf(":") > -1) {
				where += " (lower(`title`) like '%"
						+ ename.split(":")[0].toLowerCase().trim()
						+ "%') or (lower(`title`) like '%"
						+ ename.split(":")[1].toLowerCase().trim() + "%')";
			} else
				where += " (lower(`title`) like '%"
						+ ename.toLowerCase().trim() + "%')";
		} else
			where += " (1=0)";
		where = " (" + where + ") ";
		where = "("
				+ where
				+ " and length(`date`)=10 and convert(concat(substr(`date`, 1, 4),substr(`date`, 6, 2),substr(`date`, 9, 2)),UNSIGNED INTEGER) >= "
				+ onDateNum + ")";
		return where;
	}

	public Map<String, Float> findRanksByMovieDetail(MovieDetail detail) {
		String where = getWhereClause(getRelativeAliasName(detail),
				detail.getEname(), detail.getOndate());
		String sql = "select (select sum(`weight`) from `article` where "
				+ where
				+ " and `weight` > 0) as `good`, (select count(`weight`) from `article` where "
				+ where
				+ " and `weight` = 0) as `normal`, (select sum(`weight`) from `article` where "
				+ where + " and `weight` < 0) as `bad`";
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

	public List<Article> findArticlesByMovieDetail(MovieDetail detail) {
		String where = getWhereClause(getRelativeAliasName(detail),
				detail.getEname(), detail.getOndate());
		String sql = "select * from `article` where " + where;

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