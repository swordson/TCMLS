package com.cintcm.tcmls.spleen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class FieldLabelsService {

	private static String sql = "select t.field_def_id, t.field_name from t_field_def t";

	private Map<String, String> map = new HashMap<String, String>();

	public FieldLabelsService(Connection con) {
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
				String property = rs.getString(1);
				String label = rs.getString(2);

				System.out.println(property + label);
				this.map.put(property, label);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection con = DBUtils.getDBConnection();
		FieldLabelsService hierarchy = new FieldLabelsService(con);

	}
}
