package com.cintcm.classics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class LabelToIdService {

	private static String sql = "select id, name from def t";
	private Map<String, String> map = new HashMap<String, String>();

	LabelToIdService(Connection con) {
		loadAndTransform(con);
	}
	
	public String getId(String label){
		return map.get(label);
	}

	public void loadAndTransform(Connection con) {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String concept = rs.getString(1);
				String label = rs.getString(2);

				System.out.println(concept + ": " + label);
				this.map.put(label, concept);

			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		Connection con = MySQLUtils.getConnection();

		LabelToIdService hierarchy = new LabelToIdService(con);

		hierarchy.loadAndTransform(con);
	}

}
