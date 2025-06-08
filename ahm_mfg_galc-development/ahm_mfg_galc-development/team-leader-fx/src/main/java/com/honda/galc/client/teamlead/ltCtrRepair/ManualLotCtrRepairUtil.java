package com.honda.galc.client.teamlead.ltCtrRepair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.StringConverter;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.Head;
import com.honda.galc.service.ServiceFactory;



public class ManualLotCtrRepairUtil {
	public static String ProcessPointId = "ProcessPointId";
	public static String MissionPartName = "MissionPartName";
	public static String MissionTypePartName = "MissionTypePartName";
	public static String LastPassingPpid = "LastPassingPpid";
	public static String HeadPartName = "HeadPartName";
	public static String BlockPartName = "BlockPartName";
	public static String CrankshaftPartName = "CrankshaftPartName";
	public static String ConrodPartName = "ConrodPartName";
	public static String McOffProcessPointId = "McOffProcessPointId";
	public static String BlockPartNameSnType = "BlockPartNameSnType";

	public static boolean isMissionType(ManualLotControlRepairPropertyBean property, PartResult partResult) {
			
		if(property.getMissionInstallPropertyMap() == null || property.getMissionInstallPropertyMap().size() ==0)
			return false;
		
		return (property.getMissionInstallPropertyMap().get(MissionTypePartName).equals(partResult.getPartName())
				&& property.getMissionInstallPropertyMap().get(ProcessPointId).equals(partResult.getProcessPointId()));
	}
	

	public static boolean isMission(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getMissionInstallPropertyMap() == null || property.getMissionInstallPropertyMap().size() ==0)
			return false;
		
