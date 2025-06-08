package com.honda.galc.service.broadcast;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;

/**
 * 
 * <h3>ListPrintDataAssembler Class description</h3>
 * <p> ListPrintDataAssembler description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Aug 26, 2013
 *
 *
 */
public class ListPrintDataAssembler implements IPrintDataAssembler{

	public byte[] assembleData(DataContainer dc) {
		
		StringBuilder buffer = new StringBuilder();
		
		List<String> attributes =  DataContainerUtil.getAttributes(dc);
			
		boolean isFirst = true;
		String delimiterStr = dc.getString(DataContainerTag.DELIMITER);
		String delimiter = StringUtils.isEmpty(delimiterStr) ? Delimiter.COMMA : delimiterStr;
		// Write ATTRIBUTE_SEPARATOR separated attribute values
		for (String attrName : attributes) {
			if(isFirst) isFirst = false;
			else buffer.append(delimiter);
			String attrValue = dc.getString(attrName);
			buffer.append(attrValue);
		}
		buffer.append(System.getProperty("line.separator"));
		return buffer.toString().getBytes();
	}

}
