package net.dpkm.psm.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.dpkm.psm.model.Movie;
import net.dpkm.psm.model.MovieDetail;
import net.dpkm.psm.repository.MovieDetailRepository;
import net.dpkm.psm.repository.MovieRepository;
import net.dpkm.psm.util.FetchHtmlUtil;

public class UpdateMovieListJob extends TimerTask {

	private final String newMovieUrl = "https://tw.movies.yahoo.com/movie_thisweek.html";

	private final String hotMovieUrl = "https://tw.movies.yahoo.com/chart.html?cate=taipei";

	@Override
	public void run() {
		MovieRepository.getInstance().emptyTable();
		// List<Movie> newMovies = this.fetchNewMovies();
		// for (Movie movie : newMovies) {
		// MovieRepository.getInstance().save(movie);
		// fetchMovieDetail(movie);
		// }

		List<Movie> hotMovies = this.fetchHotMovies();
		for (Movie movie : hotMovies) {
			MovieRepository.getInstance().save(movie);
			fetchMovieDetail(movie);
		}
	}

	private void fetchMovieDetail(Movie movie) {
		int id = Integer.parseInt(movie.getUrl().split("id=")[1]);
		MovieDetail movieDetail = MovieDetailRepository.getInstance()
				.findMovieDetailById(id);
		if (movieDetail == null) {
			Timer timer = new Timer();
			InitMovieDetailJob job = new InitMovieDetailJob();
			job.setId(id);
			timer.schedule(job, 0);
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
			image = snippets[21];

			// fetch name
			name = snippets[23];

			// fetch url
			url = snippets[19].split("\\*")[1];

			Movie movie = new Movie();
			movie.setName(name);
			movie.setImage(image);
			movie.setType(1);
			movie.setUrl(url);
			movies.add(movie);
		}
		return movies;
	}

	private List<Movie> fetchHotMovies() {
		String context = FetchHtmlUtil.getHtmlFromUrlString(hotMovieUrl);
		String regex = "<td class=\"c1\">";
		String[] splited = context.split(regex);
		List<Movie> movies = new ArrayList<Movie>();

		for (int i = 1; i < splited.length; i++) {
			try {
				String movieContext = splited[i];
				String snippets[] = movieContext.split("<a href=\"");

				String name, image, url;

				// fetch url
				url = snippets[1].split("\"")[0].split("\\*")[1];

				// fetch name
				if (i == 1)
					name = snippets[2].split(">")[1].split("<")[0];
				else
					name = snippets[1].split(">")[1].split("<")[0];

				// fetch image
				image = fetchImageByUrl(url);

				Movie movie = new Movie();
				movie.setName(name);
				movie.setImage(image);
				movie.setType(2);
				movie.setUrl(url);
				movies.add(movie);
			} catch (Exception e) {
				System.out.println("movie data extraction failed.");
			}
		}
		return movies;
	}

	private String fetchImageByUrl(String url) {
		String id = url.split("id=")[1];
		String id1 = id.substring(0, 2);
		String id2 = id.substring(2, 4);
		String image = "https://s.yimg.com/fp/mpost2/" + id1 + "/" + id2 + "/"
				+ id1 + id2 + ".jpg";
		return image;
	}

	public static void main(String[] args) {
		UpdateMovieListJob job = new UpdateMovieListJob();
		job.run();
	}
}