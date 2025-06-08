package com.honda.galc.service.qics;

import static com.honda.galc.service.ServiceFactory.getDao;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiExternalSystemDataDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dto.qi.QiCreateDefectDto;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qi.QiAppliedRepairMethodId;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiRepairResult;

public class QiExternalDefectDataHelper {
	
	private static final String IN_LINE_REPAIR_METHOD = "IN LINE REPAIR";
	private static final short DEFAULT_PROD_LINE_NO = 1;
	public static boolean validateDefectData(QiCreateDefectDto dto) {

		if( StringUtils.isBlank(dto.getProcessPointId()) || dto.getProcessPointId().trim().length() > 17)  {
			return false;
		}		
		if(StringUtils.isBlank(dto.getProductType()) || dto.getProductType().trim().length() > 10 || !StringUtils.isAlphaSpace(dto.getProductType()))  {
			return false;
		}		
		if(StringUtils.isBlank(dto.getEntryDepartment()) || dto.getEntryDepartment().trim().length() > 32 || !StringUtils.isAlphanumericSpace(dto.getEntryDepartment()))  {
			return false;
		}		
		if(StringUtils.isBlank(dto.getEntrySite()) || dto.getEntrySite().trim().length() > 16 || !StringUtils.isAlphanumericSpace(dto.getEntrySite()))  {
			return false;
		}		
		if(dto.getOriginalDefectStatus() == null)  {
			return false;
		}		
		if(dto.getCurrentDefectStatus() == null)  {
			return false;
		}		
		if(StringUtils.isBlank(dto.getWriteupDepartment()) || dto.getWriteupDepartment().trim().length() > 32 || !StringUtils.isAlphanumericSpace(dto.getWriteupDepartment()))  {
			return false;
		}		
		if(StringUtils.isBlank(dto.getAssociateId()) || dto.getAssociateId().trim().length() > 11 || !StringUtils.isAlphanumericSpace(dto.getAssociateId()))  {
			return false;
		}		
		if(dto.getExternalSystemKey() == null)  {
			return false;
		}

		return true;
	}

	public static boolean validateKeyFields(QiCreateDefectDto dto)  {
		if (StringUtils.isBlank(dto.getExternalSystemName())
				|| StringUtils.isBlank(dto.getExternalPartCode())
				|| StringUtils.isBlank(dto.getExternalDefectCode())
				|| StringUtils.isBlank(dto.getProductId()))  {
			return false;
		}
		else if(dto.getExternalSystemName().trim().length() > 18)  { 
			return false;
		}		
		else if(dto.getExternalPartCode().trim().length() > 64)  {
			return false;
		}			
		if(dto.getExternalDefectCode().trim().length() > 64)  {
			return false;
		}		
		if(dto.getProductId().trim().length() > 255)  {
			return false;
		}		
		return true;
	}
	
	public static boolean isExternalSystemDataExist(QiExternalSystemData qiExternalSystemData) {
		QiExternalSystemData systemData = getDao(QiExternalSystemDataDao.class).findByKey(qiExternalSystemData.getId());		
		return (systemData != null);
	}
	
	public static short getProductionLineNo(String processPointId)  {		
		if(StringUtils.isBlank(processPointId))  return DEFAULT_PROD_LINE_NO;
		short prodLineNo = DEFAULT_PROD_LINE_NO;
		try {
			ProcessPoint pp = getDao(ProcessPointDao.class).findById(processPointId);
			if(pp != null)  {
				GpcsDivision gpcs = getDao(GpcsDivisionDao.class).findByKey(pp.getDivisionId());
				if(gpcs != null)  {
					prodLineNo = Short.parseShort(gpcs.getGpcsLineNo());
				}
			}
		} catch (Exception e) {
			prodLineNo = 1;
			Logger.getLogger().error(e, "Exception when getting Production Line No");

		}
		return prodLineNo;
	}
	
