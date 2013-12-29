package com.cintcm.classics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;
/**
 * 表t_ur_1027用于存储一体化语言系统中的概念释义。
 * @author Tong Yu
 *
 */
public class loadComments {

	private static String sql = "select t.f_master_id, t.f_1035 from t_ur_1027 t";
	private static String toSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";
	
	

	public void loadAndTransform(Connection fromCon, Connection toCon) throws SQLException {

		Statement stmt = fromCon.createStatement();
		PreparedStatement pStmt = toCon.prepareStatement(toSql);
		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String concept = rs.getString(1);
			String label = rs.getString(2);

			
			/*String toSql = "insert into graph (subject,property,value,user_id) values ('" + Constants.TCMLS + concept
					+ "','注释','"
					+ label + "','0'"  + ")";*/
			if ((label != null) && (label != "")){
				System.out.println(concept + ":" + label);			
				
				pStmt.setString(1,Constants.CLAN + concept);
				pStmt.setString(2, "注释");
				pStmt.setString(3, label);
				pStmt.setString(4, "0");
				pStmt.executeUpdate();
				
			}
			
			
			

			if (i++ % 10000 == 0)
				System.out.println(i);
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();
		
		loadComments hierarchy = new loadComments();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
