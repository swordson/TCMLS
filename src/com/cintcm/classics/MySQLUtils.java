package com.cintcm.classics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class MySQLUtils {
	public static Connection conn = null;

	public static Connection getConnection() throws ClassNotFoundException,
			SQLException {

		if (conn == null) {

			String driver = "com.mysql.jdbc.Driver";

			// URL指向要访问的数据库名scutcs

			String url = "jdbc:mysql://localhost:3306/clan";

			// MySQL配置时的用户名

			String user = "root";

			// Java连接MySQL配置时的密码

			String password = "yutong";

			// 加载驱动程序

			Class.forName(driver);

			// 连续数据库

			conn = DriverManager.getConnection(url, user, password);

			//if (!conn.isClosed()) System.out.println("Succeeded connecting to the Database!");

		}

		return conn;

	}
	
	
}
