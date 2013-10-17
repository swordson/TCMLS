package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;
import com.cintcm.tcmls.MySQLUtils;

/**
 * 表t_ur_1027用于存储一体化语言系统中的概念释义。
 * 
 * @author Tong Yu
 * 
 */
public class loadLabelFomOracle {

	private static String sql = "select distinct t.concept_id, t.term_label, t.type, t.lan_preferred from t_term t";

	private static String toSql = "insert into def (id,name) values (?,?)";

	private static String toGraphSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	private boolean isPrefLabel(int type, int lan_preferred) {
		if ((type == 0 || type == 1) && (lan_preferred == 0)) {
			return true;
		} else {
			return false;
		}
	}

	private String language(int type) {
		switch (type) {
		case 51:
			return "英文";
		case 53:
			return "英文";
		case 55:
			return "英文";
		case 1:
			return "中文";
		case 0:
			return "中文";
		default:
			return "";
		}
	}

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
			int type = rs.getInt(3);
			int lan_preferred = rs.getInt(4);

			if ((label != null) && (label != "")) {
				if (this.isPrefLabel(type, lan_preferred)) {
					System.out.println(concept + ": " + label);
					pStmt.setString(1, concept);
					pStmt.setString(2, label);
					try {
						pStmt.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				gStmt.setString(1, Constants.TCMLS + concept);
				gStmt.setString(2, getProperty(type, lan_preferred));
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

	private String getProperty(int type, int lan_preferred) {
		return language(type) + ((lan_preferred == 0)?"正名":"异名"); 
		
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();

		loadLabelFomOracle hierarchy = new loadLabelFomOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
