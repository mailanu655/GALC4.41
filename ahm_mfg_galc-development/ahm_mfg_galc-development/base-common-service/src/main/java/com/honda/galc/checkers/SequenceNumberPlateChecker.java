package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.property.PartCheckerPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class SequenceNumberPlateChecker extends AbstractBaseChecker<PartSerialScanData> {
	
	private PartCheckerPropertyBean propertyBean;

	
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Part;
	}
	
	public int getSequence() {
		return 0;
	}
	
	public List<CheckResult> executeCheck(PartSerialScanData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		
		String vin = inputData.getProductId();
		Integer scannedSequence = Integer.valueOf( inputData.getSerialNumber());
		if (StringUtils.isBlank(vin)){
			checkResults.add(createCheckResult("Sequence number Check Failed - No VIN scanned."));
			return checkResults;
		}
		Frame frame = null;
		if (vin != null){
			frame = ServiceFactory.getDao(FrameDao.class).findByKey(vin);	//get Frame object from 143TBX
		}
		if(frame == null){
			checkResults.add(createCheckResult("Sequence number Check Failed - Was not able to find frame information associated with VIN: " + vin));
			return checkResults;
		}
		FrameSpec spec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
		if(spec == null){
			checkResults.add(createCheckResult("Sequence number Check Failed - Was not able to find frame spec information associated with VIN: " + vin));
			return checkResults;
		}
		
		String modelCodeAndYear = ""; //currentFrameSpec.getModelYearCode()+currentFrameSpec.getModelCode()+currentFrameSpec.getModelTypeCode();
		propertyBean = PropertyService.getPropertyBean(PartCheckerPropertyBean.class);
		
		List<String> uniqueModels=Arrays.asList(propertyBean.getUniqueModelTypeYear());
		//For NSX, This should be T6N
		if(uniqueModels.contains(spec.getModelCode())) {
			modelCodeAndYear = spec.getModelCode();
		}//for PMC Edition Models..L3S2/L3S4
		else if(uniqueModels.contains(spec.getModelYearCode()+spec.getModelCode())) {
			modelCodeAndYear = spec.getModelYearCode()+spec.getModelCode();
		}//for PMC Edition Models..L3S2AB6/L3S4CB6
		else if(uniqueModels.contains(spec.getModelYearCode()+spec.getModelCode()+spec.getModelTypeCode())) {
			modelCodeAndYear = spec.getModelYearCode()+spec.getModelCode()+spec.getModelTypeCode();
		}
		if(StringUtils.isEmpty(modelCodeAndYear)) {
			checkResults.add(createCheckResult("Sequence number Check Failed - No 'UNIQUE_MODEL_TYPE_YEAR' set up in the properties "));
		}else {
			Integer nextSequenceNumber = ServiceFactory.getDao(FrameDao.class).findNextSequencePlateNumber(modelCodeAndYear);
	
		
			if(nextSequenceNumber == null) {
				checkResults.add(createCheckResult("Sequence number Check Failed - No sequence number set for this model "+modelCodeAndYear));
			}else if( nextSequenceNumber.intValue() != scannedSequence.intValue()) {
				checkResults.add(createCheckResult("Sequence number Check Failed - Next sequence plate number should be  "+nextSequenceNumber));
			}
		}
		
		
		 
		
		return checkResults;
	}
	


}
