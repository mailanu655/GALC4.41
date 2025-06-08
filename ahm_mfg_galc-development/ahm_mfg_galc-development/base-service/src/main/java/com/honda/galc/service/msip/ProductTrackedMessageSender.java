package com.honda.galc.service.msip;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;

import org.springframework.core.task.TaskExecutor;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.ProductTrackedDto;

/**
 * @author Subu Kathiresan
 * @date Apr 26, 2017
 */
public class ProductTrackedMessageSender implements IMsipMessageSender {
	
	private static final String className = ProductTrackedMessageSender.class.getName();

	private static TaskExecutor msipTaskExecutor = (TaskExecutor) ApplicationContextProvider.getBean("MsipTaskExecutor");

	public static boolean sendMessage(BaseProduct product, ProcessPoint processPoint, Timestamp actualTimestamp,
			String approverNo, String associateNo) {

		try {
			if (product.getProdLot() == null) {
				ProductionLot lot = getDao(ProductionLotDao.class).findByKey(product.getProductionLot());
				product.setProdLot(lot);
			}
			
			ProductTrackedDto productTrackedDto = new ProductTrackedDto();
			productTrackedDto.setActualTimestamp(actualTimestamp);
			productTrackedDto.setApproverNo(approverNo);
			productTrackedDto.setAssociateNo(associateNo);
			productTrackedDto.setDivisionId(processPoint.getDivisionId());
			productTrackedDto.setDivisionName(processPoint.getDivisionName());
			productTrackedDto.setLineId(processPoint.getLineId());
			productTrackedDto.setLineName(processPoint.getLineName());
			productTrackedDto.setPlantName(product.getProdLot().getPlantCode());	
			productTrackedDto.setProcessPointId(processPoint.getProcessPointId());
			productTrackedDto.setProcessPointName(processPoint.getProcessPointName());
			productTrackedDto.setProcessPointType(processPoint.getProcessPointType());
			productTrackedDto.setProductId(product.getProductId());
			productTrackedDto.setProductType(product.getProductType());
			productTrackedDto.setProductionDate(product.getProdLot().getProductionDate());								
			productTrackedDto.setProductionLot(product.getProductionLot());
			productTrackedDto.setLotNumber(product.getProdLot().getLotNumber());
			productTrackedDto.setLotSize(product.getProdLot().getLotSize());
			productTrackedDto.setLotStatus(product.getProdLot().getLotStatus());
			productTrackedDto.setKdLotNumber(product.getProdLot().getKdLotNumber());
			if (product.getProductType().equals(ProductType.FRAME)) {
				productTrackedDto.setAfOnSequenceNumber(((Frame) product).getAfOnSequenceNumber());
				productTrackedDto.setEngineSerialNo(((Frame) product).getEngineSerialNo());
				productTrackedDto.setEngineSerialNo(((Frame) product).getMissionSerialNo());
			} else if (product.getProductType().equals(ProductType.ENGINE)) {
				productTrackedDto.setEngineSerialNo(((Engine) product).getProductId());
				productTrackedDto.setMissionSerialNo(((Engine) product).getMissionSerialNo());
			}else if (product.getProductType().equals(ProductType.MISSION)) {
				
			}
			productTrackedDto.setProductSpecCode(product.getProductSpecCode());
			productTrackedDto.setPlanCode(product.getProdLot().getPlanCode());
			productTrackedDto.setLineNo(product.getProdLot().getLineNo());
			productTrackedDto.setSiteName(processPoint.getSiteName());

			setBuildSequence(product, productTrackedDto);

			msipTaskExecutor.execute(new PublishToTopicTask(productTrackedDto, null));
		} catch(Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;		
	}

	private static void setBuildSequence(BaseProduct product, ProductTrackedDto productTrackedDto) {
		PreProductionLot preProductionLot = getPreProductionLot(product.getProdLot());
		if (preProductionLot != null) {
			productTrackedDto.setBuildSequence(preProductionLot.getBuildSequenceNumber());
			productTrackedDto.setBuildSequenceNotFixed(preProductionLot.getBuildSeqNotFixedFlag());
		}
	}
	
	private static PreProductionLot getPreProductionLot(ProductionLot productionLot) {
		return getPreProductionLotDao().findByKey(productionLot.getProductionLot());
	}
	
	public static PreProductionLotDao getPreProductionLotDao() {
		return ServiceFactory.getDao(PreProductionLotDao.class);
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(className);
	}
}


