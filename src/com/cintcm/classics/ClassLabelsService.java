package com.cintcm.classics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ClassLabelsService {

	private static String sql = "select m.f_item_id, m.f_1028, m.f_1029 from t_cl_1025 m";

	private Map<String, String> map = new HashMap<String, String>();

	public ClassLabelsService(Connection con) {
		loadAndTransform(con);
	}

	public String getLabel(String id) {
		return map.get(id);
	}

	private String language(int type) {
		switch (type) {
		case 12:
			return "zh";
		case 0:
			return "en";
		default:
			return null;
		}
	}

	private boolean isPrefLabel(int type) {
		switch (type) {
		case 12:
			return true;
		case 0:
			return false;
		default:
			return false;
		}
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

		Connection con = DBUtils.getDBConnection();
		ClassLabelsService hierarchy = new ClassLabelsService(con);

	}
}
