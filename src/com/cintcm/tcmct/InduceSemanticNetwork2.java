package com.cintcm.tcmct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cintcm.tcmls.Constants;

public class InduceSemanticNetwork2 {

	private static String sql = "select subject, value, property, id from graph t";
	private static String toSql = "insert into semantic_network (subject, property, object, count, instances) values (?,?,?,?,?)";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {
		Map<String, Set<Integer>> sn = new HashMap<String, Set<Integer>>();

		TypesService tService = new TypesService(fromCon);
		
		Statement stmt = toCon.createStatement();
		PreparedStatement pStmt = toCon.prepareStatement(toSql);

		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String subject = rs.getString(1);
			String object = rs.getString(2);
			String property = rs.getString(3);
			Integer id = rs.getInt(4);
			
			if ((subject != null) && (!subject.isEmpty()) && (object != null)
					&& (!object.isEmpty())
					&& ((object.startsWith(Constants.TCMCT)))
					&& (property != null) && (!property.isEmpty())) {
				
				String sType = tService.getType(subject);
				String oType = tService.getType(object);
				System.out.println(sType + oType);
				if ((sType != null) && (!sType.isEmpty()) && (oType != null)
						&& (!oType.isEmpty())) {
					System.out.println(subject + "(" + sType
							+ ") " + property + " " + object
							+ "(" + oType + ") ");

					String triple = sType + "|" + property + "|" + oType;

					if (sn.containsKey(triple)) {
						sn.get(triple).add(id);
					} else {
						Set<Integer> ids = new HashSet<Integer>();
						ids.add(id);
						sn.put(triple, ids);
					}

				}

			}

		}

		for (String triple : sn.keySet()) {

			Set<Integer> ids = sn.get(triple);
			
			

			Integer count = ids.size();

			System.out.println(triple + ":" + count);
			String[] a = triple.split("\\|");

			try {
				pStmt.setString(1, a[0]);
				pStmt.setString(2, a[1]);
				pStmt.setString(3, a[2]);
				pStmt.setInt(4, count);
				boolean first = true;
				String ids_string = null; 
				for (Integer id : new ArrayList<Integer>(ids).subList(0, Math.min(10, count))){
				    if (first){
				    	ids_string = String.valueOf(id);
				    	first = false;
				    }else{
				    	ids_string += "|" + id;
				    }
				}
				pStmt.setString(5, ids_string);
				pStmt.executeUpdate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection toCon = MySQLUtils.getConnection();
		InduceSemanticNetwork2 hierarchy = new InduceSemanticNetwork2();
		hierarchy.loadAndTransform(toCon, toCon);

	}

}
