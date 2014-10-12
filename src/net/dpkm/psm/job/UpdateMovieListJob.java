package net.dpkm.psm.job;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import net.dpkm.psm.model.Movie;
import net.dpkm.psm.repository.MovieRepository;
import net.dpkm.psm.util.FetchHtmlUtil;

public class UpdateMovieListJob extends TimerTask {

	private final String newMovieUrl = "https://tw.movies.yahoo.com/movie_thisweek.html";

	@Override
	public void run() {
		MovieRepository.getInstance().emptyTable();
		List<Movie> newMovies = this.fetchNewMovies();
		for (Movie movie : newMovies) {
			MovieRepository.getInstance().save(movie);
		}
	}

	private List<Movie> fetchNewMovies() {
		String context = FetchHtmlUtil.getHtmlFromUrlString(newMovieUrl);
		String regex = "<div class=\"clearfix row\">";
		String[] splited = context.split(regex);
		List<Movie> movies = new ArrayList<Movie>();

		for (int i = 1; i < splited.length; i++) {
			String movieContext = splited[i];
			String snippets[] = movieContext.split("\"");

			String name, image, url;

			// fetch image
			image = snippets[33];

			// fetch name
			name = snippets[35];

			// fetch url
			url = snippets[31];

			Movie movie = new Movie();
			movie.setName(name);
			movie.setImage(image);
			movie.setType(1);
			movie.setUrl(url);
			movies.add(movie);
		}
		return movies;
	}

	public static void main(String[] args) {
		UpdateMovieListJob job = new UpdateMovieListJob();
		job.run();
	}
}