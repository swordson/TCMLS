package com.cintcm.tcmls.cases;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class AccessTest {
	public static void main(String[] args) {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Properties props = new Properties();
			Properties prop = new Properties();
			// prop.put("charSet", "UTF-8");
			// prop.put("charSet", "UTF-8");

			prop.put("user", "");
			prop.put("password", "");
			prop.put("charSet", "utf-8");
			prop.put("lc_ctype", "utf-8");
			prop.put("encoding", "utf-8");

			// props.put ("charSet", "UTF-8");
			String url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb, *.accdb);DBQ=e:\\cases.accdb";
			// Connection conn = DriverManager.getConnection(url, "", "");
			Connection conn = DriverManager.getConnection(url, props);

			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt
					.executeQuery("select ZHENGHOUMC, ZHIFA from ZHENLIAOQK");

			Set<List<String>> relations = new HashSet<List<String>>();
			while (resultSet.next()) {
				try {
					String method = new String(resultSet.getBytes(2), "gbk");
					String[] methods = method.split("\\$");
					String syndrome = new String(resultSet.getBytes(1), "gbk");
					String[] syndromes = syndrome.split("\\$");
					
					for (String m: methods){
						for (String s : syndromes){
							System.out.println(s + ":" + m);
							relations.add(Arrays.asList(m, "治疗", s, "0"));
							relations.add(Arrays.asList(s, "被治疗", m, "0"));									
						}
					}										
										
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
            MySQLUtils.addRelations(relations, "cases:o");
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}