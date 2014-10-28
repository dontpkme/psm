package net.dpkm.psm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dpkm.psm.model.MovieDetail;
import net.dpkm.psm.util.DbUtil;

public class MovieDetailRepository {

	private static MovieDetailRepository instance = null;

	public static synchronized MovieDetailRepository getInstance() {
		if (instance == null) {
			instance = new MovieDetailRepository();
		}
		return instance;
	}

	public MovieDetail save(MovieDetail movieDetail) {
		String sql = "INSERT INTO `moviedetail` (`cname`, `ename`, `age`, `ondate`, `type`, `time`, `director`, `actor`, `description`, `id`) VALUES ('"
				+ movieDetail.getCname()
				+ "', '"
				+ movieDetail.getEname()
				+ "', '"
				+ movieDetail.getAge()
				+ "', '"
				+ movieDetail.getOndate()
				+ "', '"
				+ movieDetail.getType()
				+ "', '"
				+ movieDetail.getTime()
				+ "', '"
				+ movieDetail.getDirector()
				+ "', '"
				+ movieDetail.getActor()
				+ "', '"
				+ movieDetail.getDescription()
				+ "', "
				+ movieDetail.getId() + ")";
		DbUtil.getInstance().execute(sql);
		return movieDetail;
	}

	public MovieDetail findMovieById(int id) {
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