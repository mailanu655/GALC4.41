package com.honda.galc.client.teamlead.vios;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;

public class ViosMasterValidator {

	public static String masterPartValidate(MCViosMasterOperationPart mcOpPart) {
		
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(mcOpPart.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));

	}


	public  static String masterPartCheckerValidateForMFG(MCViosMasterOperationPartChecker checker) {
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(checker.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} else if (StringUtils.isEmpty(checker.getId().getCheckPoint())) {
			requiredFields.add("Check Point");
		} else if (checker.getId().getCheckSeq() == 0) {
			requiredFields.add("Check Seq");
		} else if (StringUtils.isEmpty(checker.getCheckName())) {
			requiredFields.add("Check Name");
		} else if (StringUtils.isEmpty(checker.getChecker())) {
			requiredFields.add("Checker");
		} else if (checker.getReactionType() == null) {
			requiredFields.add("Reaction Type");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}
	
	public  static String uploadMasterPartCheckerValidate(MCViosMasterOperationPartChecker checker) {
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(checker.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} else if (StringUtils.isEmpty(checker.getId().getCheckPoint())) {
			requiredFields.add("Check Point");
		} else if (checker.getId().getCheckSeq() == 0) {
			requiredFields.add("Check Seq");
		} else if (StringUtils.isEmpty(checker.getCheckName())) {
			requiredFields.add("Check Name");
		} else if (StringUtils.isEmpty(checker.getChecker())) {
			requiredFields.add("Checker");
		} else if (StringUtils.isEmpty(checker.getReactionTypeAsString())) {
			requiredFields.add("Reaction Type");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}

	public static String masterOperationValidate(MCViosMasterOperation mcOp) {
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(mcOp.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} 
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}

	public static String masterOperationMeasValidate(MCViosMasterOperationMeasurement mcOpMeas) {
		
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(mcOpMeas.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} else if (mcOpMeas.getNumOfBolts() == 0 && mcOpMeas.getId().getMeasurementSeqNum() == 0) {
			requiredFields.add("SeqNum/noOfBolt");
		} if(mcOpMeas.getMaxLimit() < mcOpMeas.getMinLimit()) {
			return "Max Limit should be greater than Min Limit.";
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}

	public static String masterProcessValidate(MCViosMasterProcess mcProcess) {
		
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(mcProcess.getProcessPointId())) {
			requiredFields.add("Process Point Id");
		} else if (StringUtils.isEmpty(mcProcess.getId().getAsmProcNo())) {
			requiredFields.add("ASM Proc No");
		} else if (mcProcess.getProcessSeqNum() == 0) {
			requiredFields.add("Process Seq No");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}

	public static String masterPlatformValidate(MCViosMasterPlatform mcPlatform) {
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(mcPlatform.getPlantLocCode())) {
			requiredFields.add("Plant Code");
		} else if (StringUtils.isEmpty(mcPlatform.getDeptCode())) {
			requiredFields.add("Dept Code");
		} else if (mcPlatform.getModelYearDate().intValue() == ((int) 0.0)) {
			requiredFields.add("Model Year");
		} else if (mcPlatform.getProdSchQty().intValue() == ((int) 0.0)) {
			requiredFields.add("ProdSch Qty");
		} else if (StringUtils.isEmpty(mcPlatform.getProdAsmLineNo())) {
			requiredFields.add("Line Number");
		} else if (StringUtils.isEmpty(mcPlatform.getVehicleModelCode())) {
			requiredFields.add("Model Code");
		} 
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}

	public static String masterOperationCheckerValidate(MCViosMasterOperationChecker checker) {
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(checker.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} else if (StringUtils.isEmpty(checker.getId().getCheckPoint())) {
			requiredFields.add("Check Point");
		} else if (checker.getId().getCheckSeq() == 0) {
			requiredFields.add("Check Seq");
		} else if (StringUtils.isEmpty(checker.getCheckName())) {
			requiredFields.add("Check Name");
		} else if (StringUtils.isEmpty(checker.getChecker())) {
			requiredFields.add("Checker");
		} else if (StringUtils.isEmpty(checker.getReactionTypeAsString())) {
			requiredFields.add("Reaction Type");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}
	
	public static String updateMasterOperationCheckerValidate(MCViosMasterOperationChecker checker) {
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(checker.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} else if (StringUtils.isEmpty(checker.getId().getCheckPoint())) {
			requiredFields.add("Check Point");
		} else if (checker.getId().getCheckSeq() == 0) {
			requiredFields.add("Check Seq");
		} else if (StringUtils.isEmpty(checker.getCheckName())) {
			requiredFields.add("Check Name");
		} else if (StringUtils.isEmpty(checker.getChecker())) {
			requiredFields.add("Checker");
		} else if (checker.getReactionType() == null) {
			requiredFields.add("Reaction Type");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}

	public static String masterOperationMeasurementCheckerValidate(MCViosMasterOperationMeasurementChecker checker) {
		
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(checker.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} else if (StringUtils.isEmpty(checker.getId().getCheckPoint())) {
			requiredFields.add("Check Point");
		} else if (checker.getId().getCheckSeq() == 0) {
			requiredFields.add("Check Seq");
		} else if (StringUtils.isEmpty(checker.getId().getCheckName())) {
			requiredFields.add("Check Name");
		} else if (StringUtils.isEmpty(checker.getChecker())) {
			requiredFields.add("Checker");
		} else if (checker.getReactionType()== null) {
			requiredFields.add("Reaction Type");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}
	
	public static String uploadMasterOperationMeasurementCheckerValidate(MCViosMasterOperationMeasurementChecker checker) {
		
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(checker.getId().getUnitNo())) {
			requiredFields.add("Unit No");
		} else if (StringUtils.isEmpty(checker.getId().getCheckPoint())) {
			requiredFields.add("Check Point");
		} else if (checker.getId().getCheckSeq() == 0) {
			requiredFields.add("Check Seq");
		} else if (StringUtils.isEmpty(checker.getId().getCheckName())) {
			requiredFields.add("Check Name");
		} else if (StringUtils.isEmpty(checker.getChecker())) {
			requiredFields.add("Checker");
		} else if (StringUtils.isEmpty(checker.getReactionTypeAsString())) {
			requiredFields.add("Reaction Type");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}

	private static String getRequiredFieldMessage(List<String> fields) {
		return "Kindly enter required field(s): " + StringUtils.join(fields, ',')+"\n";
	}

	public static String MBPNMasterDataValidate(MCViosMasterMBPNMatrixData entity) {
		List<String> requiredFields = new ArrayList<String>();
		if (StringUtils.isEmpty(entity.getId().getAsmProcNo())) {
			requiredFields.add("PDDA Process Number");
		} else if (StringUtils.isEmpty(entity.getId().getMbpnMask())) {
			requiredFields.add("MBPN Mask");
		}  else if (StringUtils.isEmpty(entity.getId().getMtcModel())) {
			requiredFields.add("Model Code");
		}  else if (StringUtils.isEmpty(entity.getId().getMtcType())) {
			requiredFields.add("Model Type");
		}
		return (requiredFields.isEmpty()?"":getRequiredFieldMessage(requiredFields));
	}
}

