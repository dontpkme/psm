package net.dpkm.psm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.dpkm.psm.model.Article;
import net.dpkm.psm.repository.ArticleRepository;

import com.google.gson.Gson;

public class ApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter output;

		// 若要輸出中文字，記得要加 charset=Big5
		res.setContentType("application/json;charset=utf-8");
		output = res.getWriter();
		Gson gson = new Gson();
		List<Article> articles = ArticleRepository.getInstance()
				.findArticlesByNameLikeAndWeight("露西", 1f);
		String json = gson.toJson(articles);
		output.println(json);
		output.close();
	}

	public static void main(String[] args) {
		Gson gson = new Gson();
		List<Article> articles = ArticleRepository.getInstance()
				.findArticlesByNameLikeAndWeight("露西", 1f);
		String json = gson.toJson(articles);
		System.out.println(json);
	}
}
