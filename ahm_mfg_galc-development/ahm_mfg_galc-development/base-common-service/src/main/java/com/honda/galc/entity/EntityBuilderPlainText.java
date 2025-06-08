package com.honda.galc.entity;

import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>EntityBuilderForFile</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntityBuilderForFile description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Feb 24, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 24, 2015
 */
public class EntityBuilderPlainText extends EntityBuilder{

	public EntityBuilderPlainText(EntityBuildPropertyBean propertyBean, Logger logger) {
		super(propertyBean, logger);
	}
	
	public void readLine(String currentLine) {
		fieldsMap.clear();
		emitFields(currentLine);
		extractGalcProperties();
		processExpression();
		
		logger.debug(getFieldsLogInfo());
	}

}
