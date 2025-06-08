package com.honda.galc.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.AuditLoggerException;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.audit.AuditLogDao;
import com.honda.galc.entity.audit.AuditLog;
import com.honda.galc.entity.audit.AuditLogId;
import com.honda.galc.dto.Auditable;

/**
 * <h3>AuditLoggerUtil Class description</h3> <br>
 * 
 * This utility is responsible for creating audit details for any modification
 * happening on persistable entity. <br>
 * This is the responsibility of every screen/object being modified to consume
 * this hook for maintaining audit information.<br>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>1.0</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * </TABLE>
 * 
 * @author L&T Infotech<br>
 *         October 12, 2016
 * 
 * 
 */
public class AuditLoggerUtil {
	
	/*
	 * This field is a combination of non auditable columns of an entity
	 * including referenced entity's value.<br> All the values will be separated
	 * by space.<br> This is the caller responsibility to provide this field if
	 * audit entity refer some other persistable entity's id. In absence of
	 * this, value will be derived through audit entity.
	 */
	private static String auditPrimaryKey;
	private static int CHANGE_REASON_LENGTH = 1024;
	private static int PRIMARY_KEY_VALUE_LENGTH = 286;
	private static int COLUMN_NAME_LENGTH= 128;
	private static int PRIMARY_KEY_COLUMN_NAME_LENGTH=128;
	private static String EMPTY_STRING = " ";

	/**
	 * Utility method responsible for preparing and persisting audit log object.
	 * Caller need to call this method if they are providing audit primary key
	 * value combination.
	 * 
	 * @param oldObj
	 * @param modifiedObj
	 * @param changeReason
	 * @param maintenanceScreen
	 * @param auditPrimaryKeyValue
	 */
	public static void logAuditInfo(Object oldObj, Object modifiedObj, String changeReason, String maintenanceScreen,
			String auditPrimaryKeyValue,String userId) {

		auditPrimaryKey = auditPrimaryKeyValue != null ? auditPrimaryKeyValue.trim() : null;
		logAuditInfo(oldObj, modifiedObj, changeReason, maintenanceScreen,userId);
	}
	
	public static void logAuditInfo(Object oldObj, Object modifiedObj,
			String changeReason, String maintenanceScreen,String userId) {
		logAuditInfo(oldObj, modifiedObj,
				changeReason, maintenanceScreen,userId, ApplicationConstants.AUDIT_SYSTEM, ApplicationConstants.AUDIT_CHANGE_TYPE);
	}

