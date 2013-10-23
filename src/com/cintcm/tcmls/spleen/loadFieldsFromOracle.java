package com.cintcm.tcmls.spleen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;

/**
 *
 * 
 * @author Tong Yu
 * 
 */
public class loadFieldsFromOracle {

	private static String sql = "select t.record_id, t.field_def_id, t.field_value from t_field t";

	private static String toGraphSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {

		Statement stmt = fromCon.createStatement();
		PreparedStatement gStmt = toCon.prepareStatement(toGraphSql);
		FieldLabelsService service = new FieldLabelsService(fromCon);

		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String concept = rs.getString(1);
			String field_id = rs.getString(2);
			String field_name = service.getLabel(field_id);
			String label = rs.getString(3);

			if ((label != null) && (label != "")) {

				System.out.println(concept + ": " + label);
				
				gStmt.setString(1, Constants.SPLEEN + concept);
				gStmt.setString(2, field_name);
				gStmt.setString(3, label);
				gStmt.setString(4, "0");
				try {
					gStmt.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			/*
			 * String toSql =
			 * "insert into graph (subject,property,value,user_id) values ('" +
			 * Constants.TCMLS + concept + "','注释','" + label + "','0'" + ")";
			 */

			if (i++ % 10000 == 0)
				System.out.println(i);
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();

		loadFieldsFromOracle hierarchy = new loadFieldsFromOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
