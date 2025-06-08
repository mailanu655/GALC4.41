package com.honda.galc.service.broadcast;

import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.product.Template;


/**
 * 
 * <h3>JasperPrintDataAssembler Class description</h3>
 * <p>
 * JasperPrintDataAssembler description
 * </p>
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
 * @author Alex Johnson<br>
 * Aug 4, 2016
 * 
 * 
 */
public class CompiledJasperPrintDataAssembler extends JasperPrintDataAssembler {

	public CompiledJasperPrintDataAssembler(Template template) {
		super(template);
	}

	public byte[] assembleData(DataContainer dc) {
		return null;
	}
}