	/**
	 * Utility method responsible for preparing and persisting audit log object.
	 * 
	 * @param oldObj
	 * @param modifiedObj
	 * @param changeReason
	 * @param maintenanceScreen
	 * @throws AuditLoggerException
	 */
	public static void logAuditInfo(Object oldObj, Object modifiedObj,
			String changeReason, String maintenanceScreen,String userId, String system, String changeType) {

		// initializing audit log list
		List<AuditLog> auditLogList = new ArrayList<AuditLog>();
		String auditColumnName = StringUtils.EMPTY;
		String auditPrimaryKeyColumnName = StringUtils.EMPTY;
		String auditCurrentValue = StringUtils.EMPTY;
		String auditPreviousValue = StringUtils.EMPTY;
		try {
			String entityId = oldObj != null ? getEntityPrimaryKey(oldObj) : getEntityPrimaryKey(modifiedObj);	
			
			if (StringUtils.length(changeReason) > CHANGE_REASON_LENGTH) {
				changeReason = StringUtils.substring(changeReason, 0, CHANGE_REASON_LENGTH);
			}
			
			if (modifiedObj != null && oldObj!=null) {

				auditPrimaryKey = StringUtils.isNotEmpty(auditPrimaryKey) ? auditPrimaryKey : preparePrimaryKeyValue(modifiedObj);
				auditPrimaryKeyColumnName = getEntityprimaryKeyColumnName(oldObj);
				Field[] entityFields = oldObj.getClass().getDeclaredFields();
				for (Field field : entityFields) {
					if (field.isAnnotationPresent(Auditable.class)) {
						field.setAccessible(true);
						Object oldValue = field.get(oldObj);
						Object modifiedValue = field.get(modifiedObj);

						if (oldValue == modifiedValue || (oldValue != null && oldValue.equals(modifiedValue))) {
							continue;
							// if values are equal then no need to log it in database.
						}
						
						// check if composite Ids have been modified
						if (field.isAnnotationPresent(EmbeddedId.class)) {
							compareEmbeddedIds(oldObj, oldValue, modifiedValue, changeReason, maintenanceScreen,
									entityId, auditLogList,userId,system, changeType);
							continue;
						}
						
						if (StringUtils.length(auditPrimaryKey) > PRIMARY_KEY_VALUE_LENGTH) {
							auditPrimaryKey = StringUtils.substring(auditPrimaryKey, 0, PRIMARY_KEY_VALUE_LENGTH);
						}
						
						AuditLog auditLog = prepareAuditLogObj(modifiedObj, field, oldValue, modifiedValue,
								auditPrimaryKey, changeReason, maintenanceScreen, entityId,userId,system,changeType);

						if (validate(auditLog))
							auditLogList.add(auditLog);
					}
				}
		    }
			else if (modifiedObj == null) { 	
				// delete functionality
				auditPrimaryKey = StringUtils.isNotEmpty(auditPrimaryKey) ? auditPrimaryKey	: preparePrimaryKeyValue(oldObj);
				auditPrimaryKeyColumnName = getEntityprimaryKeyColumnName(oldObj);
				Field[] entityFields = oldObj.getClass().getDeclaredFields();
				StringBuilder builderPreviousValue = new StringBuilder();
				StringBuilder builderColumnName = new StringBuilder();
				int counter = 0;
				
				for (Field field : entityFields) {
					if (field.isAnnotationPresent(Auditable.class) && !field.isAnnotationPresent(EmbeddedId.class) && !field.isAnnotationPresent(Id.class)) {
						String columnName = EMPTY_STRING;
						String previousValue = EMPTY_STRING;
						field.setAccessible(true);
						columnName = field.getName();
						previousValue = getFieldValue(field.get(oldObj));

						if (counter != 0) {				
							builderColumnName.append(Delimiter.COMMA);
							builderPreviousValue.append(Delimiter.COMMA);
						}
						builderColumnName.append(columnName);
						builderPreviousValue.append(previousValue);
						counter++;
					}
				}
				if (builderPreviousValue != null) {	auditPreviousValue = builderPreviousValue.toString();}
				if (builderColumnName != null) {auditColumnName = builderColumnName.toString().trim();	}

				AuditLog auditLog = prepareAuditLog(oldObj,modifiedObj, auditColumnName, auditPrimaryKeyColumnName,
						auditPrimaryKey, auditCurrentValue, auditPreviousValue, changeReason, maintenanceScreen, userId,
						system, changeType,false);

				if (validate(auditLog)) {
					auditLogList.add(auditLog);
				}
			}	
				
			else if (oldObj == null) { //insert functionality						
				auditPrimaryKey = StringUtils.isNotEmpty(auditPrimaryKey) ? auditPrimaryKey : preparePrimaryKeyValue(modifiedObj);
				auditPrimaryKeyColumnName = getEntityprimaryKeyColumnName(modifiedObj);
				Field[] entityFields = modifiedObj.getClass().getDeclaredFields();
				StringBuilder builderCurrentValue = new StringBuilder();
				StringBuilder builderColumnName = new StringBuilder();
				int counter = 0;
						  
				for(Field field:entityFields) {
					if(field.isAnnotationPresent(Auditable.class)&& !field.isAnnotationPresent(EmbeddedId.class) && !field.isAnnotationPresent(Id.class)) {	
						String columnName = EMPTY_STRING;
						String currentValue = EMPTY_STRING;
						
						field.setAccessible(true);						
						columnName = field.getName();
						currentValue = getFieldValue(field.get(modifiedObj));
						if(counter!=0) {
							builderColumnName.append(Delimiter.COMMA);
							builderCurrentValue.append(Delimiter.COMMA);
						}							
						builderColumnName.append(columnName);
						builderCurrentValue.append(currentValue);  					
						counter++;
					}				
				}
				
				if(builderCurrentValue!=null) { auditCurrentValue  = builderCurrentValue.toString();}
				if(builderColumnName!=null) { auditColumnName = builderColumnName.toString().trim();}
										
			    AuditLog auditLog = prepareAuditLog(oldObj,modifiedObj,auditColumnName,auditPrimaryKeyColumnName,auditPrimaryKey,auditCurrentValue,
					auditPreviousValue,changeReason, maintenanceScreen,userId, system, changeType,true);	
			
				if (validate(auditLog))	{ auditLogList.add(auditLog); }
			}
			
			auditPrimaryKey = null;
			// calling dao to log it in database
			if (!auditLogList.isEmpty())
				getDao(AuditLogDao.class).saveAll(auditLogList);
			
		} catch (IllegalAccessException e) {
			throw new AuditLoggerException("Error occured while logging audit for entity -" + oldObj.getClass().getName(), e);
		} catch (Exception ex) {
			ex.printStackTrace(new java.io.PrintStream(System.out)); 
		}	
	}
	
