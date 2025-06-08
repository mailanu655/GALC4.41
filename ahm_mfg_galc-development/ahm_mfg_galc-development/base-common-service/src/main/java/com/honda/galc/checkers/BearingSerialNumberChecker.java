package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.bearing.BearingMatrixDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingMatrixId;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

public class BearingSerialNumberChecker extends AbstractBaseChecker<PartSerialScanData> {

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Part;
	}

	public int getSequence() {
		return 0;
	}


	@Override
	public List<CheckResult> executeCheck(PartSerialScanData partSerialScanData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		//get all conrods for  that part from common name and validate particular index of that 
		if(partSerialScanData == null || StringUtils.isBlank(partSerialScanData.getSerialNumber())){
			Logger.getLogger().info("Unit Part Serial Check Failed - Part Serial number is empty or null : "+partSerialScanData);
			checkResults.add(createCheckResult("Unit Part Serial Check Failed - Part Serial number is empty or null "));
			return checkResults;
		} else {
			BearingMatrix bearingMatrix = getBearingMatrix(partSerialScanData);
			
			String viosPlatformId = partSerialScanData.getPartName().substring(partSerialScanData.getPartName().indexOf(Delimiter.UNDERSCORE)+1);
			
			//Map<String, MCViosMasterOperation> mapOfUnits = ServiceFactory.getDao(MCViosMasterOperationDao.class).findAllUnitbyCommonName(partSerialScanData.getPartName().
			//		substring(partSerialScanData.getPartName().indexOf(Delimiter.UNDERSCORE)+1), bearingMatrix.getNumberOfConrods(), false);
			
			List<MCViosMasterOperation> listOfUnits = ServiceFactory.getDao(MCViosMasterOperationDao.class).findAllUnitbyCommonName(viosPlatformId, ApplicationConstants.CONROD_, bearingMatrix.getNumberOfConrods());
			
			String partSerialNumberChar = "";
			String[] weight;
			int startIndex = 0;
			int endIndex = 0;
			if(!StringUtil.isNullOrEmpty(bearingMatrix.getConrodWeightIndex())) {
				weight = bearingMatrix.getConrodWeightIndex().split(Delimiter.COMMA);
				startIndex = Integer.parseInt(weight[0])-1;
				endIndex = startIndex+ Integer.parseInt(weight[1]);
				partSerialNumberChar= partSerialScanData.getSerialNumber().substring(startIndex,endIndex);
			
				
				for (int i = 1; i <= listOfUnits.size(); i++) {
					MCViosMasterOperation viosMasterOP = listOfUnits.get(i - 1);
					if(viosMasterOP != null){
						
						InstalledPart part = ServiceFactory.getDao(InstalledPartDao.class).findByKey(new InstalledPartId(partSerialScanData.getProductId(), viosMasterOP.getId().getOperationName()));
						if(part != null) {
							String partSerialNumberChartemp = "";
							if( part.getPartSerialNumber() != null)
								partSerialNumberChartemp = part.getPartSerialNumber().substring(startIndex,endIndex);
							
							if(!partSerialNumberChar.equals(partSerialNumberChartemp)) {
								checkResults.add(createCheckResult("Index of Serial numbers not matching with other CONROD unit serial number."));
								break;
							}
						}
					}
				}
			}
		}
	    return checkResults;
	}	
	
	protected BearingMatrix getBearingMatrix(PartSerialScanData partSerialScanData) {
	    BearingMatrixDao bearingMatrixDao = ServiceFactory.getDao(BearingMatrixDao.class);
	    BearingMatrixId bearingMatrixId = new BearingMatrixId();
	    String modelYearCode = ProductSpec.extractModelYearCode(partSerialScanData.getProductSpecCode());
	    bearingMatrixId.setModelYearCode(modelYearCode);
	    bearingMatrixId.setModelCode(partSerialScanData.getModelCode());
	    return bearingMatrixDao.findByKey(bearingMatrixId);
	}

}