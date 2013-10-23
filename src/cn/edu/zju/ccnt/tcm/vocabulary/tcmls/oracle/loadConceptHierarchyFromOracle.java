package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;
import com.cintcm.tcmls.MySQLUtils;

public class loadConceptHierarchyFromOracle {

	private static String sql = "select distinct t.child_id, t.parent_id from mt.t_concept_rels t";
	private static String toSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";



	public void loadAndTransform(Connection con, Connection toCon) throws SQLException {

		Statement stmt = con.createStatement();
		PreparedStatement pStmt = toCon.prepareStatement(toSql);
		
		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String child = rs.getString(1);
			String parent = rs.getString(2);
			if ((null != child) && (null != parent)) {
				try {
					pStmt.setString(1,Constants.TCMLS + child);
					pStmt.setString(2, "上位词");
					pStmt.setString(3, Constants.TCMLS + parent);
					pStmt.setString(4, "0");
					pStmt.executeUpdate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					pStmt.setString(1,Constants.TCMLS + parent);
					pStmt.setString(2, "下位词");
					pStmt.setString(3, Constants.TCMLS + child);
					pStmt.setString(4, "0");
					pStmt.executeUpdate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}							
			
			}

		}

	

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();

		loadConceptHierarchyFromOracle hierarchy = new loadConceptHierarchyFromOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
