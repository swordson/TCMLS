package com.cintcm.classics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;

public class loadAssociations {

	private static String sql = "select t.left_conc_id, t.right_conc_id, t.type from t_associative_rels t";
	private static String toSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	
	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {

		PropertyLabelsService pLabels = new PropertyLabelsService(fromCon);

	    Statement stmt = fromCon.createStatement();
		PreparedStatement pStmt = toCon.prepareStatement(toSql);
		
		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String subject = rs.getString(1);
			String object = rs.getString(2);
			String property = rs.getString(3);
			System.out.println(Constants.CLAN + subject + " " + pLabels.getLabel(property) + " " + Constants.CLAN + object);
			
			try {
				pStmt.setString(1,Constants.CLAN + subject);
				pStmt.setString(2, pLabels.getLabel(property));
				pStmt.setString(3, Constants.CLAN + object);
				pStmt.setString(4, "0");
				pStmt.executeUpdate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();

		loadAssociations hierarchy = new loadAssociations();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
	
}
