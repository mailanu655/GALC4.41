package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.dto.ProductionProgressDTO;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * <h3>NseProductionProgressTask Class description</h3>
 * <p> Nse Production Progress Task description </p>
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
 *
 * </TABLE>
 *   
 * @author Larry Karpov <br>
 * May 14, 2014
 */
public class NseProductionProgressTask extends OifTask<Object> implements
		IEventTaskExecutable {

	public NseProductionProgressTask(String pObjectName) throws IOException {
		super(pObjectName);
		errorsCollector = new OifErrorsCollector(pObjectName);
	}

	public final String PPID = "PPID";
	public final String LAST_LOT_SENT_TO_NSE = "LAST_LOT_SENT_TO_NSE";
	public final String LAST_KD_LOT_SENT_TO_NSE = "LAST_KD_LOT_SENT_TO_NSE";
	public final String PROPERTY_VALUE = "PROPERTY_VALUE";

	private OifErrorsCollector errorsCollector;

	public void execute(Object[] args) {
		try {
			processNseProductionProgress();
		} catch (TaskException e) {
			logger.emergency(e);
			errorsCollector.emergency(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}

	// main method
	// create a helper to create an output line based on ProductionProgressDTO
	// create a file and send a message
	private void processNseProductionProgress() {
		initialize();
		OutputFormatHelper<ProductionProgressDTO> ofHelper = new OutputFormatHelper<ProductionProgressDTO>(
				getProperty(OIFConstants.OUTPUT_FORMAT_DEFS), this.logger,
				this.errorsCollector);
		ofHelper.initialize(ProductionProgressDTO.class);
		List<String> lotResultList = new ArrayList<String>();
		lotResultList.addAll(processPpidList(ofHelper));
		String interfaceID = getProperty(OIFConstants.INTERFACE_ID);
		String resultPath = PropertyService.getProperty(
				OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
		String exportFilePath = new StringBuffer(resultPath)
				.append(interfaceID).append(".oif").toString();
		try {
			if (lotResultList.size() > 0) {
				OIFFileUtility.writeToFile(lotResultList, exportFilePath);
			}
		} catch (IOException e) {
			String errorResult = new StringBuffer("Failed to write data for: ")
					.append(interfaceID).append(" to file: ")
					.append(exportFilePath).toString();
			logger.error(e, errorResult);
			errorsCollector.emergency(e, errorResult);
		}
		logger.info(new StringBuffer("Processed ").append(interfaceID)
				.append(", File: ").append(exportFilePath).toString());
		String interfaceId = getProperty(OIFConstants.INTERFACE_ID);
		if (lotResultList.size() > 0) {
			MQUtility mqutil = new MQUtility(this);
			try {
				mqutil.executeMQSendAPI(interfaceId, PropertyService
						.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES,
								OIFConstants.MQ_CONFIG), exportFilePath);
			} catch (MQUtilityException e) {
				StringBuffer emergencyMessage = new StringBuffer();
				emergencyMessage.append("Failed to send interface. ")
						.append(interfaceId).append(", File: ")
						.append(exportFilePath);
				logger.emergency(emergencyMessage.toString());
				errorsCollector.emergency(emergencyMessage.toString());
			}
			logger.info(new StringBuffer("Sent ").append(interfaceID)
					.append(", File: ").append(exportFilePath).toString());
		} else {
			logger.info("No NSE Production Progress data found " + interfaceID);
		}
	}

	// get data from Product Result (GAL215TBX), Process Point (GAL214TBX), and
	// Production Lot (GAL217TBX) tables
	// update COMPONENT_STATUS_TBX
	private List<String> processPpidList(
			OutputFormatHelper<ProductionProgressDTO> ofHelper) {
		List<String> lotResultList = new ArrayList<String>();
		int n = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);
		char[] charArray = new char[n];
		Arrays.fill(charArray, ' ');
		List<String> departments = PropertyService.getPropertyList(componentId,	"DEPARTMENTS");
		for (String div : departments) {
			List<String> ppIdList = PropertyService.getPropertyList(componentId, createDepartmentKey("PPID", div));
			for (String ppid : ppIdList) {
				if(isSubAssemblyLine(div)){
					PreProductionLot preProdLot = getFirstAvailableProductionLot(div);
					ProcessPoint processPoint = getDao(ProcessPointDao.class).findById(ppid);
					String strResult = "";
					ProductionProgressDTO lotResult = getProductionProgressDTO(preProdLot,processPoint);					
						
					lotResult.setKdLotNumber(lotResult.getKdLotNumber().substring(0,16));
					strResult = ofHelper.formatOutput(lotResult, charArray);
					lotResultList.add(strResult);
				}else{
				List<Object[]> tempList = new ArrayList<Object[]>();
				tempList = getDao(ProductResultDao.class).findProdLotResults(
						siteName, siteLineId, div, ppid);
				String strResult = "";
				for (Object[] objArray : tempList) {
					ProductionProgressDTO lotResult = getProductionProgressDTO(objArray);
					String ppId = lotResult.getPpId();
					String kdLot = lotResult.getKdLotNumber();
					String prodLot = lotResult.getProductionLot();
					ComponentStatusId cpId = new ComponentStatusId(ppId, LAST_LOT_SENT_TO_NSE);
					ComponentStatus cpLot = getDao(ComponentStatusDao.class).findByKey(cpId);
					if (cpLot == null) {
						cpLot = new ComponentStatus();
						cpLot.setId(cpId);						
					}
					cpLot.setStatusValue(prodLot);
					getDao(ComponentStatusDao.class).save(cpLot);
					
					String lastLot = cpLot.getStatusValue();
					if (prodLot.compareTo(lastLot) > 0) {
						ProductionLot skippedLot = getDao(ProductionLotDao.class).findLastSkippedLot(ppId, lastLot, prodLot, siteName, siteLineId, div);
						logger.info("Skipped Lot: " + skippedLot);
						if(skippedLot != null) {
							kdLot = skippedLot.getKdLotNumber();
							prodLot = skippedLot.getProductionLot();
						}
					}
					List<ProductResult> lots = getDao(ProductResultDao.class).getSubLots(kdLot, div, ppId);
					HashSet<String> setLots = new HashSet<String>();
					Date maxTimestamp = null;
					for(ProductResult lot : lots) {
						setLots.add(lot.getId().getProductId());	// count only distinct ProductId
						if(maxTimestamp == null || maxTimestamp.before(lot.getActualTimestamp())) {
							maxTimestamp = lot.getActualTimestamp();
						}
					}
					int unitCount = setLots.size();
					if(maxTimestamp != null) { 
						lotResult.setLineOnTimestamp(new Timestamp(maxTimestamp.getTime()));
					}
					lotResult.setUnitsPassed(unitCount);
					lotResult.setKdLotNumber(lotResult.getKdLotNumber().substring(0,16));
					strResult = ofHelper.formatOutput(lotResult, charArray);
					lotResultList.add(strResult);

					ComponentStatusId cpIdKd = new ComponentStatusId(ppId, LAST_KD_LOT_SENT_TO_NSE);
					ComponentStatus cpKdLot = getDao(ComponentStatusDao.class).findByKey(cpIdKd);
					if (cpKdLot == null) {
						cpKdLot = new ComponentStatus();
						cpKdLot.setId(cpIdKd);
					}
					cpKdLot.setStatusValue(strResult);
					getDao(ComponentStatusDao.class).save(cpKdLot);
				}
				}
			}
		}
		return lotResultList;
	}

	/**
	 * Description
	 * 
	 * @param div
	 * @return	boolean 
	 */
	private boolean isSubAssemblyLine(String div) {
		List<String> subAssemblyLines= PropertyService.getPropertyList(componentId,	"SUB_ASSEMBLY_LINE");
		for (String subLines : subAssemblyLines)
		{
			if(StringUtils.equals(subLines, div)) return true;
		}
		return false;
	}

	private String createDepartmentKey(String key, String div) {
		return (new StringBuilder(key)).append("{").append(div).append("}").toString();
	}

	private ProductionProgressDTO getProductionProgressDTO(Object[] objArray) { 
		ProductionProgressDTO lotResult = new ProductionProgressDTO();
		lotResult.setProductionLot(objArray[0].toString());
		String kdLot = objArray[1].toString();
		lotResult.setKdLotNumber(kdLot);
		String procLoc = objArray[2].toString();
		lotResult.setProcessLocation(procLoc);
		setCountSize(lotResult, kdLot, procLoc);
		String ppId = objArray[4].toString().trim();
		lotResult.setPpId(ppId);
		int ppIdLength = ppId.length();
		String ppId7 = ppIdLength > 7 ? ppId.substring(ppIdLength-7) : ppId;
		lotResult.setPpId7(ppId7);
		lotResult.setPpIdDescription(objArray[5].toString());
		return lotResult;
	}
	
	private PreProductionLot getFirstAvailableProductionLot(String processLocation) {
		PreProductionLot preProdLot = null;
		PreProductionLot tempPreProdLot = null;
		PreProductionLot retPreProdLot = null;
		preProdLot = getDao(PreProductionLotDao.class).findLastPreProductionLotByProcessLocation(processLocation);
		if (preProdLot != null) {
			retPreProdLot = preProdLot;
			int sendStatus = preProdLot.getSendStatusId();
			while (sendStatus < PreProductionLotSendStatus.DONE.getId()) {
				tempPreProdLot =getDao(PreProductionLotDao.class).findParent(
						preProdLot.getProductionLot());
				retPreProdLot = preProdLot;
				if (tempPreProdLot == null)
					break;
				sendStatus = tempPreProdLot.getSendStatusId();
				preProdLot = tempPreProdLot;
			}
			if(retPreProdLot.getSendStatusId()==PreProductionLotSendStatus.WAITING.getId())
			{
				retPreProdLot=tempPreProdLot;
			}
			logger.info("Successful getFirstAvailableProductionLot(): "	+ retPreProdLot);
			return retPreProdLot;
		} else
			return null;
	}
	
	private ProductionProgressDTO getProductionProgressDTO(PreProductionLot ppLot, ProcessPoint processPoint){
		ProductionProgressDTO lotResult = new ProductionProgressDTO();
		lotResult.setProductionLot(ppLot.getProductionLot().toString());
		String kdLot = ppLot.getKdLot().toString();
		lotResult.setKdLotNumber(kdLot);
		String procLoc = ppLot.getProcessLocation().toString();
		lotResult.setProcessLocation(procLoc);
		String ppId = processPoint.getProcessPointId().toString().trim();
		lotResult.setPpId(ppId);
		setCountSize(lotResult, kdLot, procLoc);
		int ppIdLength = ppId.length();
		String ppId7 = ppIdLength > 7 ? ppId.substring(ppIdLength-7) : ppId;
		lotResult.setPpId7(ppId7);
		lotResult.setPpIdDescription(processPoint.getProcessPointDescription().toString());
		Date maxTimestampStr = ppLot.getUpdateTimestamp();
		lotResult.setLineOnTimestamp(new Timestamp(maxTimestampStr.getTime()));
		return lotResult;
	}
	
	private void setCountSize(ProductionProgressDTO lotResult, String kdLot, String procLoc) {
		Object[] result = null;
		result=	getDao(PreProductionLotDao.class).getKdStampCountLotSize(kdLot, procLoc);
		lotResult.setLotSize((Integer) result[0]);
		lotResult.setUnitsPassed((Integer) result[1]);	
	}

}