		return (property.getMissionInstallPropertyMap().get(MissionPartName).equals(partResult.getPartName())
				&& property.getMissionInstallPropertyMap().get(ProcessPointId).equals(partResult.getProcessPointId()));
	}
	
	public static boolean isEngineHead(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getHeadMarriagePropertyMap() == null || property.getHeadMarriagePropertyMap().size() ==0)
			return false;
		
		return (property.getHeadMarriagePropertyMap().get(HeadPartName).equals(partResult.getPartName())
				&& property.getHeadMarriagePropertyMap().get(ProcessPointId).equals(partResult.getProcessPointId()));
	}

	public static boolean isEngineBlock(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getBlockLoadPropertyMap() == null || property.getBlockLoadPropertyMap().size() == 0)
				return false;
		
		return (property.getBlockLoadPropertyMap().get(BlockPartName).equals(partResult.getPartName())
				&& property.getBlockLoadPropertyMap().get(ProcessPointId).equals(partResult.getProcessPointId()));
	}

	public static boolean isEngineCrankshaft(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getCrankshaftLoadPropertyMap() == null || property.getCrankshaftLoadPropertyMap().size() == 0)
				return false;
		
		return (property.getCrankshaftLoadPropertyMap().get(CrankshaftPartName).equals(partResult.getPartName())
				&& property.getCrankshaftLoadPropertyMap().get(ProcessPointId).equals(partResult.getProcessPointId()));
	}

	public static boolean isEngineConrod(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getConrodLoadPropertyMap() == null || property.getConrodLoadPropertyMap().size() == 0)
				return false;
		
		return (property.getConrodLoadPropertyMap().get(ConrodPartName).equals(partResult.getPartName())
				&& property.getConrodLoadPropertyMap().get(ProcessPointId).equals(partResult.getProcessPointId()));
	}

	public static String getMissionTypePartName(ManualLotControlRepairPropertyBean property) {
		return property.getMissionInstallPropertyMap().get(MissionTypePartName);
	}

	public static String getMissionPartName(ManualLotControlRepairPropertyBean property) {
		return property.getMissionInstallPropertyMap().get(MissionPartName);
	}

	public static String getHeadLastPassingPointId(ManualLotControlRepairPropertyBean property) {
		return property.getHeadMarriagePropertyMap().get(LastPassingPpid);
	}

	public static String getBlockLastPassingPointId(ManualLotControlRepairPropertyBean property) {
		return property.getBlockLoadPropertyMap().get(LastPassingPpid);
	}
	
	public static String getBlockMcOffProcessPointId(ManualLotControlRepairPropertyBean property) {
		return property.getBlockLoadPropertyMap().get(McOffProcessPointId);
	}
	
	public static String getHeadMcOffProcessPointId(ManualLotControlRepairPropertyBean property) {
		return property.getHeadMarriagePropertyMap().get(McOffProcessPointId);
	}

	public static Head findHead(String headNumber) {
		Head head = null;
		HeadDao dao = ServiceFactory.getDao(HeadDao.class);
		if(!isHcmHead(headNumber)){
			head = dao.findByKey(headNumber);
		} else if(isHeadDcNumber(headNumber)){
			head = dao.findByDCSerialNumber(headNumber);
		} else if(isHeadMcNumber(headNumber)){
			head = dao.findByMCSerialNumber(headNumber);
		}
		return head;
	}

	private static boolean isHcmHead(String headNumber) {
		return isHeadDcNumber(headNumber) || isHeadMcNumber(headNumber);
	}
	
	private static boolean isHeadDcNumber(String headNumber) {
		
		return ProductNumberDef.DCH.getLength() == headNumber.length();
	}

	private static boolean isHeadMcNumber(String headNumber) {
		return ProductNumberDef.MCH.getLength() == headNumber.length();
	}

	public static Block findBlock(String blockNumber) {
		BlockDao dao = ServiceFactory.getDao(BlockDao.class);
		Block block = null;
		if(!isHcmBlock(blockNumber)){
			block = dao.findByKey(blockNumber);
		} else if(isBlockDcNumber(blockNumber)){
			block = dao.findByDCSerialNumber(blockNumber);
		} else if(isBlockMcNumber(blockNumber)){
			block = dao.findByMCSerialNumber(blockNumber);
		}
		return block;
	}

	public static boolean isHcmBlock(String blockNumber) {
		return isBlockDcNumber(blockNumber) || isBlockMcNumber(blockNumber);
	}

	public static boolean isBlockDcNumber(String blockNumber) {
		return ProductNumberDef.DCB.getLength() == blockNumber.length();
	}

	public static boolean isBlockMcNumber(String blockNumber) {
		return ProductNumberDef.MCB.getLength() == blockNumber.length();
	}
	
	private static ProcessPoint getProcessPoint(String lastProcessingPointId) {
		ProcessPointDao dao = ServiceFactory.getDao(ProcessPointDao.class);
		return dao.findByKey(lastProcessingPointId);
	}

	public static ProcessPoint getHeadLastPassingPoint(ManualLotControlRepairPropertyBean property) {
		return getProcessPoint(getHeadLastPassingPointId(property));
	}

	public static ProcessPoint getBlockLastPassingPoint(ManualLotControlRepairPropertyBean property) {
		return getProcessPoint(getBlockLastPassingPointId(property));
	}

	public static ProcessPoint getHeadMcOffProcessPoint(ManualLotControlRepairPropertyBean property) {
		return getProcessPoint(getHeadMcOffProcessPointId(property));
	}

	public static ProcessPoint getBlockMcOffProcessPoint(ManualLotControlRepairPropertyBean property) {
		return getProcessPoint(getBlockMcOffProcessPointId(property));
	}
	
	public static void updateHead(Head head) {
		HeadDao dao = ServiceFactory.getDao(HeadDao.class);
		dao.update(head);
		
	}
	
	public static void updateBlock(Block block) {
		// update block table
		BlockDao blockDao = ServiceFactory.getDao(BlockDao.class);
		blockDao.update(block);
	}
	
	public static void updateBlockLoad(Block block){
		if (StringUtils.isBlank(block.getMcSerialNumber())) {
			return;
		}
		// update block load table
		BlockLoadDao blockLoadDao = ServiceFactory.getDao(BlockLoadDao.class);
		BlockLoad blockLoad = blockLoadDao.findByKey(block.getMcSerialNumber());
		if(blockLoad != null){
			blockLoad.setStatus(BlockLoadStatus.STAMPED);
			blockLoadDao.update(blockLoad);
			Logger.getLogger().info("BloadLoad Id:", blockLoad.getMcNumber(), 
					" status was updated to:", BlockLoadStatus.STAMPED.toString());
		}
	}

	public static NumberType getBlockPartNameSnType(ManualLotControlRepairPropertyBean property) {
		NumberType defaultNumberType = NumberType.MC;
		if (property == null) {
			return defaultNumberType;
		}
		if (property.getBlockLoadPropertyMap() == null) {
			return defaultNumberType;
		}
		if (!property.getBlockLoadPropertyMap().containsKey(BlockPartNameSnType)) {
			return defaultNumberType;
		}
		String str = property.getBlockLoadPropertyMap().get(BlockPartNameSnType);
		str = StringUtils.trim(str);
		NumberType numberType = null;
		for (NumberType nt : NumberType.values()) {
			if (nt.name().equals(str)) {
				numberType = nt;
				break;
			}
		}
		return numberType;
	}
	
	public static boolean hasScanPart(MCOperationRevision operation) {
		if (operation == null)
			return false;

		return (operation.getType().equals(OperationType.GALC_SCAN)
				|| operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS)
				|| operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL));
	}

	public static boolean isMeasOnlyOperation(MCOperationRevision operation) {
		if (operation == null)
			return false;
		return (operation.getType().equals(OperationType.GALC_MEAS)
				|| operation.getType().equals(OperationType.GALC_MEAS_MANUAL));

	}
	
	public static boolean isInstructionOrAutoCompleteOperation(MCOperationRevision operation) {
		if (operation == null)
			return false;
		return (operation.getType().equals(OperationType.INSTRUCTION) ||
				operation.getType().equals(OperationType.GALC_AUTO_COMPLETE) ||
				operation.getType().equals(OperationType.GALC_INSTRUCTION) ||
				operation.getType().equals(OperationType.GALC_MADE_FROM));

	}
	
	public static boolean hasScanMeasurementPart(MCOperationRevision operation) {
		if (operation == null)
			return false;

		return (operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS)
				|| operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL));
	}
	
	public static boolean hasOnlyInstalledPart(String opType) {
		if (StringUtils.isEmpty(opType) )
			return false;

		return (opType.equals(OperationType.GALC_SCAN.name())
				|| opType.equals(OperationType.INSTRUCTION.name())
				|| opType.equals(OperationType.GALC_AUTO_COMPLETE.name()));
	}
	
	public static StringConverter<Division> divisionStringConverter() {
		return new StringConverter<Division>() {

			@Override
			public Division fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(Division arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getDivisionName();
				}
			}

		};
	}

	public static StringConverter<ProcessPoint> processPointStringConverter() {
		return new StringConverter<ProcessPoint>() {

			@Override
			public ProcessPoint fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(ProcessPoint arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getProcessPointName();
				}
			}

		};
	}
	
	

	
	public static List<String> getOperationTypes(){
		List<String> types = new ArrayList<String>();
		for(OperationType type : OperationType.values()){
			types.add(type.name());
		}
		return types;
	}
	
	public static List<String> getSpecialityScreens(){
		List<Object[]> viewProcessors = ServiceFactory.getService(MCOperationRevisionDao.class).findAllOperationViewAndProcessor();
		List<String> viewProcessorsList = new ArrayList<String>();
		viewProcessorsList.add("");
		for (Object[] opName: viewProcessors) {
			Pattern p = Pattern.compile("([^/.]+)*$");
			Matcher m1 = p.matcher(opName[0].toString().trim());
			Matcher m2 = p.matcher(opName[1].toString().trim());
			if (m1.find() && m2.find()) {
				viewProcessorsList.add(m1.group()+"/"+m2.group());
			}
		}
		return viewProcessorsList;
	}
	
	public static List<String> getToolTypes(){
		List<Object[]> deviceIdData = ServiceFactory.getService(MCOperationMeasurementDao.class).findDistinctDeviceId();
		List<String> deviceId = new ArrayList<String>();
		deviceId.add("");
		for(int k=0; k<deviceIdData.size() ; k++) {
			if(deviceIdData.get(k)!=null) {
				Object obj = deviceIdData.get(k);
				deviceId.add(obj.toString().trim());	
			}
		}
		return deviceId;
	}

	/**
	 * This method is used to remove duplicate items from an ArrayList
	 * @param arrayList
	 * @return
	 */
	public static <T extends Object>List<T> getUniqueArrayList(List<T> arrayList) {
		List<T> uniqueDtoList = new ArrayList<T>(new HashSet<T>(arrayList));
		return uniqueDtoList;
	}
	
	/**
	 * This method is used to add numeric field validation to an input control
	 * @param arrayList
	 * @return
	 */
	public static String checkNumericInput(String newValue) {
		if (!newValue.matches("(\\d+)|(\\d+\\.)|(\\d+\\.\\d{1,2})") && newValue.length() > 0) {
			return newValue.substring(0, newValue.length() - 1);
		}
		return newValue;
	}
}
