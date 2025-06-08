package com.honda.galc.client.teamleader.qi.productRecovery;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.Head;
import com.honda.galc.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>ManualLotCtrRepairUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotCtrRepairUtil description </p>
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
 * <TD>May 26, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author L&T Infotech
 * Aug 28, 2017
 */

public class ManualLotCtrRepairUtil {
	public final static String PROCESS_POINT_ID = "ProcessPointId";
	public final static String MISSION_PART_NAME = "MissionPartName";
	public final static String MISSION_TYPE_PART_NAME = "MissionTypePartName";
	public final static String LAST_PASSING_PPID = "LastPassingPpid";
	public final static String HEAD_PART_NAME = "HeadPartName";
	public final static String BLOCK_PART_NAME = "BlockPartName";
	public final static String CRANKSHAFT_PART_NAME = "CrankshaftPartName";
	public final static String CONROD_PART_NAME = "ConrodPartName";
	public final static String MC_OFF_PROCESS_POINT_ID = "McOffProcessPointId";
	public final static String BLOCK_PART_NAME_SN_TYPE = "BlockPartNameSnType";

	public static boolean isMissionType(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		return checkMissionInstallPropertyMap(property, partResult,MISSION_TYPE_PART_NAME);
	}

	public static boolean isMission(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		return checkMissionInstallPropertyMap(property, partResult,MISSION_PART_NAME);
	}
	
	private static boolean checkMissionInstallPropertyMap(ManualLotControlRepairPropertyBean property, PartResult partResult,String missionType) {
		if(property.getMissionInstallPropertyMap() == null || property.getMissionInstallPropertyMap().size() ==0)
			return false;
		
		return (property.getMissionInstallPropertyMap().get(missionType).equals(partResult.getPartName())
				&& property.getMissionInstallPropertyMap().get(PROCESS_POINT_ID).equals(partResult.getProcessPointId()));
	}
	
	public static boolean isEngineHead(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getHeadMarriagePropertyMap() == null || property.getHeadMarriagePropertyMap().size() ==0)
			return false;
		
		return (property.getHeadMarriagePropertyMap().get(HEAD_PART_NAME).equals(partResult.getPartName())
				&& property.getHeadMarriagePropertyMap().get(PROCESS_POINT_ID).equals(partResult.getProcessPointId()));
	}

	public static boolean isEngineBlock(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getBlockLoadPropertyMap() == null || property.getBlockLoadPropertyMap().size() == 0)
				return false;
		
		return (property.getBlockLoadPropertyMap().get(BLOCK_PART_NAME).equals(partResult.getPartName())
				&& property.getBlockLoadPropertyMap().get(PROCESS_POINT_ID).equals(partResult.getProcessPointId()));
	}

	public static boolean isEngineCrankshaft(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getCrankshaftLoadPropertyMap() == null || property.getCrankshaftLoadPropertyMap().size() == 0)
				return false;
		
		return (property.getCrankshaftLoadPropertyMap().get(CRANKSHAFT_PART_NAME).equals(partResult.getPartName())
				&& property.getCrankshaftLoadPropertyMap().get(PROCESS_POINT_ID).equals(partResult.getProcessPointId()));
	}

	public static boolean isEngineConrod(ManualLotControlRepairPropertyBean property, PartResult partResult) {
		
		if(property.getConrodLoadPropertyMap() == null || property.getConrodLoadPropertyMap().size() == 0)
				return false;
		
		return (property.getConrodLoadPropertyMap().get(CONROD_PART_NAME).equals(partResult.getPartName())
				&& property.getConrodLoadPropertyMap().get(PROCESS_POINT_ID).equals(partResult.getProcessPointId()));
	}

	public static String getMissionTypePartName(ManualLotControlRepairPropertyBean property) {
		return property.getMissionInstallPropertyMap().get(MISSION_TYPE_PART_NAME);
	}

	public static String getMissionPartName(ManualLotControlRepairPropertyBean property) {
		return property.getMissionInstallPropertyMap().get(MISSION_PART_NAME);
	}

	public static String getHeadLastPassingPointId(ManualLotControlRepairPropertyBean property) {
		return property.getHeadMarriagePropertyMap().get(LAST_PASSING_PPID);
	}

	public static String getBlockLastPassingPointId(ManualLotControlRepairPropertyBean property) {
		return property.getBlockLoadPropertyMap().get(LAST_PASSING_PPID);
	}
	
	public static String getBlockMcOffProcessPointId(ManualLotControlRepairPropertyBean property) {
		return property.getBlockLoadPropertyMap().get(MC_OFF_PROCESS_POINT_ID);
	}
	
	public static String getHeadMcOffProcessPointId(ManualLotControlRepairPropertyBean property) {
		return property.getHeadMarriagePropertyMap().get(MC_OFF_PROCESS_POINT_ID);
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
		if (!property.getBlockLoadPropertyMap().containsKey(BLOCK_PART_NAME_SN_TYPE)) {
			return defaultNumberType;
		}
		String str = property.getBlockLoadPropertyMap().get(BLOCK_PART_NAME_SN_TYPE);
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
}
