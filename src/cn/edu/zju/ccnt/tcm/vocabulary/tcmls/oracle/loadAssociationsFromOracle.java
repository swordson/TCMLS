package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;
import com.cintcm.tcmls.MySQLUtils;

public class loadAssociationsFromOracle {

	private static String sql = "select t.left_conc_id, t.right_conc_id, t.type from mt.t_associative_rels t";
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
			System.out.println(Constants.TCMLS + subject + " " + pLabels.getLabel(property) + " " + Constants.TCMLS + object);
			
			try {
				pStmt.setString(1,Constants.TCMLS + subject);
				pStmt.setString(2, pLabels.getLabel(property));
				pStmt.setString(3, Constants.TCMLS + object);
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

		loadAssociationsFromOracle hierarchy = new loadAssociationsFromOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
	
}
