package com.honda.galc.client.teamleader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotCtrRepairUtil;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.constant.InstalledPartShipStatus;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.datacollection.BasePartResult;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class PartResult extends BasePartResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean stopShipOnly = false;
	protected String partDesc;
	protected String partMask;
	protected String operationType;
	protected String operationDesc;
	private ManualLotControlRepairPropertyBean property;
	
	public PartResult() {
		super();
	}

	public PartResult(LotControlRule lotControlRule, InstalledPart installedPart) {
		super(lotControlRule, installedPart);
	}

	public PartResult(LotControlRule lotControlRule, ProcessPoint processPoint) {
		super(lotControlRule, processPoint);
	}

	public boolean isStatusOnly() {
		return getLotControlRule().isStatusOnly();
	}

	public String getMeasurementResult() {

		StringBuilder result = new StringBuilder();

		if (buildResult != null && buildResult.getMeasurements() != null && buildResult.getMeasurements().size() > 0) {
			for (Measurement measurement : buildResult.getMeasurements()) {
				if (result.length() > 0)
					result.append(",");
				result.append(measurement.getMeasurementValue());
			}
		} else {
			result.append("*");
		}

		return result.toString();
	}

	/**
	 * This method is used to find measurement status for operations
	 * @param operation
	 * @return MeasurementStatus
	 */
	public MeasurementStatus getBuildResultStatusMeasure(MCOperationRevision  operation  ) {
		if (buildResult != null){
			InstalledPart part = (InstalledPart) buildResult;
			if(InstalledPartStatus.NC.getId() == part.getInstalledPartStatus().getId()) return null;
			
			if(ManualLotCtrRepairUtil.hasOnlyInstalledPart(getOperationType()) ){
				if(part.isStatusOk()) return MeasurementStatus.OK;
				else return MeasurementStatus.NG;
			}
			if(operation != null){
				//checks measurement for part for which data is collected.
				for(MCOperationPartRevision  mcOperationPartRev : operation.getParts()){
					if (getInstalledPart().getPartId().equalsIgnoreCase(mcOperationPartRev.getId().getPartId())){
						if((findGoodMeasurements().size() == mcOperationPartRev.getMeasurements().size())){
							return MeasurementStatus.OK;
						}else{
							return MeasurementStatus.NG;
						}
					}
				}
				
			}
		}
		return null;
	}
	
	/**
	 * This method is used to find measurement status for NON GALC Part data
	 * @param lotControlRule
	 * @return  MeasurementStatus
	 */
	public MeasurementStatus getBuildResultStatusMeasure(LotControlRule  lotControlRule  ) {
		if (buildResult != null){
			InstalledPart part = (InstalledPart) buildResult;
			if(InstalledPartStatus.NC.getId() == part.getInstalledPartStatus().getId()) return null;
			
			if(ManualLotCtrRepairUtil.hasOnlyInstalledPart(getOperationType()) ){
				if(part.isStatusOk()) return MeasurementStatus.OK;
				else return MeasurementStatus.NG;
			}
			if(lotControlRule != null){
				//checks measurement for part for which data is collected.
					for(PartSpec  partSpec : lotControlRule.getParts()){
						if (getInstalledPart().getPartId().equalsIgnoreCase(partSpec.getId().getPartId())){
							if(findGoodMeasurements().size() == partSpec.getMeasurementSpecs().size()){
								return MeasurementStatus.OK;
							}else{
								return MeasurementStatus.NG;
							}
						}
					}
					
				}
		}
		return null;
	}

	
	public List<Measurement> findGoodMeasurements(){
		List<Measurement> measurements = getInstalledPart().getMeasurements();
		List<Measurement>  goodMeasurements = new ArrayList<Measurement>() ;
		for(Measurement goodMeas:measurements){
			//checks measurement for Measurement Status is OK
			if(goodMeas.getMeasurementStatus().equals(MeasurementStatus.OK))
					goodMeasurements.add(goodMeas);
		}
		return goodMeasurements;
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

	private ManualLotControlRepairPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class, this.processPointId);
		
		return property;
	}

	public boolean isStopShipOnly() {
		return stopShipOnly;
	}

	public void setStopShipOnly(boolean stopShipOnly) {
		this.stopShipOnly = stopShipOnly;
	}

	public String getPartDesc() {
		return partDesc;
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}
	
	
	public String getPartMask() {
		return partMask;
	}

	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}


}
