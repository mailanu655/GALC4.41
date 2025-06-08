package com.honda.galc.client.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;

/**
 * 
 * <h3>IpuDataParser Class description</h3>
 * <p> IpuDataParser description </p>
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
 * Nov 30, 2011
 *
 *
 */
public class IpuDataParser {
	
	public static List<InstalledPart> parseDataOfLine(String dataStr,String productId) {
		
		List<InstalledPart>  parts = new ArrayList<InstalledPart>();
		
		
		if(StringUtils.isEmpty(dataStr)) return parts;
		String[] items = dataStr.split(" " );
		if(items.length <= 1) return parts;
		for(String item : items) {
			InstalledPart part = new InstalledPart();
			InstalledPartId id = new InstalledPartId();
			id.setPartName(item);
			id.setProductId(productId);
			part.setId(id);
			parts.add(part);
		}
		return parts;
	
		
	}
	
           	
	public static List<InstalledPart> parseDataOfTester(String dataStr,String productId) {
		
		List<InstalledPart>  parts = new ArrayList<InstalledPart>();
		
		if(StringUtils.isEmpty(dataStr)) return parts;
		String[] items = dataStr.split(" " );
		if(items.length <= 1) return parts;
		for(String item : items) {
			InstalledPart part = new InstalledPart();
			InstalledPartId id = new InstalledPartId();
			id.setPartName(item);
			id.setProductId(productId);
			part.setId(id);
			parts.add(part);
		}
		return parts;
		
	}
}
