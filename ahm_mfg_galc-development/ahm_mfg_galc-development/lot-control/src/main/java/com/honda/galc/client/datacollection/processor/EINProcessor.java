package com.honda.galc.client.datacollection.processor;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.ui.component.Constant;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.InstalledPart;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * SR27623 Processor for EIN and VIN mapping verification
 * <h4>Usage and Example</h4>
 *
 * <h4>Special Notes</h4>
 * Compare the Engine Serial No of the VIN to the EIN scanned
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>2013.07.02</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see
 * @ver 0.1
 * @author YX
 */
public class EINProcessor extends PartSerialNumberProcessor {

	public EINProcessor(ClientContext context) {
		super(context);
	}

	//check whether the Engine Serial No of the VIN is equaled to the EIN scanned
	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		if(partnumber == null || partnumber.getPartSn() == null || StringUtils.isEmpty(partnumber.getPartSn().trim())){
			handleException("Received EIN is null!");
		}
		
		//set up installed Part
		installedPart.setPartSerialNumber(partnumber.getPartSn());
		//compare serial number
		confirmEngineSerialNumber(installedPart.getPartSerialNumber());
		return true;
	}
	
	protected void confirmEngineSerialNumber(String scannedEIN) {
		ProductBean product = ((DataCollectionState) getController().getState()).getProduct();
		String expectedEIN = getPartEIN(product);
		
		//check the scanned engine matches with the actual married engine. 
		if(scannedEIN.trim().equals(expectedEIN.trim())){
			getController().getFsm().partSnOk(installedPart);
		} else {
			String userMsg = "Engine not matched: Expected EIN [" + expectedEIN + "] Scanned EIN[" + installedPart.getPartSerialNumber() + "]";
			handleException(userMsg);
		}
	}
	
	//get Product's expected EIN
	private String getPartEIN(ProductBean product) {
		String expectedEIN= "";
		
		//The expected EIN is stored as an InstalledPart with part name "EIN", see more details in FrameVinDetailProcessor.java
		for(InstalledPart part: product.getPartList()) {
			if(Constant.VERIFY_EIN_PART_NAME.equals(part.getPartName())) {
				expectedEIN = part.getPartSerialNumber();
				break;
			}
		}
		return (expectedEIN==null?"":expectedEIN); 
	}
	

}
