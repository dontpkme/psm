package net.dpkm.psm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.dpkm.psm.job.UpdateArticleDataJob;
import net.dpkm.psm.job.UpdateMovieListJob;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		System.out
				.println("=======================Movie List is Updating=======================");
		Timer timer = new Timer();
		UpdateMovieListJob job = new UpdateMovieListJob();
		timer.schedule(job, 0);
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
		UpdateArticleDataJob job = new UpdateArticleDataJob();
		timer.schedule(job, 3000);
	}
}