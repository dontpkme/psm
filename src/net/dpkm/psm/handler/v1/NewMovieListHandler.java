package net.dpkm.psm.handler.v1;

import java.util.Map;

import net.dpkm.psm.handler.ApiHandler;
import net.dpkm.psm.job.UpdateMovieListJob;

public class NewMovieListHandler extends ApiHandler {

	@Override
	public Object handle(Map<String, String> params) {
		UpdateMovieListJob job = new UpdateMovieListJob();
		return job.getNewMovies();
	}
}
