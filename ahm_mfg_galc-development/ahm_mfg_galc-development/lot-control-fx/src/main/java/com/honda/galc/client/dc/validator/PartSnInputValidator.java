package com.honda.galc.client.dc.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

/**
 * @author Subu Kathiresan
 * @date Jun 25, 2014
 */
public class PartSnInputValidator implements IPartSnInputValidator {

	protected PartSerialScanData partSn;
	
	public PartSnInputValidator(PartSerialScanData partSn) {
		this.partSn = partSn;
	}
			
	public boolean validate(String productId, PartSerialScanData partSerialNumber, MCOperationPartRevision part) {
		
		boolean isValid = true;
		
		//TODO add additional validations
		isValid &= isDuplicatePart(productId, partSerialNumber, part);
		isValid &= isMatchingPartMask(partSerialNumber, part);
		
		return isValid;
	}
	

	/**
	 * Check if the same part has installed on other product.
	 * Check against installed part database table
	 * @param partnumber 
	 * @return
	 */
	private boolean isDuplicatePart(String productId, PartSerialScanData partSerialNumber, MCOperationPartRevision part) {
		if(StringUtils.isEmpty(partSerialNumber.toString())) return false;
		// TODO check duplicate part in cache if required
		
		try {
			List<InstalledPart> installedPartList = ServiceFactory.getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(
					part.getId().getOperationName(), partSerialNumber.toString());
			return (createDuplicatePartList(productId, installedPartList).size() == 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	private List<String> createDuplicatePartList(String productId, List<InstalledPart> installedPartList) {
		List<String> duplicateList = new ArrayList<String>();
		if(installedPartList == null) return duplicateList;
		
		for(InstalledPart ip : installedPartList) {
			if(ip != null && !(ip.getId().getProductId().equals(productId)))
				duplicateList.add(ip.getId().getProductId());
		}
		return duplicateList;
	}
	
	private boolean isMatchingPartMask(PartSerialScanData partSerialNumber, MCOperationPartRevision part) {
//		List<PartSpec> partSpecs = getController().getState().getCurrentLotControlRulePartList();
//		Iterator<PartSpec> iter = partSpecs.iterator();
//		PartSpec partSpec = null;
//		List<String> masks = new ArrayList<String>();
//		partIdx  = 0;
//		while(iter.hasNext()){
//			partSpec = iter.next();
//			if(CommonPartUtility.verification(installedPart.getPartSerialNumber(), partSpec.getPartSerialNumberMask(),
//					PropertyService.getPartMaskWildcardFormat()))
//			{
//				installedPart.setPartIndex(partIdx);
//					installedPart.setValidPartSerialNumber(true);
//				installedPart.setPartId(partSpec.getId().getPartId());
//				return;
//			}
//			partIdx++;
//			masks.add(partSpec.getPartSerialNumberMask());
//		}
//		handleException("Part serial number:" + installedPart.getPartSerialNumber() + 
//				" verification failed. Masks:" + masks.toString());
		return CommonPartUtility.verification(partSerialNumber.getSerialNumber(), part.getPartMask(), PropertyService.getPartMaskWildcardFormat());
	}
}
