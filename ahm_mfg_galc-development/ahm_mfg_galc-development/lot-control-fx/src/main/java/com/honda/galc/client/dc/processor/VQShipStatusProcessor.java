package com.honda.galc.client.dc.processor;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.InRepairAreaDao;
import com.honda.galc.dao.conf.ParkingAddressDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.ShippingTransactionDao;
import com.honda.galc.entity.conf.InRepairArea;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.ParkingAddress;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ProductionLotStatus;
import com.honda.galc.entity.enumtype.ShippingOnTimeStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.VQShipProcessorBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class VQShipStatusProcessor extends LotControlOperationProcessor {

	private static final Character FACTORY_RETURN_SENDED_FLAG			= 'R';
	
	public VQShipStatusProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}

	public void completeOperation() {
		EntityList<AbstractEntity> entityList = null;
		EntityList<AbstractEntity> afterTrackingEntityList = null;
		VQShipProcessorBean vqShipPropertyBean = PropertyService.getPropertyBean(VQShipProcessorBean.class);
		FrameLinePropertyBean frameLinePropBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class);
		String productId = getController().getProductModel().getProductId();
		//find process-point
		ProcessPoint processPoint = ServiceFactory.getDao(
				ProcessPointDao.class).findByKey(frameLinePropBean.getAfOffProcessPointId());
		String[] keySetParts = vqShipPropertyBean.getKeySetPartName();
		//find the current frame
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		Frame frame = frameDao.findByKey(productId);
		
		//find and update the production lot status
		ProductionLotDao productionLotDao = ServiceFactory
				.getDao(ProductionLotDao.class);
		ProductionLot prodLot = productionLotDao.findByKey(frame
				.getProductionLot());
		prodLot.setLotStatus(ProductionLotStatus.PROCESS_OUT.getId());
		
		//create ship status record
		ShippingStatusDao shippingStatusDao = ServiceFactory
				.getDao(ShippingStatusDao.class);
		ShippingStatus shippingStatus = shippingStatusDao
				.findByKey(productId);
		ShippingTransactionDao shippingTransactionDao = ServiceFactory.getDao(ShippingTransactionDao.class);
		ShippingTransaction shippingTransaction = shippingTransactionDao.findByKey(productId);
		if (shippingStatus == null) {
			shippingStatus = new ShippingStatus();
			shippingStatus.setVin(productId);
			shippingStatus.setStatus(ShippingStatusEnum.INIT.getStatus());
			shippingStatus.setInvoiced("N");
		}else if(shippingStatus.getStatus() == ShippingStatusEnum.S90A.getStatus() ){
			shippingStatus.setStatus(ShippingStatusEnum.INIT.getStatus());
			//Also,update Shipping transaction table 
			if(shippingTransaction != null && FACTORY_RETURN_SENDED_FLAG.equals(shippingTransaction.getSendFlag())){
				shippingTransaction.setSendFlag('N');
			}
		}
		
		//actual ship time is current timestamp
		Timestamp actualShipTime = new Timestamp(System.currentTimeMillis());
		shippingStatus.setActualTimestamp(actualShipTime);

		//computing on-time status
		//find history record
		ProductResultDao productResultDao = ServiceFactory
				.getDao(ProductResultDao.class);
		List<ProductResult> afOffProductResultList = productResultDao
				.findByProductAndProcessPoint(new ProductResult(frame,
						processPoint));
		if ((afOffProductResultList == null)
				|| (afOffProductResultList.size() == 0)) {
			getLogger().error("VIN has not passed through AF OFF");
			return;
		}
		
		//get actual timestamp for af_off
		Timestamp afOffTimestamp = null;
		for (ProductResult productResult : afOffProductResultList) {
			afOffTimestamp = new Timestamp(productResult
					.getActualTimestamp().getTime());
		}

		//get time part of actual af_off time
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
		StringBuffer afOffTime = new StringBuffer(sdfTime
				.format(afOffTimestamp));
		java.sql.Date afOffProdDay = null;
		java.sql.Date nextProductionDate = null;

		//get the production date for af_off timestamp
		//the default in all cases is to use the af_off timestamp if a production date cannot be established
		boolean useAfOffTimeStamp = false;

		List<Object[]> afOffProdDayList = ServiceFactory.getDao(
				DailyDepartmentScheduleDao.class).getProductionDay(
				afOffTimestamp.toString(), "AF");
		for (Object[] date : afOffProdDayList) {
			if (date != null && date[0]!=null) {
				afOffProdDay = (java.sql.Date) date[0];
			} else {
				useAfOffTimeStamp = true;
			}
		}
		if (!useAfOffTimeStamp) {
			List<Object[]> nextProductionDateList = ServiceFactory.getDao(
					DailyDepartmentScheduleDao.class).findNextProdDate(
					afOffProdDay.toString(), "AF", "Y");
			for (Object[] date : nextProductionDateList) {
				if (date != null && date[0]!=null) {
					nextProductionDate = (java.sql.Date) date[0];
				} else {
					useAfOffTimeStamp = true;
				}
			}
		}
		StringBuffer otsTarget = null;
		Timestamp otsTargetStamp = null;
		if (useAfOffTimeStamp) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(afOffTimestamp);
			cal.add(Calendar.DATE, 1);
			nextProductionDate = new java.sql.Date(cal.getTimeInMillis());
		}
		//set on_time status
		otsTarget = new StringBuffer((nextProductionDate + " " + afOffTime
				.toString()));
		otsTargetStamp = (Timestamp) (Timestamp.valueOf(otsTarget
				.toString()));

		if (actualShipTime.after(otsTargetStamp)) {
			shippingStatus.setOnTimeShipping(ShippingOnTimeStatus.OVER_24HR.getStatus());
		} else {
			shippingStatus.setOnTimeShipping(ShippingOnTimeStatus.UNDER_24HR.getStatus());

		}

		InRepairAreaDao inRepairAreaDao = ServiceFactory
				.getDao(InRepairAreaDao.class);
		InRepairArea inRepairArea = inRepairAreaDao.findByKey(productId);
		if (inRepairArea != null) {
			Logger.getLogger()
					.info(
							"Deleted the InRepairArea product for VIN:"
									+ productId);
		}

		ParkingAddressDao parkingAddressDao=ServiceFactory.getDao(ParkingAddressDao.class);
		List<ParkingAddress> parkingAddressLst = parkingAddressDao.getParkingAddress(productId);
		
		for(ParkingAddress parkingAddress : parkingAddressLst){
			parkingAddress.setVin(null);
			parkingAddressDao.save(parkingAddress);
		}
		
		String keyName = "";
		if(keySetParts != null) {
			InstalledPartDao ipDao = ServiceFactory.getDao(InstalledPartDao.class);
			for(int i=0; (i < keySetParts.length && StringUtils.isBlank(keyName)); i++) {
				@SuppressWarnings("unused")
				String partSerialNo = null;
				List<InstalledPart> installedPartList =ipDao.findAllInstalledPartByCommonName(productId, StringUtils.trimToEmpty(keySetParts[i]));
				if(installedPartList != null && !installedPartList.isEmpty()) {
					keyName = installedPartList.get(0).getPartSerialNumber();
				} else {
					String partName = StringUtils.trimToEmpty(keySetParts[i]);
					keyName = ipDao.getLatestPartSerialNumber(productId, StringUtils.trimToEmpty(partName));
				}
			}
		}
		frame.setActualOffDate(afOffTimestamp);
		frame.setKeyNo(keyName);
		productionLotDao.save(prodLot);
		shippingStatusDao.save(shippingStatus);
		if(shippingTransaction != null)	shippingTransactionDao.save(shippingTransaction);
		inRepairAreaDao.remove(inRepairArea);
		frameDao.save(frame);
		return;
	}

}
