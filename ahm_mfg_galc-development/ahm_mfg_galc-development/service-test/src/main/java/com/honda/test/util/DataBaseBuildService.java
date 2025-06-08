package com.honda.test.util;

import java.sql.SQLException;

import com.honda.galc.service.IService;

public interface DataBaseBuildService extends IService{

	public int executeNativeUpdate(String ddl);
	
	public void insertTestData();
	
	public void cleanAllData() throws SQLException;
}
