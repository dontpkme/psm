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
		repository.save(movieDetail);
	}

	public void setId(int id) {
		this.id = id;
	}

	private String stripOutHtmlTag(String str) {
		while (str.indexOf("<") != -1) {
			String prefix = str.substring(0, str.indexOf("<"));
			String postfix = str.substring(str.indexOf(">") + 1);
			str = prefix + postfix;
		}
		return str;
	}

	private MovieDetail fetchMovieDetail(int id) {
		String content = FetchHtmlUtil.getHtmlFromUrlString(host + id);
		String regex = "<div class=\"item clearfix\">";
		String splited[] = content.split(regex);

		MovieDetail movieDetail = new MovieDetail();
		String detail = splited[1];
		movieDetail.setCname(detail.split("<h4>")[1].split("<")[0]);
		movieDetail.setEname(detail.split("<h5>")[1].split("<")[0]);
		String[] snippets = detail.split("<span class=\"dta\">");
		if (splited[1].indexOf("icon_gate_1.gif") != -1)
			movieDetail.setAge("1");
		else if (splited[1].indexOf("icon_gate_2.gif") != -1)
			movieDetail.setAge("2");
		else if (splited[1].indexOf("icon_gate_3.gif") != -1)
			movieDetail.setAge("3");
		else if (splited[1].indexOf("icon_gate_4.gif") != -1)
			movieDetail.setAge("4");
		else
			movieDetail.setAge("");

		movieDetail.setOndate(snippets[1].split("</span>")[0]);
		movieDetail.setType(stripOutHtmlTag(snippets[2].split("</span>")[0]));
		movieDetail.setTime(snippets[3].split("</span>")[0]);
		movieDetail
				.setDirector(stripOutHtmlTag(snippets[4].split("</span>")[0]));
		movieDetail.setActor(stripOutHtmlTag(snippets[5].split("</span>")[0]));

		movieDetail.setDescription(stripOutHtmlTag(splited[3].split("<p>")[1]
				.split("</p>")[0]));
		movieDetail.setId(id);
		return movieDetail;
	}

	public static void main(String[] args) {
		InitMovieDetailJob job = new InitMovieDetailJob();
		job.setId(5319);
		job.run();
	}
}