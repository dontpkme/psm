package net.dpkm.psm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.dpkm.psm.job.UpdateDataJob;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out;
		res.setContentType("text/plain;charset=utf-8");
		out = res.getWriter();
		out.println("FULL SCAN START IN 3 SECONDS!!");
		out.close();

		Timer timer = new Timer();
		UpdateDataJob job = new UpdateDataJob();
		timer.schedule(job, 3000);
	}
}