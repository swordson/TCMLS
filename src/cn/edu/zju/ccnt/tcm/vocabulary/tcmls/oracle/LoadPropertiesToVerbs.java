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
import java.util.HashMap;
import java.util.Map;

import com.cintcm.tcmls.Constants;
import com.cintcm.tcmls.MySQLUtils;

public class LoadPropertiesToVerbs {

	private static String sql = "select t.f_item_id, t.f_lang, t.f_code, t.f_description from mt.t_cl_25 t";
	private static String toSql = "insert into verbs (property) values (?)";
			
	private String language(int type) {
		switch (type) {
		case 12:
			return "zh";
		case 0:
			return "en";
		default:
			return null;
		}
	}
	
	private boolean isPrefLabel(int type) {
		switch (type) {
		case 12:
			return true;
		case 0:
			return false;
		default:
			return false;
		}
	}
	

	public void loadAndTransform(Connection con, Connection toCon)  {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			PreparedStatement pStmt = toCon.prepareStatement(toSql);
			
			

			while (rs.next()) {
				int lan = rs.getInt(2);
				String label = rs.getString(3);
				
				if (this.isPrefLabel(lan)){
					pStmt.setString(1, label);					
					pStmt.executeUpdate();
				}
				

				

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection con = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();

		LoadPropertiesToVerbs hierarchy = new LoadPropertiesToVerbs();

		hierarchy.loadAndTransform(con, toCon);

	}
}
