package com.cintcm.tcmct;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ClassLabelsService {

	private static String sql = "select id, value from cls";

	private Map<String, String> map = new HashMap<String, String>();

	public ClassLabelsService(Connection con) {
		loadAndTransform(con);
	}

	public String getLabel(String id) {
		return map.get(id);
	}

	
	public void loadAndTransform(Connection con) {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString(1);
				String label = rs.getString(2);
				System.out.println(id + label);
				this.map.put(id, label);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection con = MySQLUtils.getConnection();
		ClassLabelsService hierarchy = new ClassLabelsService(con);

	}
}
