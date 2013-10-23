package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TypesService {

	private static String sql = "select distinct m.f_master_id, m.f_1034 from t_ur_1026 m";
	
	private Map<String, String> map = new HashMap<String, String>();

	public TypesService(Connection con) {
		loadAndTransform(con);
	}

	public String getType(String entity){
		return map.get(entity);
	}
	
	public void loadAndTransform(Connection con)  {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			ClassLabelsService cLabels = new ClassLabelsService(con);
			

			while (rs.next()) {
				String instance_id = rs.getString(1);
				String class_id = rs.getString(2);
				String class_label = cLabels.getLabel(class_id);

				if ((class_label != null) && (class_label != "")) {
					System.out.println(instance_id + " 类型 " + class_label);
				    map.put(instance_id, class_label);

				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection fromCon = DBUtils.getDBConnection();
		// Connection toCon = MySQLUtils.getConnection();

		TypesService hierarchy = new TypesService(fromCon);

	}
}