	private static  AuditLog prepareAuditLog(Object oldObj,
			 Object modifiedObj,String auditColumnName,String auditPrimaryKeyColumnName, String primaryKeyValue,String auditCurrentValue,
			 String auditPreviousValue,String changeReason, String maintenanceScreen,String userId, String system, String changeType, boolean insertFlag) {
		
		if (StringUtils.length(auditPrimaryKey) > PRIMARY_KEY_VALUE_LENGTH) {
			auditPrimaryKey = StringUtils.substring(auditPrimaryKey, 0, PRIMARY_KEY_VALUE_LENGTH);
		}
		
		if (StringUtils.length(auditPrimaryKeyColumnName) > PRIMARY_KEY_COLUMN_NAME_LENGTH) {
			auditPrimaryKeyColumnName = StringUtils.substring(auditPrimaryKeyColumnName, 0, PRIMARY_KEY_COLUMN_NAME_LENGTH);
		}
		
		if (StringUtils.length(auditColumnName) > COLUMN_NAME_LENGTH) {
			auditColumnName = StringUtils.substring(auditColumnName, 0, COLUMN_NAME_LENGTH);
		}
		
		AuditLog auditLog = new AuditLog();
		AuditLogId auditLogId = new AuditLogId();
		if(insertFlag) {
			auditLogId.setTableName(modifiedObj.getClass().getAnnotation(Table.class).name());
		}else {
			auditLogId.setTableName(oldObj.getClass().getAnnotation(Table.class).name());
		}
		auditLogId.setColumnName(auditColumnName.toUpperCase());
		auditLogId.setActualTimeStamp(new Timestamp(System.currentTimeMillis()));
		auditLogId.setPrimaryKeyValue(auditPrimaryKey);		
		auditLog.setId(auditLogId);
		auditLog.setPrimaryKey(auditPrimaryKeyColumnName);
		auditLog.setChangeType(changeType);
		auditLog.setCurrentValue(auditCurrentValue);	
		auditLog.setMaintenanceScreen(maintenanceScreen);		
		auditLog.setPreviousValue(getFieldValue(auditPreviousValue));
		auditLog.setSystem(system);
		auditLog.setUpdateReason(changeReason);
		auditLog.setUpdateUser(userId);
		
		return auditLog;		
	}
		
