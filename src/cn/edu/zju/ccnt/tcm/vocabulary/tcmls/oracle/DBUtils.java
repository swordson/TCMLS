package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

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
		String user = "mt";
		String password = "mt";
		Connection con = DriverManager.getConnection(url, user, password);
		return con;

	}
	


}
