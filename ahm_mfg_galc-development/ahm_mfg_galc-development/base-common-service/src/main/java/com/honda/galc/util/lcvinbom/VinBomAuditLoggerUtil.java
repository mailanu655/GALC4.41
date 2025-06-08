package com.honda.galc.util.lcvinbom;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.lcvinbom.VinBomAuditLogDao;
import com.honda.galc.entity.enumtype.VinBomAuditLogChangeType;
import com.honda.galc.entity.lcvinbom.VinBomAuditLog;

public class VinBomAuditLoggerUtil {

	public static <E> void logAuditInfo(Class<E> entityClass, E currentValue, E updatedValue, String userId, VinBomAuditLogChangeType changeType) {
		logAuditInfo(entityClass, currentValue, updatedValue, null, userId, changeType);
	}
	
	public static <E> void logAuditInfo(Class<E> entityClass, Object inputData, String userId, VinBomAuditLogChangeType changeType) {
		logAuditInfo(entityClass, null, null, inputData, userId, changeType);
	}
	
	private static <E> void logAuditInfo(Class<E> entityClass, E currentValue, E updatedValue, Object inputData, String userId, VinBomAuditLogChangeType changeType) {
		try {
			String inputValue = (inputData!=null)?StringUtils.trimToEmpty(inputData.toString()):StringUtils.EMPTY;
			//Fetch table name from entity class
			String tableName = entityClass.getAnnotation(Table.class).name();
			//Fetch Audit Details
			String primaryKeyValue = StringUtils.EMPTY;
			List<String> columnNameList = new ArrayList<String>();
			List<String> currentValueList = new ArrayList<String>();
			List<String> updatedValueList = new ArrayList<String>();
			Field[] entityFields = entityClass.getDeclaredFields();
			for (Field field : entityFields) {
				//Fetch Primary Key Value
				if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
					field.setAccessible(true);
					if(currentValue != null) {
						primaryKeyValue = getFieldValue(field.get(currentValue));
					} else if(updatedValue != null) {
						primaryKeyValue = getFieldValue(field.get(updatedValue));
					} else if(StringUtils.isNotBlank(inputValue)) {
						primaryKeyValue =  getFieldValue(inputValue);
					}
				}
				//Fetch column names and values
				if(field.isAnnotationPresent(Column.class)) {
					if(!field.isAnnotationPresent(Id.class) && !field.isAnnotationPresent(EmbeddedId.class)) {
						field.setAccessible(true);	
						columnNameList.add(StringUtils.trimToEmpty(field.getName()));
						if(currentValue != null)
							currentValueList.add(getFieldValue(field.get(currentValue)));
						if(updatedValue != null)
							updatedValueList.add(getFieldValue(field.get(updatedValue)));
					}
				}
			}
			String auditColumnName = (!columnNameList.isEmpty())?StringUtils.join(columnNameList, Delimiter.COMMA):StringUtils.EMPTY;
			String auditCurrentValue = (!currentValueList.isEmpty())?StringUtils.join(currentValueList, Delimiter.COMMA):inputValue;
			String auditUpdatedValue = (!updatedValueList.isEmpty())?StringUtils.join(updatedValueList, Delimiter.COMMA):StringUtils.EMPTY;
			VinBomAuditLog auditLog = prepareAuditLog(tableName, auditColumnName, primaryKeyValue, 
					auditCurrentValue, auditUpdatedValue, userId, changeType);
			if (auditLog!=null && validate(auditLog)) {
				getDao(VinBomAuditLogDao.class).save(auditLog);
			}
		} catch (Exception ex) {
			getLogger().error(ex, "Error occured while logging VIN-BOM Audit for Entity: "+entityClass);
		}
	}
	
	private static VinBomAuditLog prepareAuditLog(String tableName, String auditColumnName,String auditPrimaryKeyVlaue,
			String currentValue, String updatedValue, String userId, VinBomAuditLogChangeType changeType) {
		VinBomAuditLog auditLog = new VinBomAuditLog();
		try {
			if (StringUtils.length(auditPrimaryKeyVlaue) >  auditLog.getClass().getDeclaredField("primaryKeyValue").getAnnotation(Column.class).length()) {
				auditPrimaryKeyVlaue = StringUtils.substring(auditPrimaryKeyVlaue, 0,  auditLog.getClass().getDeclaredField("primaryKeyValue").getAnnotation(Column.class).length());
			}
			
			if (StringUtils.length(currentValue) > auditLog.getClass().getDeclaredField("currentValue").getAnnotation(Column.class).length()) {
				currentValue = StringUtils.substring(currentValue, 0, auditLog.getClass().getDeclaredField("currentValue").getAnnotation(Column.class).length());
			}
			if (StringUtils.length(auditColumnName) > auditLog.getClass().getDeclaredField("columnName").getAnnotation(Column.class).length()) {
				auditColumnName = StringUtils.substring(auditColumnName, 0, auditLog.getClass().getDeclaredField("columnName").getAnnotation(Column.class).length());
			}
			auditLog.setTableName(StringUtils.trimToEmpty(tableName));
			auditLog.setColumnName(auditColumnName.toUpperCase());
			auditLog.setPrimaryKeyValue(StringUtils.trimToEmpty(auditPrimaryKeyVlaue));
			auditLog.setChangeType(changeType);
			auditLog.setCurrentValue(getFieldValue(currentValue));
			auditLog.setUpdatedValue(updatedValue);	
			auditLog.setAssociateNumber(StringUtils.trimToEmpty(userId));
			auditLog.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		} catch(Exception ex) {
			getLogger().error(ex, "Error occured while preparing VIN-BOM Audit Log Entity: "); 
		}
		return auditLog;
	}
	
	private static String getFieldValue(Object obj) {
		String fieldValue = StringUtils.EMPTY;
		if (obj != null) {
			String value = String.valueOf(obj);
			if(value !=null) {
				if(value.indexOf("(") > 0 && value.lastIndexOf(")") > 0) {
					value = value.substring(value.indexOf("(")+1, value.lastIndexOf(")"));
				}
				if(value != null)
					fieldValue = value.trim().length() == 0 ? "" : value.trim();
			}
		} 
		return fieldValue;
	}
	
	private static boolean validate(VinBomAuditLog auditLog) {
		if(auditLog.getTableName()==null || auditLog.getColumnName()==null 
				|| auditLog.getPrimaryKeyValue()==null || auditLog.getChangeType()==null) {
			return false;
		} else {
			return true;
		}
	}
	
	private static Logger getLogger(){
		return Logger.getLogger();
	}
}
