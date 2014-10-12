package net.dpkm.psm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String relativeUri = httpRequest.getRequestURI().split(
				httpRequest.getContextPath())[1];
		// System.out.println(relativeUri);
		if (!relativeUri.equals("/") && !relativeUri.equals("/init")
				&& relativeUri.split("/").length == 2)
			httpRequest.getRequestDispatcher("/index.html").forward(
					httpRequest, httpResponse);
		filter.doFilter(httpRequest, httpResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}