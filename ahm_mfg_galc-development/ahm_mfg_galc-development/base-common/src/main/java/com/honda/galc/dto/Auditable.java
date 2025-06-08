package com.honda.galc.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>AuditLogDao Class description</h3><br>
 * This custom annotation will be used in audit framework.
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
 *         October 10, 2016
 * 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Auditable {

	/**
	 * 
	 * @return boolean flag indicating primary key for audit
	 */
	boolean isPartOfPrimaryKey() default false;

	/**
	 * It will be used while forming primary key value, should be provided if
	 * its part of primary key.
	 * 
	 * @return sequence
	 */
	int sequence() default 0;

}
