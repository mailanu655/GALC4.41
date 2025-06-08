package com.honda.galc.service.qics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.InstalledPartHelper;
import com.honda.galc.service.utils.QiHeadlessHelper;

/**
 * 
 * <h3>QicsUpdaterProduct</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsUpdaterProduct description </p>
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
 * <TD>May 11, 2011</TD>
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
 * @since May 11, 2011
 * @param <T>
 */

public abstract class QicsUpdaterProduct<T extends BaseProduct> extends QicsUpdaterBase<T>{
	
	@Override
	protected List<DefectResult> createDefect(String processPointId, ProductBuildResult result) {
		return createDefectForInstalledPart(processPointId, (InstalledPart)result);
	}
	
	protected List<DefectResult> createDefectForInstalledPart(
			String processPointId, InstalledPart part) {
		List<DefectResult> defects = new ArrayList<DefectResult>();
		QiHeadlessHelper qiHelper = new QiHeadlessHelper(property);
		if(part.getInstalledPartStatus() != InstalledPartStatus.OK){
			
			if(!part.isValidPartSerialNumber()){
				DefectResult thisDefectResult = createPartDefect(processPointId,part);
				if(thisDefectResult != null)  {
					thisDefectResult.setQicsDefect(part.isQicsDefect());
					defects.add(thisDefectResult);
					if(qiHelper.isSendToNAQics())  {
						IdCreate newId = ServiceFactory.getDao(IdCreateDao.class).incrementIdWithNewTransaction("BuildResult", "DEFECT_REF_ID");
						long defectRefId = newId.getCurrentId();
						part.setDefectRefId(defectRefId);
						ServiceFactory.getDao(InstalledPartDao.class).save(part);
						thisDefectResult.setDefectRefId(defectRefId);
						String errorCode = String.valueOf(InstalledPartStatus.NG.getId());
						thisDefectResult.setErrorCode(errorCode);
					}
				}
			} 
			
			if(part.getMeasurements() != null && part.getMeasurements().size() > 0)
			{
				for(int i = 0; i < part.getMeasurements().size(); i++){
					if(part.getMeasurements().get(i).getMeasurementStatus() != MeasurementStatus.OK)  {
						Measurement m = part.getMeasurements().get(i);
						DefectResult thisDefectResult = createTorqueDefect(processPointId, part, part.getMeasurements().get(i));
						thisDefectResult.setQicsDefect(part.isQicsDefect());
						defects.add(thisDefectResult);//torque defect location start from 1
						if(qiHelper.isSendToNAQics())  {
							IdCreate newId = ServiceFactory.getDao(IdCreateDao.class).incrementIdWithNewTransaction("BuildResult", "DEFECT_REF_ID");
							long defectRefId = newId.getCurrentId();
							m.setDefectRefId(defectRefId);
							ServiceFactory.getDao(MeasurementDao.class).save(m);
							thisDefectResult.setDefectRefId(defectRefId);
							String seqNo = (String.valueOf(m.getId().getMeasurementSequenceNumber()));
							thisDefectResult.setErrorCode(seqNo);
						}
					}
				}
			}
			
			if(defects.size() == 0){
				//Nothing wrong with PSN and Torque, this is the case Only product Id and status passed in
				DefectResult thisDefectResult = createForProduct(processPointId, part);
				defects.add(thisDefectResult);
				if(qiHelper.isSendToNAQics())  {
					IdCreate newId = ServiceFactory.getDao(IdCreateDao.class).incrementIdWithNewTransaction("BuildResult", "DEFECT_REF_ID");
					long defectRefId = newId.getCurrentId();
					part.setDefectRefId(defectRefId);
					ServiceFactory.getDao(InstalledPartDao.class).save(part);
					thisDefectResult.setDefectRefId(defectRefId);
					thisDefectResult.setErrorCode("0");
				}
			}
		}
		
		getLogger().info("new defects: ", defects.toString());
		
		return defects;
	}
	
	private DefectResult createForProduct(String processPointId, InstalledPart part) {
		DefectResultId id = createDefectResultId(processPointId, part);
		id.setDefectTypeName(getDefectTypeName(part,null));
		id.setInspectionPartLocationName(getInspectionPartLocationName(part, null));
		
		DefectResult defect = new DefectResult();
		defect.setId(id);
		defect.setNewDefect(true);
		defect.setQicsDefect(part.isQicsDefect());
		
		setDefectProperties(defect, part);
		
		return defect;
	}

	private DefectResult createPartDefect(String processPointId, InstalledPart part) {
		DefectResultId id = createDefectResultId(processPointId, part);
		id.setDefectTypeName(getDefectTypeName(part,InstalledPartStatus.NG.toString()));
		id.setInspectionPartLocationName(getInspectionPartLocationName(part, "SN"));
		
		DefectResult defect = new DefectResult();
		defect.setId(id);
		defect.setNewDefect(true);
		defect.setQicsDefect(part.isQicsDefect());
		defect.setErrorCode(part.getErrorCode());
		
		defect.setDefectStatus(getDefectStatus(part));
		setDefectProperties(defect, part);
		
		return defect;
	}
	
	private DefectResult createTorqueDefect(String processPointId, InstalledPart part, Measurement measurement) {
		DefectResultId id = createDefectResultId(processPointId, part);
		id.setDefectTypeName(getDefectTypeName(measurement));
		id.setInspectionPartLocationName(getInspectionPartLocationName(part, measurement.getDefectLocation()));
		DefectResult defect = new DefectResult();
		defect.setId(id);
		defect.setNewDefect(true);
		defect.setQicsDefect(part.isQicsDefect());
		defect.setErrorCode(measurement.getErrorCode());
		
		setDefectProperties(defect, part);
		
		return defect;
	}

	

}
