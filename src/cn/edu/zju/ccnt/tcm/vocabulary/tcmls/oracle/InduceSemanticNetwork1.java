package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.cintcm.tcmls.Constants;
import com.cintcm.tcmls.MySQLUtils;

public class InduceSemanticNetwork1 {

	private static String sql = "select subject, object, predicate from relation t";
	private static String toSql = "insert into sn2 (subject, property, object, count) values (?,?,?,?)";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {
		Map<String, Integer> sn = new HashMap<String, Integer>();

		TypesService tService = new TypesService(fromCon);
		LabelToIdService labelToIdService = new LabelToIdService(toCon);

		Statement stmt = toCon.createStatement();
		PreparedStatement pStmt = toCon.prepareStatement(toSql);

		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String subject = rs.getString(1);
			String sType = tService.getType(labelToIdService.getId(subject));
			String object = rs.getString(2);
			String oType = tService.getType(labelToIdService.getId(object));
			String property = rs.getString(3);
			String[] properties = property.split("\\|");
			// String pLabel = pLabels.getLabel(property);

			System.out.println(Constants.TCMLS + subject + "(" + sType + ") "
					+ property + " " + Constants.TCMLS + object + "(" + oType
					+ ") ");

			if ((sType != null) && (!sType.isEmpty()) && (oType != null)
					&& (!oType.isEmpty()) && (properties != null)) {
				for (String p : properties) {
					if ((p != null) && (!p.isEmpty())) {
						String triple = sType + "|" + p + "|" + oType;
						
						if (sn.containsKey(triple)) {
							sn.put(triple, sn.get(triple) + 1);
						} else {
							sn.put(triple, 1);
						}
					}

				}

			}

		}

		for (String triple : sn.keySet()) {

			Integer count = sn.get(triple);
			System.out.println(triple + ":" + count);
			String[] a = triple.split("\\|");

			try {
				pStmt.setString(1, a[0]);
				pStmt.setString(2, a[1]);
				pStmt.setString(3, a[2]);
				pStmt.setInt(4, count);
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
		InduceSemanticNetwork1 hierarchy = new InduceSemanticNetwork1();
		hierarchy.loadAndTransform(fromCon, toCon);

	}

}
