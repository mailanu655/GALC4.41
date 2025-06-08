package com.honda.galc.client.datacollection.strategy;

import java.util.List;

import com.honda.galc.client.common.util.IpuDataParser;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.entity.product.InstalledPart;
/**
 * 
 * <h3>IpuDataOfTesterStrategy Class description</h3>
 * <p> IpuDataOfTesterStrategy description </p>
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
 * Nov 29, 2011
 *
 *
 */
public class IpuDataOfTesterStrategy extends IpuAbstractStrategy {

	public IpuDataOfTesterStrategy(ClientContext context) {
		super(context);
		
	}
	
	@Override
	public List<InstalledPart> parseIpuData(String dataStr) {
		return IpuDataParser.parseDataOfTester(dataStr, getController().getState().getProductId());
	}

}
