package net.dpkm.psm.repository;

public class Repository {

	protected String antiSQLInjection(String str) {
		try {
			return str.replaceAll("'", "’");
		} catch (Exception e) {
			return "";
		}
	}
}
