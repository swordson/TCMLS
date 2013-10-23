package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.MySQLUtils;

public class loadDefFromOracle {

	private static String sql = "select t.f_master_id, t.f_1025 from mt.t_ur_1024 t";
	private static String toSql = "update def set def= ? where id = ?";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {

		Statement stmt = fromCon.createStatement();

		PreparedStatement pStmt = toCon.prepareStatement(toSql);

		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String concept = rs.getString(1);
			String label = rs.getString(2);

			if ((label != null) && (label != "")) {

				System.out.println(concept + ": " + label);
				pStmt.setString(1, label);
				pStmt.setString(2, concept);
				try {
					pStmt.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		Connection toCon = MySQLUtils.getConnection();
		loadDefFromOracle hierarchy = new loadDefFromOracle();
		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
