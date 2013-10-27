package com.cintcm.tcmls.cases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import com.cintcm.tcmls.Constants;


public class MySQLUtils {
	public static Connection conn = null;
	private static String nameToId = "select id from def where name = ?";
	private static String addNameSql = "insert into def (name) values (?)";
	private static String toGraphSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	
	public static Connection getConnection() throws ClassNotFoundException,
			SQLException {

		if (conn == null) {

			String driver = "com.mysql.jdbc.Driver";

			// URL指向要访问的数据库名scutcs

			String url = "jdbc:mysql://localhost:3306/cases";

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
	
	public static void addRelations(Set<List<String>> relations, String prefix){
		try {
			Connection con = getConnection();
			PreparedStatement gStmt = con.prepareStatement(toGraphSql);

			for(List<String> relation : relations){	
				
				int s = getId(con, relation.get(0));
				int o = getId(con, relation.get(2));
				if ((s != -1) && (o != -1)){
					gStmt.setString(1, prefix + s);			
					gStmt.setString(2, relation.get(1));
					gStmt.setString(3, prefix + o);
					gStmt.setString(4, relation.get(3));					
					gStmt.executeUpdate();		
				}				
						
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static int getId(Connection con, String name){
		if (selectId(con, name) == -1) addName(con, name);
		return selectId(con, name);
	}
	
	
	
	private static void addName(Connection con, String name) {
		if ((name == null) || (name.equals(""))) return; 
		try {
			PreparedStatement gStmt = con.prepareStatement(addNameSql);
			gStmt.setString(1, name);
			gStmt.executeUpdate();			
			gStmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private static int selectId(Connection con, String name){
		int id = -1;
		try {
			PreparedStatement gStmt = con.prepareStatement(nameToId);
			gStmt.setString(1, name);
			ResultSet rs = gStmt.executeQuery();
			if (rs.next()) id = rs.getInt(1);
			gStmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if (id != -1) System.out.println(id);
		return id;
	}
	
	
	
}
