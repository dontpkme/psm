package net.dpkm.psm.job;

import java.util.TimerTask;

import net.dpkm.psm.model.MovieDetail;
import net.dpkm.psm.repository.MovieDetailRepository;
import net.dpkm.psm.util.FetchHtmlUtil;

public class InitMovieDetailJob extends TimerTask {

	protected final String host = "https://tw.movies.yahoo.com/movieinfo_main.html/id=";

	private int id;

	@Override
	public void run() {
		System.out.println("new movie found! fetch movie detail infomation");
		MovieDetail movieDetail = fetchMovieDetail(id);
		MovieDetailRepository repository = MovieDetailRepository.getInstance();
		// repository.save(movieDetail);
	}

	public void setId(int id) {
		this.id = id;
	}

	private MovieDetail fetchMovieDetail(int id) {
		String content = FetchHtmlUtil.getHtmlFromUrlString(host + id);
		System.out.println(content);
		return null;
	}
}