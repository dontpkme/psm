package net.dpkm.psm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dpkm.psm.model.MovieDetail;
import net.dpkm.psm.util.DbUtil;

public class MovieDetailRepository extends Repository {

	private static MovieDetailRepository instance = null;

	public static synchronized MovieDetailRepository getInstance() {
		if (instance == null) {
			instance = new MovieDetailRepository();
		}
		return instance;
	}

	public MovieDetail save(MovieDetail movieDetail) {
		String sql = "INSERT INTO `moviedetail` (`cname`, `ename`, `age`, `ondate`, `type`, `time`, `director`, `actor`, `description`, `id`) VALUES ('"
				+ antiSQLInjection(movieDetail.getCname())
				+ "', '"
				+ antiSQLInjection(movieDetail.getEname())
				+ "', '"
				+ antiSQLInjection(movieDetail.getAge())
				+ "', '"
				+ antiSQLInjection(movieDetail.getOndate())
				+ "', '"
				+ antiSQLInjection(movieDetail.getType())
				+ "', '"
				+ antiSQLInjection(movieDetail.getTime())
				+ "', '"
				+ antiSQLInjection(movieDetail.getDirector())
				+ "', '"
				+ antiSQLInjection(movieDetail.getActor())
				+ "', '"
				+ antiSQLInjection(movieDetail.getDescription())
				+ "', "
				+ movieDetail.getId() + ")";
		DbUtil.getInstance().execute(sql);
		return movieDetail;
	}

	public MovieDetail findMovieDetailById(int id) {
		String sql = "select * from `moviedetail` where `id`=" + id;
		ResultSet rs = DbUtil.getInstance().executeQuery(sql);
		MovieDetail result = null;
		try {
			while (rs.next()) {
				result = new MovieDetail();
				result.setCname(rs.getString("cname"));
				result.setEname(rs.getString("ename"));
				result.setAge(rs.getString("age"));
				result.setOndate(rs.getString("ondate"));
				result.setType(rs.getString("type"));
				result.setTime(rs.getString("time"));
				result.setDirector(rs.getString("director"));
				result.setActor(rs.getString("actor"));
				result.setDescription(rs.getString("description"));
				result.setId(rs.getInt("id"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}