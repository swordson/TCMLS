package com.cintcm.tcmls.spleen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;

/**
 * 
 * @author Tong Yu
 * 
 */
public class loadHierarchyFromOracle {

	private static String sql = "select t.parent_record_id, t.child_record_id from t_hierarchy t";

	private static String toGraphSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {

		Statement stmt = fromCon.createStatement();
		PreparedStatement gStmt = toCon.prepareStatement(toGraphSql);

		LabelsService labels_service = new LabelsService(fromCon);

		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String c1 = rs.getString(1);
			String c2 = rs.getString(2);

			if (!c1.equals("-1000") && !c2.equals("-1000")) {
				System.out.println(c1 + " : " + labels_service.getLabel(c1)
						+ " : " + c2 + " : " + labels_service.getLabel(c2));

				gStmt.setString(1, Constants.SPLEEN + c1);
				gStmt.setString(2, "下位词");

				gStmt.setString(3, Constants.SPLEEN + c2);
				gStmt.setString(4, "0");
				try {
					gStmt.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}

				gStmt.setString(1, Constants.SPLEEN + c2);
				gStmt.setString(2, "上位词");

				gStmt.setString(3, Constants.SPLEEN + c1);
				gStmt.setString(4, "0");
				try {
					gStmt.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (i++ % 10000 == 0)
				System.out.println(i);
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();

		loadHierarchyFromOracle hierarchy = new loadHierarchyFromOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
