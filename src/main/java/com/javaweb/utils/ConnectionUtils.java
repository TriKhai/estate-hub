package com.javaweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {
	public static Connection getConnection() throws SQLException {
		String url =  System.getenv("DB_URL");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");
		
		if (url == null || url.trim().isEmpty()) {
			throw new IllegalStateException("Thieu bien moi truong DB_URL");
		}

		if (user == null || user.trim().isEmpty()) {
			throw new IllegalStateException("Thieu bien moi truong DB_USER");
		}

		if (password == null) {
			throw new IllegalStateException("Thieu bien moi truong DB_PASSWORD");
		}
		
		return DriverManager.getConnection(url, user, password);
	}
}