	/**
	 * This method will be used to check if composite key of the objects have
	 * been modified.
	 * 
	 * @param oldEntityObject
	 * @param oldEmbeddedId
	 * @param modifiedEmbeddedId
	 * @param changeReason
	 * @param maintenanceScreen
	 * @param entityId
	 * @param auditLogList
	 * @throws IllegalAccessException
	 */
	private static void compareEmbeddedIds(Object oldEntityObject, Object oldEmbeddedId, Object modifiedEmbeddedId,
			String changeReason, String maintenanceScreen, String entityId, List<AuditLog> auditLogList,String userId,String system, String changeType)
			throws IllegalAccessException {

		Field[] embeddedIdFields = oldEmbeddedId.getClass().getDeclaredFields();
		for (Field field : embeddedIdFields) {
			if (field.isAnnotationPresent(Column.class)) {
				field.setAccessible(true);

				Object oldValue = field.get(oldEmbeddedId);
				Object modifiedValue = field.get(modifiedEmbeddedId);

				if (oldValue == modifiedValue || (oldValue != null && oldValue.equals(modifiedValue))) {
					continue;
					// if values are equal then no need to log it in database.
				}
				AuditLog auditLog = prepareAuditLogObj(oldEntityObject, field, oldValue, modifiedValue, auditPrimaryKey,
						changeReason, maintenanceScreen, entityId,userId,system,changeType);

				if (validate(auditLog))
					auditLogList.add(auditLog);
			}
		}
	}

