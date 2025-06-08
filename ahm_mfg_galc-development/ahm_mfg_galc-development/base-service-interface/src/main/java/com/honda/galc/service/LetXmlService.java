package com.honda.galc.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.LetProgramResultValue;
import com.honda.galc.entity.product.LetSpool;

public interface LetXmlService extends IService{

	void reloadLetInspectionProgram();
	
	void reloadLetInspectionParam();
	
	Map<String, Integer> getLetInspectionParamMap();
	
	Map<String, Integer> getLetInspectionProgramMap();
	
	int getLetResultCountForProduct(String product);
	
	int getLetInspectionParamId(String letInspectionParam);
	
	int getLetInspectionPgmId(String letInspectionPgmId);
	
	boolean saveLetXmlData(HashMap<String, ArrayList<?>> letMap);
	
    void saveLetProgramResultValues(List<LetProgramResultValue> letProgramResultValues);
    
    void insertLetProgramResultValues(List<LetProgramResultValue> letProgramResultValues);
	
	String getPhysicalTableName(Timestamp timestamp, Class<?> clazz);
	
	public void processLetMessage(LetSpool letSpool, String letDeviceIpAddress, String message);
}
