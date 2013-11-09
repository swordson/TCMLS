package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.MySQLUtils;

public class InduceSemanticNetwork3 {

	private static String sql = "select id, instances from semantic_network";
	private static String toSql = "insert into triple_type (triple, type) values (?,?)";

	public void loadAndTransform(Connection fromCon, Connection toCon)
			throws SQLException {

		try {

			// FileWriter writer = new FileWriter("insert.sql");
			PrintWriter writer = new PrintWriter(new FileWriter("insert.sql"));
			writer.println();
			Statement stmt = toCon.createStatement();
			PreparedStatement pStmt = toCon.prepareStatement(toSql);

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				Integer id = rs.getInt(1);
				String[] instances = rs.getString(2).split("\\|");

				for (String instance : instances) {
					try {
						System.out.println(id + " : " + instance);
						pStmt.setString(1, instance);
						pStmt.setInt(2, id);
						pStmt.executeUpdate();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void loadAndWrite(Connection con) throws SQLException {

		try {

			// FileWriter writer = new FileWriter("insert.sql");
			PrintWriter writer = new PrintWriter(new FileWriter("insert.sql"));
			writer.println("insert into triple_type (triple, type) values");
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(sql);
			int i = 0;
			//int c =0;
			while (rs.next()) {				

				Integer id = rs.getInt(1);
				String[] instances = rs.getString(2).split("\\|");

				for (String instance : instances) {
					
					if (i == 0) {
						
					} else if (i == 10000){
						writer.println(";");
						writer.println("insert into triple_type (triple, type) values");
						i=0;
				    } else {
				    	writer.println(",");
					}
					
					i++;
					System.out.println(instance + ":" + id);
					writer.print("(" + instance + "," + id + ")");
				}
				//if (c++ > 100000) break;

			}
			writer.println(";");
			writer.flush();
			writer.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection toCon = MySQLUtils.getConnection();
		InduceSemanticNetwork3 hierarchy = new InduceSemanticNetwork3();
		hierarchy.loadAndWrite(toCon);

	}

}
