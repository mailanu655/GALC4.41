package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.IdCreateId;
import com.honda.galc.service.Parameters;

public class IdCreateDaoImpl extends BaseDaoImpl<IdCreate, IdCreateId> implements IdCreateDao {
	
	private static String INCREMENT_CURRENT_ID="update IdCreate e set e.currentId=e.currentId +1 where e.id.tableName=:tableName and e.id.columnName=:columnName and e.currentId < e.endId";                                            

	public synchronized IdCreate getIdCreate(String tableName, String columnName) {
		IdCreateId idCreateId = new IdCreateId();
		idCreateId.setTableName(tableName);
		idCreateId.setColumnName(columnName);
		return findByKey(idCreateId);	
	}
	
	@Transactional
	public synchronized int incrementAndGetStatus(String tableName, String columnName) {
		Parameters params = Parameters.with("tableName", tableName).put("columnName", columnName);	
		return executeUpdate(INCREMENT_CURRENT_ID,params);
	}
	
	@Transactional
	public synchronized IdCreate incrementId(String tableName, String columnName) {
		Parameters params = Parameters.with("tableName", tableName).put("columnName", columnName);	
		int status = executeUpdate(INCREMENT_CURRENT_ID,params);
		if(status == 0)  return null;
		else  {
			IdCreate newId = getIdCreate(tableName, columnName);
			return newId;
		}
	}
	
	//creating separate method with a sub-transaction because above methods are used elsewhere
	//since it involves transaction propagation, prefer to leave existing methods as is.
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public synchronized IdCreate incrementIdWithNewTransaction(String tableName, String columnName) {
		Parameters params = Parameters.with("tableName", tableName).put("columnName", columnName);	
		int status = executeUpdate(INCREMENT_CURRENT_ID,params);
		if(status == 0)  return null;
		else  {
			IdCreate newId = getIdCreate(tableName, columnName);
			return newId;
		}
	}


}
