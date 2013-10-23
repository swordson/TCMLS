package com.cintcm.tcmls.spleen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class RelationLabelsService {

	private static String sql = "select t.relation_def_id, t.relation_name, t.reverse_relation_name from t_relation_def t";

	private Map<String, String> map = new HashMap<String, String>();
	private Map<String, String> rmap = new HashMap<String, String>();


	public RelationLabelsService(Connection con) {
		loadAndTransform(con);
	}

	public String getLabel(String id, boolean reverse) {
		if (reverse){
			return rmap.get(id);
		}else{
			return map.get(id);
		}
		
	}

	public void loadAndTransform(Connection con) {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String property = rs.getString(1);
				String label = rs.getString(2);
				String rlabel = rs.getString(3);

				System.out.println(property + ":" + label + ":" + rlabel);
				this.map.put(property, label);
				this.rmap.put(property, rlabel);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection con = DBUtils.getDBConnection();
		RelationLabelsService hierarchy = new RelationLabelsService(con);

	}
}
