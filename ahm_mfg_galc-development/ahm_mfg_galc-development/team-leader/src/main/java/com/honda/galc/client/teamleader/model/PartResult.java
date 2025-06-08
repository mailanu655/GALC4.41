package com.honda.galc.client.teamleader.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.constant.InstalledPartShipStatus;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.datacollection.BasePartResult;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>PartResult</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartResult description </p>
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
 * @author Paul Chou
 * Aug 18, 2010
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 03, 2016
 */
public class PartResult extends BasePartResult implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ManualLotControlRepairPropertyBean property;
	
	public PartResult() {
		super();
	}

	public PartResult(LotControlRule lotControlRule, ProductBuildResult installedPart) {
		super(lotControlRule, installedPart);
	}

	public PartResult(LotControlRule lotControlRule, ProcessPoint processPoint) {
		super(lotControlRule, processPoint);
	}
	
	public PartResult(LotControlRule lotControlRule, ProcessPoint processPoint, boolean headless) {
		super(lotControlRule, processPoint, headless);
	}

	public boolean isQuickFix() {
		if(quickFix == null){
			Map<String, String> quickFixMap = getProperty().getQuickFixHeadLessMap();
			
			//quick fix can be configured at part name level, example use case is Lower Block Number
			String quicFixStr = quickFixMap == null ? null : quickFixMap.get(getPartName());
			
			quickFix = StringUtils.isEmpty(quicFixStr)? getProperty().isQuickFixHeadLess() : Boolean.parseBoolean(quicFixStr);
			
			quickFix &= (lotControlRule.isNoScan() && !lotControlRule.isVerify());
		}
		return quickFix;
	}

	private ManualLotControlRepairPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class, this.processPointId);
		
		return property;
	}

	public boolean isStatusOnly() {
		return getLotControlRule().isStatusOnly();
	}
	
	public String getMeasurementResult(){
		if(!getProperty().isRepairDeviceDrivenData())
			return super.getMeasurementResult();
		else
			return getMeasurementResultRepairDeviceDrivenData();
	}

	private String getMeasurementResultRepairDeviceDrivenData() {

		int measurementCount = getEffectiveMeasurementCount();
		StringBuilder result = new StringBuilder();
		if(measurementCount > 0){
			if(buildResult != null && buildResult.getMeasurements() != null && 
					buildResult.getMeasurements().size() > 0){
				
				for( Measurement measurement: buildResult.getMeasurements()){
					if(result.length() > 0) result.append(",");
					result.append(StringUtils.isEmpty(measurement.getMeasurementName())? 
							measurement.getMeasurementValue() : measurement.getMeasurementStringValue());
				}
			} else {
				result.append("*****");
			}
		}
		return result.toString();
	
	}
	
	public String getShipStatus()
	{	
		boolean isRequiredPart=false;
		List<String> requiredPartShipProcessList=Arrays.asList(getProperty().getRequiredPartsShipProcess());
		if(requiredPartShipProcessList!=null && !requiredPartShipProcessList.isEmpty() && requiredPartShipProcessList.contains(getProcessPointId()))
		{
			List<RequiredPart> requiredPartsList = ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPoint(getProcessPointId());
			for(RequiredPart requiredPart:requiredPartsList){
				if(requiredPart.getId().getPartName().equals(partName))
				{
					isRequiredPart=true;
					break;
				}
			}
		}
		if(!lotControlRule.isPartConfirm() && !isRequiredPart){
			return InstalledPartShipStatus.OK.toString();
		}else{
			if(buildResult==null)
			{				
				return InstalledPartShipStatus.NG.toString();					
			}else
			{
				if (!(buildResult.getInstalledPartStatus().equals(InstalledPartStatus.NG) || 
						buildResult.getInstalledPartStatus().equals(InstalledPartStatus.NC)|| 
						buildResult.getInstalledPartStatus().equals(InstalledPartStatus.REMOVED)|| 
						getStatusMeasure() == MeasurementStatus.REMOVED ||
						getStatusMeasure() == MeasurementStatus.NG))
				{
					return InstalledPartShipStatus.OK.toString();
				}else{
					return InstalledPartShipStatus.NG.toString();
				}
			}
		}
	}

	/**
	 * Description
	 * 
	 * @return	boolean 
	 */
	public boolean isSubProduct() {
		return !StringUtils.isEmpty(lotControlRule.getPartName().getSubProductType());
	}

}
