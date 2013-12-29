package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.cintcm.tcmls.Constants;
import com.cintcm.tcmls.MySQLUtils;

public class TransformVerbProps {

	private static String sql = "SELECT distinct verb,property FROM verbs";
	private static String toSql = "insert into verbprops (verb, property) values (?,?)";

	public void loadAndTransform(Connection con) throws SQLException {

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		PreparedStatement pStmt = con.prepareStatement(toSql);

		while (rs.next()) {
			String verbs = rs.getString("verb");
			String prop = rs.getString("property");

			if (verbs != null) {
				for (String verb : verbs.split("\\$")) {
					if ((verb != null) && (verb != "")) {

						pStmt.setString(1, verb);
						pStmt.setString(2, prop);
						pStmt.executeUpdate();

					}
				}
			}

		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection toCon = getConnection();

		TransformVerbProps hierarchy = new TransformVerbProps();

		hierarchy.loadAndTransform(toCon);

	}

	public static Connection getConnection() throws ClassNotFoundException,
			SQLException {

		String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名scutcs

		String url = "jdbc:mysql://localhost:3306/hamster1";

		// MySQL配置时的用户名

		String user = "root";

		// Java连接MySQL配置时的密码

		String password = "yutong";

		// 加载驱动程序

		Class.forName(driver);

		// 连续数据库

		Connection conn = DriverManager.getConnection(url, user, password);

		// if (!conn.isClosed())
		// System.out.println("Succeeded connecting to the Database!");

		return conn;

	}

}
