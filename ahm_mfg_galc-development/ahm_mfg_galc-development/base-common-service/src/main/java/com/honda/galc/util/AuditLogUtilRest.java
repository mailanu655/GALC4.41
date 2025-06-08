package com.honda.galc.util;

import com.honda.galc.data.AuditLogConstants;

public class AuditLogUtilRest extends AuditLoggerUtil {

	public static void logInsert(Object newObj, String userId, String changeType) {
		logAuditInfo(null, newObj, AuditLogConstants.INSERT, newObj.getClass().getSimpleName(),userId, 
				      AuditLogConstants.GALC, changeType);
	}
	
	public static void logUpdate(Object oldObj, Object newObj, String userId, String changeType) {
		logAuditInfo(oldObj, newObj, AuditLogConstants.UPDATE, newObj.getClass().getSimpleName(),userId, 
				      AuditLogConstants.GALC, changeType);
	}
	
	public static void logDelete(Object oldObj, String userId, String changeType) {
		logAuditInfo(oldObj, null, AuditLogConstants.DELETE, oldObj.getClass().getSimpleName(),userId, 
				      AuditLogConstants.GALC, changeType);
	}	

}
