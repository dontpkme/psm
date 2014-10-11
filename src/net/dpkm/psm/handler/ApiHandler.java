package net.dpkm.psm.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public abstract class ApiHandler {

	public Object run(String queryString) throws UnsupportedEncodingException {
		String[] paramString = queryString.split("&");
		Map<String, String> params = new HashMap<String, String>();
		for (String param : paramString) {
			params.put(URLDecoder.decode(param.split("=")[0], "UTF-8"),
					URLDecoder.decode(param.split("=")[1], "UTF-8"));
		}
		return handle(params);
	}

	public abstract Object handle(Map<String, String> params);
}
