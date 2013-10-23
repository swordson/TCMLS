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
public class loadLabelFomOracle {

	private static String sql = "select distinct t.record_id, t.record_name from t_record t";

	private static String toSql = "insert into def (id,name) values (?,?)";

	private static String toGraphSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {

		Statement stmt = fromCon.createStatement();
		PreparedStatement pStmt = toCon.prepareStatement(toSql);
		PreparedStatement gStmt = toCon.prepareStatement(toGraphSql);

		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String concept = rs.getString(1);
			String label = rs.getString(2);

			if ((label != null) && (label != "")) {

				System.out.println(concept + ": " + label);
				pStmt.setString(1, concept);
				pStmt.setString(2, label);
				try {
					pStmt.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}

				gStmt.setString(1, Constants.SPLEEN + concept);
				gStmt.setString(2, "中文正名");
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

		loadLabelFomOracle hierarchy = new loadLabelFomOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
