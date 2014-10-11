package net.dpkm.psm.job;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import net.dpkm.psm.model.Movie;
import net.dpkm.psm.util.FetchHtmlUtil;

public class UpdateMovieListJob extends TimerTask {

	private final String host = "https://tw.movies.yahoo.com/movie_thisweek.html";

	@Override
	public void run() {
		this.getNewMovies();
	}

	private List<Movie> getNewMovies() {
		String url = host;
		String context = FetchHtmlUtil.getHtmlFromUrlString(url);
		String regex = "<div class=\"clearfix row\">";
		String[] splited = context.split(regex);
		List<Movie> movies = new ArrayList<Movie>();

		for (int i = 1; i < splited.length; i++) {
			String movieContext = splited[i];
			String snippets[] = movieContext.split("<img src=\"")[2]
					.split("\"");

			String name, image;

			// fetch image
			image = snippets[0];

			// fetch name
			name = snippets[2];

			System.out.println(name + " : " + image);

			Movie movie = new Movie(name, image);
			movies.add(movie);
			// System.out.println(article);
			// System.out.println("===============================");
		}
		return movies;
	}

	public static void main(String[] args) {
		UpdateMovieListJob job = new UpdateMovieListJob();
		job.run();
	}
}