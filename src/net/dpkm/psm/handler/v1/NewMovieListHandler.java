package net.dpkm.psm.handler.v1;

import java.util.ArrayList;
import java.util.List;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.model.Movie;

public class NewMovieListHandler extends ApiHandler {

	@Override
	public Object run(String queryString) {
		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = new Movie();
		movie.setName("露西");
		movie.setImage("http://aaa.bbb.com/123.jpg");

		Movie movie2 = new Movie();
		movie2.setName("別相信任何人");
		movie2.setImage("http://aaa.bbb.com/456.jpg");

		Movie movie3 = new Movie();
		movie3.setName("安娜貝爾");
		movie3.setImage("http://aaa.bbb.com/789.jpg");

		movies.add(movie);
		movies.add(movie2);
		movies.add(movie3);
		return movies;
	}
}