	/**
	 * This method will be used to derive auditable entity primary key value;
	 * 
	 * @param entity
	 * @return entityId
	 * @throws IllegalAccessException
	 */
	private static String getEntityPrimaryKey(Object entity) throws IllegalAccessException {

		String entityId = "";
		Field[] entityFields = entity.getClass().getDeclaredFields();
		for (Field field : entityFields) {
			if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
				field.setAccessible(true);
				entityId = getFieldValue(field.get(entity));
				break;
			}
		}
		return entityId;
	}

	/**
	 * This method will be used to create primary key value for audit log based
	 * on annotation values present in field.<br>
	 * If there are multiple fields representing primary key value then all
	 * should be Separated with ' ' space delimiter.
	 * 
	 * @param modifiedObj
	 * @return primaryKeyValue
	 * @throws IllegalAccessException
	 */
	private static String preparePrimaryKeyValue(Object modifiedObj)
			throws IllegalAccessException {

		Map<Integer, String> valueSequenceMap = new TreeMap<Integer, String>();

		Field[] entityFields = modifiedObj.getClass().getDeclaredFields();
		// preparing sorted field value sequence map
		for (Field field : entityFields) {
			if (field.isAnnotationPresent(Auditable.class)
					&& field.getAnnotation(Auditable.class)
							.isPartOfPrimaryKey()) {
				field.setAccessible(true);
				// populating sequence value map
				valueSequenceMap.put(field.getAnnotation(Auditable.class)
						.sequence(), getFieldValue(field.get(modifiedObj)));
			}
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		for (Map.Entry<Integer, String> entry : valueSequenceMap.entrySet()) {

			if (entry.getValue() != null && entry.getValue().trim().length() > 0) {
				stringBuilder.append(entry.getValue().trim()).append(" ");
			}
		}
		return stringBuilder.toString().trim();
	}
	
	private static String getEntityprimaryKeyColumnName(Object entity) throws IllegalAccessException {
		String entityColumnName = "";
		Field[] entityFields = entity.getClass().getDeclaredFields();
		for (Field field : entityFields) {
			if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
				entityColumnName = field.getAnnotation(Column.class) != null ? field.getAnnotation(Column.class).name()
						: getCompositeKeyColumnNames(entity, field);
			}
		}
		return entityColumnName.toString().trim();
	}

	/**
	 * This method will be used to prepare AuditLog object from given details.
	 * 
	 * @param obj
	 * @param field
	 * @param oldValue
	 * @param modifiedValue
	 * @param primaryKeyValue
	 * @param changeReason
	 * @param maintenanceScreen
	 * @param entityId
	 * @return AuditLog
	 * @throws IllegalAccessException 
	 */
	private static AuditLog prepareAuditLogObj(Object obj, Field field,
			Object oldValue, Object modifiedValue, String primaryKeyValue,
			String changeReason, String maintenanceScreen, String entityId,String userId, String system, String changeType) throws IllegalAccessException {

		AuditLog auditLog = new AuditLog();
		// populating log id
		AuditLogId auditLogId = new AuditLogId();
		auditLogId.setTableName(obj.getClass().getAnnotation(Table.class).name());
		//composite id will be used in case of hard delete entity with embedded key
		auditLogId.setColumnName(field.getAnnotation(Column.class) != null ? field.getAnnotation(Column.class).name() : getCompositeKeyColumns(obj, field));
		auditLogId.setActualTimeStamp(new Timestamp(System.currentTimeMillis()));
		auditLogId.setPrimaryKeyValue(primaryKeyValue);

		// populating AuditLog obj
		auditLog.setId(auditLogId);
		auditLog.setPrimaryKey(entityId);
		auditLog.setChangeType(changeType);
		auditLog.setCurrentValue(getFieldValue(modifiedValue));
		auditLog.setMaintenanceScreen(maintenanceScreen);
		auditLog.setPreviousValue(getFieldValue(oldValue));
		auditLog.setSystem(system);
		auditLog.setUpdateReason(changeReason);
		auditLog.setUpdateUser(userId);
		return auditLog;
	}

	/**
	 * Method will be used to convert object value into string.
	 * 
	 * @param value
	 * @return fieldValue
	 */
	private static String getFieldValue(Object value) {
		String fieldValue = "";
		if (value != null) {
			fieldValue = String.valueOf(value);
			fieldValue = fieldValue.trim().length() == 0 ? "" : fieldValue
					.trim();
		} 
		return fieldValue;
	}

	/**
	 * This method will be used to validate audit object. All the primary fields
	 * are mandatory, failing which audit entry will not be generated.
	 * 
	 * @param auditLog
	 * @return isValid
	 */
	private static boolean validate(AuditLog auditLog) {
		boolean isValid = true;
		AuditLogId id = auditLog.getId();
		if (id == null || StringUtils.isEmpty(id.getColumnName())
				|| StringUtils.isEmpty(id.getTableName())
				|| StringUtils.isEmpty(id.getPrimaryKeyValue())
				|| StringUtils.isEmpty(auditLog.getMaintenanceScreen())
				|| auditLog.getPreviousValue().equals(auditLog.getCurrentValue())) {
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * This method will return composite key columns separated by comma ','.
	 * 
	 * @param obj
	 * @param field
	 * @return
	 * @throws IllegalAccessException
	 */
	private static String getCompositeKeyColumns(Object obj, Field field) throws IllegalAccessException {
		field.setAccessible(true);
		Object embeddedId = field.get(obj);

		StringBuilder columnList = new StringBuilder();
		Field[] embeddedIdFields = embeddedId.getClass().getDeclaredFields();
		int counter = 0;
		for (Field attribute : embeddedIdFields) {
			if (attribute.isAnnotationPresent(Column.class)) {
				attribute.setAccessible(true);
				if (counter != 0) {
					columnList.append(",");
				}
				columnList.append(attribute.getAnnotation(Column.class).name());
				counter++;
			}
		}
		return columnList.toString().trim();
	}
	
	private static String getCompositeKeyColumnNames(Object obj, Field field) throws IllegalAccessException {
		field.setAccessible(true);
		Object embeddedId = field.get(obj);

		StringBuilder columnList = new StringBuilder();
		Field[] embeddedIdFields = embeddedId.getClass().getDeclaredFields();
		int counter = 0;
		for (Field attribute : embeddedIdFields) {
			if (attribute.isAnnotationPresent(Column.class)) {
				attribute.setAccessible(true);
				if (counter != 0) {
					columnList.append(",");
				}
				columnList.append(attribute.getAnnotation(Column.class).name());
				counter++;
			}
		}
		return columnList.toString().trim();
	}
	
	
}
