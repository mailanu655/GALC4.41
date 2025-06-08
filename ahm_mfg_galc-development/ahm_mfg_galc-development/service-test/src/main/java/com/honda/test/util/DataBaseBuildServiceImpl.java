package com.honda.test.util;

import java.sql.SQLException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.JpaEntityManager;

public class DataBaseBuildServiceImpl extends JpaEntityManager implements DataBaseBuildService{
	
	private static String ALL_TABLES = 
			"SELECT TABLE_SCHEMA,TABLE_NAME FROM INFORMATION_SCHEMA.TABLES";
	
	public DataBaseBuildServiceImpl() {
		
	}

	@Override
	@Transactional
	public int executeNativeUpdate(String ddl) {
		if(ddl == null || ddl.length() == 0) return 0;
		return super.executeNativeUpdate(ddl);
	}
	
	

	@Override
	public void insertTestData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public void cleanAllData() throws SQLException {
		List<Object[]> resultSets = super.findResultListByNativeQuery(ALL_TABLES, null);
		
		for(Object[] resultSet : resultSets) {
			String tableSchema = (String)resultSet[0];
			String tableName = (String)resultSet[1];
			if(!tableSchema.equalsIgnoreCase("INFORMATION_SCHEMA")) {
				String sql = "DELETE FROM " + tableSchema + "." + tableName;
				this.executeNativeUpdate(sql);
			}
		}
	}
}
