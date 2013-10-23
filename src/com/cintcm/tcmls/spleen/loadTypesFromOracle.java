package com.cintcm.tcmls.spleen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.cintcm.tcmls.Constants;

public class loadTypesFromOracle {
	
	private static String sql = "select distinct m.record_id, m.class_def_id from t_record m";
	private static String toSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	private Map<String, String> classes = new HashMap<String, String>();

	private void loadClasses() {
		classes.put("4", "中药");
		classes.put("5", "证候");
		classes.put("6", "疾病");
		classes.put("7", "症状");
		classes.put("8", "方剂");
		classes.put("11", "证候加减");
		classes.put("10", "工具书");
		classes.put("12", "治法");
		classes.put("13", "病因");
	}

	public void loadAndTransform(Connection con, Connection toCon)
			throws SQLException {
		loadClasses();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		
		PreparedStatement pStmt = toCon.prepareStatement(toSql);

		while (rs.next()) {
			String instance_id = rs.getString(1);
			String class_id = rs.getString(2);
			String class_label = this.classes.get(class_id);

			if ((class_label != null) && (class_label != "")) {
				System.out.println(instance_id + " 类型 " + class_label);
				try {
					pStmt.setString(1, Constants.SPLEEN + instance_id);
					pStmt.setString(2, "类型");
					pStmt.setString(3, class_label);
					pStmt.setString(4, "0");
					pStmt.executeUpdate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();

		loadTypesFromOracle hierarchy = new loadTypesFromOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
