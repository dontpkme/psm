package net.dpkm.psm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.dpkm.psm.job.InitArticleDataJob;
import net.dpkm.psm.job.UpdateMovieListJob;
import net.dpkm.psm.job.UpdateArticleDataJob;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		System.out.println("==================Movie List is Updating==================");
		Timer timer = new Timer();
		UpdateMovieListJob umlJob = new UpdateMovieListJob();
		timer.schedule(umlJob, 0);
		
		UpdateArticleDataJob uadJob = new UpdateArticleDataJob();
		timer.scheduleAtFixedRate(uadJob, 0, 1000 * 60 * 10);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out;
		res.setContentType("text/html;charset=utf-8");
		out = res.getWriter();
		out.flush();
		out.println("<script> location.replace('./index.html'); </script>");
		out.close();

		Timer timer = new Timer();
		InitArticleDataJob job = new InitArticleDataJob();
		timer.schedule(job, 3000);
	}
}