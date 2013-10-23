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
public class loadRelationsFromOracle {

	private static String sql = "select t.record_id_1, t.record_id_2, t.relation_def_id, t.reverse_flag from t_record_rel t";

	private static String toGraphSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {

		Statement stmt = fromCon.createStatement();
		PreparedStatement gStmt = toCon.prepareStatement(toGraphSql);
		RelationLabelsService service = new RelationLabelsService(fromCon);

		LabelsService labels_service = new LabelsService(fromCon);

		ResultSet rs = stmt.executeQuery(sql);
		int i = 0;
		while (rs.next()) {
			String c1 = rs.getString(1);
			String c2 = rs.getString(2);

			String r_id = rs.getString(3);
			Integer flag = rs.getInt(4);

			String r_name = service.getLabel(r_id, (flag == 1));

			if ((r_name != null) && (r_name != "")) {

				System.out.println(labels_service.getLabel(c1) + " : " + r_name
						+ " : " + labels_service.getLabel(c2));

				gStmt.setString(1, Constants.SPLEEN + c1);
				gStmt.setString(2, r_name);
				gStmt.setString(3, Constants.SPLEEN + c2);
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

		loadRelationsFromOracle hierarchy = new loadRelationsFromOracle();

		hierarchy.loadAndTransform(fromCon, toCon);

	}
}
