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
import java.util.HashSet;
import java.util.Set;

import com.cintcm.tcmls.Constants;
import com.cintcm.tcmls.MySQLUtils;

public class LoadVerbs {

	private static String sql = "SELECT distinct verb,property FROM verbs";
	

	public void loadAndTransform(Connection con) throws SQLException {

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Set<String> verbSet = new HashSet<String>();

		while (rs.next()) {
			String verbs = rs.getString("verb");			
			if (verbs != null) {
				for (String verb : verbs.split("\\$")) {
					if ((verb != null) && (verb != "")) {
						verbSet.add(verb);
					}
				}
			}

		}

		for (String verb : verbSet) {
			System.out.println(verb);
		}
		System.out.println(verbSet.size());

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection toCon = MySQLUtils.getConnection();

		LoadVerbs hierarchy = new LoadVerbs();

		hierarchy.loadAndTransform(toCon);

	}
}
