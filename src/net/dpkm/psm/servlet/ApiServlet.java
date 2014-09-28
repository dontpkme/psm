package net.dpkm.psm.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.dpkm.psm.util.ApiUtil;

import com.google.gson.Gson;

public class ApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out;
		res.setContentType("application/json;charset=utf-8");
		out = res.getWriter();

		Gson gson = new Gson();
		String pattern = req.getRequestURI().split(req.getContextPath())[1];
		String result = gson
				.toJson(ApiUtil.getHandler(req.getMethod(), pattern).run(
						req.getQueryString()));
		out.println(result);
		out.close();
	}
}