    public static QiRepairResult createRepairResultFromDefect(QiDefectResult defect, QiRepairDefectDto dto)  {
		QiRepairResult qiRepairResult = new QiRepairResult(defect); 

		qiRepairResult.setEntrySiteName(defect.getEntrySiteName());
		qiRepairResult.setEntryPlantName(defect.getEntryPlantName());
		if(!StringUtils.isBlank(dto.getEntryDept()))  {
			qiRepairResult.setEntryDept(dto.getEntryDept().toUpperCase());		
		} else  {
			qiRepairResult.setEntryDept(defect.getEntryDept());
		}
		if(!StringUtils.isEmpty(dto.getProcessPointId()))  {
			qiRepairResult.setApplicationId(dto.getProcessPointId());			
		}
		else  {
			qiRepairResult.setApplicationId(defect.getApplicationId());
		}
		short lineNo = QiExternalDefectDataHelper.getProductionLineNo(qiRepairResult.getApplicationId());
		qiRepairResult.setEntryProdLineNo(lineNo);
		if(!StringUtils.isBlank(dto.getAssociateId()))  {
			qiRepairResult.setCreateUser(dto.getAssociateId().toUpperCase());			
		}
		else  {
			qiRepairResult.setCreateUser(defect.getCreateUser());
		}
		short maxSeq = (short)getDao(QiRepairResultDao.class).findMaxSequenceByDefectResultId(defect.getDefectResultId());
		qiRepairResult.setActualProblemSeq((short)(maxSeq+1)); //set a sequence number for the actual problem
		qiRepairResult.setCurrentDefectStatus((short)dto.getCurrentDefectStatus());
		qiRepairResult.setDefectTransactionGroupId(defect.getDefectTransactionGroupId());
		return qiRepairResult;
    }
    
    public static QiAppliedRepairMethod createRepairMethod(QiRepairResult repairResult, QiRepairDefectDto dto)  {
		boolean isFixed = repairResult.getCurrentDefectStatus() == DefectStatus.FIXED.getId();
		Integer count = getDao(QiAppliedRepairMethodDao.class).findCurrentSequence(repairResult.getRepairId());
		count = count == null ? 0 : count;
		count++;
		QiAppliedRepairMethod qiAppliedRepairMethod = new QiAppliedRepairMethod();
		QiAppliedRepairMethodId id = new QiAppliedRepairMethodId();
		qiAppliedRepairMethod.setId(id);
		qiAppliedRepairMethod.getId().setRepairMethodSeq(count);
		qiAppliedRepairMethod.getId().setRepairId(repairResult.getRepairId());
		qiAppliedRepairMethod.setRepairMethod(IN_LINE_REPAIR_METHOD);
		qiAppliedRepairMethod.setComment("In line repair");
		qiAppliedRepairMethod.setCreateUser(repairResult.getCreateUser());
		if(dto != null)  {
			if(!StringUtils.isBlank(dto.getRepairMethod()))  {
				qiAppliedRepairMethod.setRepairMethod(dto.getRepairMethod());
			}
			if(!StringUtils.isBlank(dto.getRepairReason()))  {
				qiAppliedRepairMethod.setComment(dto.getRepairReason());
			}
			if(!StringUtils.isBlank(dto.getAssociateId()))  {
				qiAppliedRepairMethod.setCreateUser(dto.getAssociateId());
			}
		}
		qiAppliedRepairMethod.setApplicationId(repairResult.getApplicationId());
		qiAppliedRepairMethod.setRepairTime(1);
		qiAppliedRepairMethod.setIsCompletelyFixed(isFixed ? 1 : 0);
		qiAppliedRepairMethod.setProductionDate(repairResult.getProductionDate());
		qiAppliedRepairMethod.setShift(repairResult.getShift());
		qiAppliedRepairMethod.setTeam(repairResult.getTeam());
		qiAppliedRepairMethod.setRepairTimestamp(getDao(QiAppliedRepairMethodDao.class).getDatabaseTimeStamp());
		return qiAppliedRepairMethod;
    }

}
