package net.dpkm.psm.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {

	private static DbUtil instance = null;

	private final String username;

	private final String password;

	private final String dbUrl;

	private final Boolean showSql;

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
		showSql = Boolean.parseBoolean(prop.getProperty("showSql"));
	}

	public static synchronized DbUtil getInstance() {
		if (instance == null) {
			instance = new DbUtil();
		}
		return instance;
	}

	public void execute(String sql) {
		if (showSql)
			System.out.println(sql);
		Connection conn = DbUtil.getInstance().getConnection();
		try {
			conn.createStatement().execute(sql);
			conn.close();
		} catch (SQLException e) {
			System.out.println("something wrong when execute sql");
			throw new RuntimeException(e);
		}
	}

	public ResultSet executeQuery(String sql) {
		if (showSql)
			System.out.println(sql);
		Connection conn = DbUtil.getInstance().getConnection();
		try {
			ResultSet rs = conn.createStatement().executeQuery(sql);
			conn.close();
			return rs;
		} catch (SQLException e) {
			System.out.println("something wrong when execute sql");
			throw new RuntimeException(e);
		}
	}

	private Connection getConnection() {
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
