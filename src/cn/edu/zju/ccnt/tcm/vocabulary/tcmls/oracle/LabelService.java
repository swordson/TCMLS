package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class LabelService {
	
	private static String sql = "select distinct t.concept_id, t.term_label, t.type from t_term t";
	private Map<String, String> map = new HashMap<String, String>();
	
	LabelService(Connection con){
		loadAndTransform(con);		
	}
	
	

	private String language(int type) {
		switch (type) {
		case 51:
			return "en";
		case 53:
			return "en";
		case 55:
			return "en";
		case 1:
			return "zh-cn";
		case 0:
			return "zh-cn";
		default:
			return null;
		}
	}
	
	private boolean isPrefLabel(int type){
		if (type == 1) {
		    return true;
		}
		else{
			return false;
		}
	}

	public void loadAndTransform(Connection con)  {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				String concept = rs.getString(1);
				String label = rs.getString(2);
				int type = rs.getInt(3);
				
				if (this.isPrefLabel(type)){
					System.out.println(concept + ": " + label);
					this.map.put(concept, label);
				}
				



				if (i++ % 10000 == 0)
					System.out.println(i);
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

}
