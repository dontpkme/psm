package net.dpkm.psm.handler.v1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.dpkm.psm.handler.ApiHandler;

public class MovieRankHandler extends ApiHandler {

	@Override
	public Object run(String queryString) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		Random ran = new Random();
		result.put("good", ran.nextInt(51));
		result.put("normal", ran.nextInt(51));
		result.put("bad", ran.nextInt(51));
		return result;
	}
}