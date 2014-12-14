package net.dpkm.psm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dpkm.psm.model.Movie;
import net.dpkm.psm.util.DbUtil;

public class MovieRepository extends Repository {

	private static MovieRepository instance = null;

	public static synchronized MovieRepository getInstance() {
		if (instance == null) {
			instance = new MovieRepository();
		}
		return instance;
	}

	public void emptyTable() {
		String sql = "delete from `movie`";
		DbUtil.getInstance().execute(sql);
	}

	public Movie save(Movie movie) {
		String sql = "INSERT INTO `movie` (`name`, `image`, `type`, `url`) VALUES ('"
				+ movie.getName()
				+ "', '"
				+ movie.getImage()
				+ "', '"
				+ movie.getType() + "', '" + movie.getUrl() + "')";
		DbUtil.getInstance().execute(sql);
		return movie;
	}

	public List<Map<String, Object>> findMovieDistinctById() {
		String sql = "select distinct `moviedetail`.`id`,`moviedetail`.`cname`,`moviedetail`.`ename`,`movie`.`image`,`movie`.`url` from `movie`, `moviedetail` where SUBSTR(`movie`.`url`, -4) = `moviedetail`.`id`";
		ResultSet rs = DbUtil.getInstance().executeQuery(sql);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			while (rs.next()) {
				Map<String, Object> movie = new HashMap<String, Object>();
				movie.put("id", rs.getInt("id"));
				movie.put("cname", rs.getString("cname"));
				movie.put("ename", rs.getString("ename"));
				movie.put("image", rs.getString("image"));
				movie.put("url", rs.getString("url"));
				result.add(movie);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Map<String, Object>> findMovieByType(int type) {
		String sql = "select `moviedetail`.`id`,`moviedetail`.`cname`,`moviedetail`.`ename`,`movie`.`image`,`movie`.`url` from `movie`, `moviedetail` where SUBSTR(`movie`.`url`, -4) = `moviedetail`.`id` and `movie`.`type`="
				+ type;
		ResultSet rs = DbUtil.getInstance().executeQuery(sql);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			while (rs.next()) {
				Map<String, Object> movie = new HashMap<String, Object>();
				movie.put("id", rs.getInt("id"));
				movie.put("cname", rs.getString("cname"));
				movie.put("ename", rs.getString("ename"));
				movie.put("image", rs.getString("image"));
				movie.put("url", rs.getString("url"));
				result.add(movie);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}