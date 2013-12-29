package com.cintcm.classics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBUtils {

	INSTANCE;

	public static Connection getDBConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		System.out
				.println("The oracle driver registration process is successful!\n");

		String url = "jdbc:oracle:thin:@192.168.137.254:1521:orcl";
		String user = "classics";
		String password = "classics";
		Connection con = DriverManager.getConnection(url, user, password);
		return con;

	}
	


}
