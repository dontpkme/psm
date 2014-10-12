package net.dpkm.psm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.dpkm.psm.model.Movie;
import net.dpkm.psm.util.DbUtil;

public class MovieRepository {

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

	public List<Movie> findMovieByType(int type) {
		String sql = "select * from `movie` where `type`=" + type;
		ResultSet rs = DbUtil.getInstance().executeQuery(sql);
		List<Movie> result = new ArrayList<Movie>();
		try {
			while (rs.next()) {
				Movie movie = new Movie();
				movie.setName(rs.getString("name"));
				movie.setImage(rs.getString("image"));
				movie.setUrl(rs.getString("url"));
				movie.setType(rs.getInt("type"));
				result.add(movie);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}