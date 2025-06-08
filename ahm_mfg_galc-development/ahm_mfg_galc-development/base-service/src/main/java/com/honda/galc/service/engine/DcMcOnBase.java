package com.honda.galc.service.engine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.datacollection.DiecastDataCollector;
import com.honda.galc.service.datacollection.work.PersistenceOnWork;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
/**
 * 
 * <h3>DcMcOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DcMcOn description </p>
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
 * <TD>Jun 28, 2012</TD>
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
 * @since Jun 28, 2012
 */
public abstract class DcMcOnBase extends DiecastDataCollector{
	protected String barcodeGrade;
	protected String dcNumber;
	
	protected abstract String getDepartment();
	protected abstract boolean isCreateDefect(boolean barCodeStatus);
	 
	public boolean isBarcodeGradeOk() {
		
		String thresholdGrade = (String) PropertyService.getProperty(context.getProcessPointId(), "BARCODE_GRADE_DEFECT_THRESHOLD");
		
		if(StringUtils.isEmpty(thresholdGrade))
			throw new TaskException("Dc head on barcode threshold is not defined");
		
		return barcodeGrade.trim().compareTo(thresholdGrade.trim()) < 0;
	}
	
	public void processBarCode() {
		getLogger().info("DC:", dcNumber, " bar code grade:" + barcodeGrade);
		if(barcodeGrade == null) return;
		boolean barCodeStatus = isBarcodeGradeOk();
		ProductBuildResult result = ProductTypeUtil.createBuildResult(context.getProductType().name(), dcNumber, partName);
		result.setResultValue(barcodeGrade);
		result.setInstalledPartStatus(barCodeStatus ? InstalledPartStatus.OK : InstalledPartStatus.NG);
		result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		saveBuildResult(result);
		
		if(isCreateDefect(barCodeStatus)){
			List<ProductBuildResult> buildResults = new ArrayList<ProductBuildResult>();
			result.setPartName(getDepartment() + " " + context.getProductType().toString());
			buildResults.add(result); 
			getQicsService().update(context.getProcessPointId(), context.getProduct(), buildResults);
		}
	}
	

	protected String getModelCode() {
		//model code from PLC
		String modelCode = (String)context.get(TagNames.MODEL_CODE.name());
		getLogger().info("Input model code:" + modelCode);
		return modelCode;
	}
	
	protected void addPersistenceWork() {
		addwork(new PersistenceOnWork(context, this));
	}
	
	
}
