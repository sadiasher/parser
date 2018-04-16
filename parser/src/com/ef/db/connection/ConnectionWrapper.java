package com.ef.db.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class ConnectionWrapper {

	private ConnectionWrapper() {}

	private static Connection connection;

	private static final String DATASOURCE_PROPERTIES_FILE="database.properties";
	private static final String DATASOURCE; 
	private static final String USERNAME;
	private static final String PASSWORD;

	static {
		Properties prop = getDatabaseCredentials();
		DATASOURCE = prop.getProperty("datasource.url");
		USERNAME = prop.getProperty("datasource.username");
		PASSWORD = prop.getProperty("datasource.password");
	}

	private  static Properties getDatabaseCredentials() {
		Properties prop = new Properties();
		try {
			InputStream inputStream = ConnectionWrapper.class.getClassLoader().getResourceAsStream(DATASOURCE_PROPERTIES_FILE);
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static Connection getConnection() throws Exception {
		if(connection == null) {
			connection = DriverManager.getConnection(DATASOURCE, USERNAME, PASSWORD);
		}
		return connection;
	}


}
