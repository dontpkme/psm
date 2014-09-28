package net.dpkm.psm.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbUtil {

	private static DbUtil instance = null;

	private final String username;

	private final String password;

	private final String dbUrl;

	public DbUtil() {
		Properties prop = new Properties();
		String propFileName = "db.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		username = prop.getProperty("username");
		password = prop.getProperty("password");
		dbUrl = prop.getProperty("dbUrl");
	}

	public static synchronized DbUtil getInstance() {
		if (instance == null) {
			instance = new DbUtil();
		}
		return instance;
	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager.getConnection(dbUrl, username, password);
			return conn;
		} catch (Exception e) {
			return null;
		}
	}
}
