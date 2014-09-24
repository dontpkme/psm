package net.dpkm.psm.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {

	private static DbUtil instance = null;

	private final String username = "root";

	private final String password = "1234";

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
			conn = DriverManager
					.getConnection(
							"jdbc:mysql://localhost:3306/pms?useUnicode=true&characterEncoding=UTF-8",
							username, password);
			return conn;
		} catch (Exception e) {
			return null;
		}
	}
}
