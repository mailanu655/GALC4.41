 
package com.honda.galc.service.lotcontrol;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.rest.RepairDefectDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.LotControlRepairService;
import com.honda.galc.service.utils.ProductTypeUtil;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LotControlRepairServiceImpl implements LotControlRepairService {
	
	public LotControlRepairServiceImpl() {
		super();
	}

	@Override
	public boolean repairBuildResult(RepairDefectDto dto)  {
		boolean resp = false;
		ProductBuildResult installedPart = null;
		ProductBuildResultDao<? extends ProductBuildResult , ?> myDao = null;
		
		ProductType pType = ProductType.getType(dto.getProductType());
		if(pType != null)  {
			myDao = ProductTypeUtil.getTypeUtil(pType).getProductBuildResultDao();
		}
		//external system key must not be 0, can be presumably negative
		if (dto == null || dto.getExternalSystemKey() == 0 || myDao == null) {
    		return false;
		}
		try {
			installedPart = myDao.findByRefId(dto.getExternalSystemKey());
			if(installedPart != null)  {  //means build ref id was found in InstalledPart table
				updateInstalledPart(installedPart, dto, pType);
	    		//if it came here, defect was found and repaired
				resp = true;
			}
			else  {
				Measurement meas = null;
				if(dto.getExternalSystemKey() > 0)  {
					meas = getDao(MeasurementDao.class).findByRefId(dto.getExternalSystemKey());
				}
				if(meas != null)  { //means build ref id was found in Measurement table
			    	updateMeasurement(meas, dto);
			    	if(!isFixed(dto.getCurrentDefectStatus()) || areAllMeasurementsRepaired(meas.getId().getProductId(), meas.getId().getPartName()))  {
						InstalledPart iPart = getDao(InstalledPartDao.class).findById(meas.getId().getProductId(), meas.getId().getPartName());
						if(iPart != null)  {
							updateInstalledPart(iPart, dto, pType);
						}
			    	}
		    		//if it came here, defect was found and repaired
					resp = true;
				}
				else  {  //neither a measurement nor installed part matched build ref id
					resp = false;
				}
			}
		} catch (Exception e) {
			getLogger().error(e, "LotControlService: headless lot control repair");
			resp = false;
		}
    	return resp;
    }
    
    private boolean isFixed(short defectStatus)  {
    	return defectStatus == DefectStatus.REPAIRED.getId() || defectStatus == DefectStatus.FIXED.getId();
    }
    
    void updateInstalledPart(ProductBuildResult iPart, RepairDefectDto dto, ProductType pType)  {
		ProductBuildResultDao<? extends ProductBuildResult , ?> myDao = null;
		myDao = ProductTypeUtil.getTypeUtil(pType).getProductBuildResultDao();
    	iPart.setAssociateNo(dto.getAssociateId());
    	if(isFixed(dto.getCurrentDefectStatus()))  {
    		iPart.setInstalledPartStatus(InstalledPartStatus.OK);
    	}
    	else  {
    		iPart.setInstalledPartStatus(InstalledPartStatus.NG);
    	}
    	if(iPart instanceof InstalledPart)  {
    		((InstalledPart)iPart).setInstalledPartReason(dto.getRepairReason());
    	}
    	iPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
    	List<ProductBuildResult> buildResultList = new ArrayList<ProductBuildResult>();
    	buildResultList.add(iPart);
		myDao.saveAllResults(buildResultList);    	
    }
    
    private void updateMeasurement(Measurement meas, RepairDefectDto dto)  {
    	if(isFixed(dto.getCurrentDefectStatus()))  {
    		meas.setMeasurementStatus(MeasurementStatus.OK);
    	}
    	else  {
    		meas.setMeasurementStatus(MeasurementStatus.NG);
    	}
    	meas.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
    	meas.setMeasurementAngle(0);
    	meas.setMeasurementValue(0);
		getDao(MeasurementDao.class).save(meas);
    }
    
	public boolean areAllMeasurementsRepaired(String productId, String partName) {
		List<Measurement> allMeasurements = getDao(MeasurementDao.class).findAll(productId, partName);
		if(allMeasurements != null && allMeasurements.size() > 0 ){
			for(Measurement measurement : allMeasurements){
				if(measurement.getMeasurementStatus() != MeasurementStatus.OK)
				{
					return false;
				}
			}
		}
		
		return true;
		
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());
	}

}