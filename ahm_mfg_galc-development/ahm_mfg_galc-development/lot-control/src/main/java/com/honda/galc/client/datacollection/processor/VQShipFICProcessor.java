package com.honda.galc.client.datacollection.processor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.InRepairAreaDao;
import com.honda.galc.dao.conf.ParkingAddressDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.InRepairArea;
import com.honda.galc.entity.conf.ParkingAddress;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.DeleteEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.SaveEntity;
import com.honda.galc.entitypersister.UpdateEntity;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

public class VQShipFICProcessor extends PartSerialNumberProcessor {
	private final static String MESSAGE_ID = "PART_SN";
	private final static int PROCESS_OUT = 2;
	private final static int NOT_RETURN_FACTORY = 0;
	private final static int OVER_24_HOURS = 1;
	private final static int UNDER_24_HOURS = 0;
	private final static String ENTITIES_FOR = "VQShipFICProcessor";
	boolean isFrameFromEntityList = false;
	
	public VQShipFICProcessor(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized boolean execute(PartSerialNumber partnumber) {

		EntityList<AbstractEntity> entityList = null;
		EntityList<AbstractEntity> afterTrackingEntityList = null;
		try {
			String scannedFICVin = partnumber.getPartSn();
			String productId = getController().getState().getProductId();

//			if (scannedFICVin.trim().length() < getCommonPropertyBean().getMaxProductSnLength()) {
//				getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
//						"Incorrect VIN length.");
//				return false;
//			}

			// This is to compare the scanned VIN against the part mask - NACL-1140 
			try {
				if (validateAgainstPartMask()) checkPartSerialNumber(partnumber);
			} catch (Exception e) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
						"The FIC VIN " + scannedFICVin + " and Certification Label " + productId + " does not match.");
				return false;
			}
			
			if (scannedFICVin.length() > getCommonPropertyBean().getMaxProductSnLength()) {

				scannedFICVin = scannedFICVin.trim();
				if (scannedFICVin.length() > getCommonPropertyBean().getMaxProductSnLength()) {

					scannedFICVin = scannedFICVin.substring(scannedFICVin
							.length()
							- getCommonPropertyBean().getMaxProductSnLength(), scannedFICVin.length());

				}

			}

			if (scannedFICVin.equals(productId)) {
				partnumber.setPartSn(scannedFICVin);
			} else {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
						"The FIC VIN and Certification Label does not match");
				return false;
			}

			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			Frame frame = frameDao.findByKey(productId);

			ProductionLotDao productionLotDao = ServiceFactory
					.getDao(ProductionLotDao.class);
			ProductionLot prodLot = productionLotDao.findByKey(frame
					.getProductionLot());
			prodLot.setLotStatus(PROCESS_OUT);
			entityList = new EntityList<AbstractEntity>(ENTITIES_FOR, productId, partnumber.getPartSn());
			afterTrackingEntityList=new EntityList<AbstractEntity>(ENTITIES_FOR, productId, partnumber.getPartSn());
			
			entityList.addEntity(new UpdateEntity(prodLot, prodLot.toString(),productionLotDao));

			Logger.getLogger().info(
					"Updated the Production Lot status for VIN:" + productId);

			String[] afOffProcessPointIds = getFrameLinePropertyBean().getAfOffProcessPointId().split("\\|");
			List<ProductResult> afOffProductResultList = null;
			for (String afOffProcessPointId : afOffProcessPointIds) {
				ProcessPoint processPoint = ServiceFactory.getDao(
						ProcessPointDao.class).findByKey(afOffProcessPointId);
				ProductResultDao productResultDao = ServiceFactory
						.getDao(ProductResultDao.class);
				afOffProductResultList = productResultDao
						.findByProductAndProcessPoint(new ProductResult(frame,
								processPoint));
				if ((afOffProductResultList != null)
						&& (!afOffProductResultList.isEmpty())) {
					break;
				}
			}
			if ((afOffProductResultList == null)
					|| (afOffProductResultList.size() == 0)) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
						"VIN has not passed through AF OFF");
				return false;
			}
			Timestamp afOffTimestamp = null;
			for (ProductResult productResult : afOffProductResultList) {
				afOffTimestamp = new Timestamp(productResult
						.getActualTimestamp().getTime());
			}

			// NALC-1259 - All holds check on VIN + Engine
			if (PropertyService.getPropertyBean(FrameLinePropertyBean.class, context.getAppContext().getApplicationId())
					.isHoldCheck()) {
				ProductCheckUtil checkUtil = new ProductCheckUtil();
				checkUtil.setProduct(frame);
				List<String> productHoldList = checkUtil.productOnHoldCheck();
				if (productHoldList != null && productHoldList.size() > 0) {
					getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
							"The VIN " + scannedFICVin + " is on hold. Vehicle cannot ship!");
					return false;
				}
				List<String> engineHoldList=checkUtil.engineOnHoldCheck();
				if (engineHoldList != null && engineHoldList.size() > 0) {
					getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
							"The Engine assigned to this vehicle is on hold. Vehicle cannot ship!");
					return false;
				}
			} 
			
//			shipping status logic to a new method
	        updateShippingStatus(productId, afOffTimestamp, afterTrackingEntityList);
			
			InRepairAreaDao inRepairAreaDao = ServiceFactory
					.getDao(InRepairAreaDao.class);
			InRepairArea inRepairArea = inRepairAreaDao.findByKey(productId);
			if (inRepairArea != null) {

				entityList.addEntity(new DeleteEntity(inRepairArea, inRepairArea.toString(),inRepairAreaDao));
				Logger.getLogger()
						.info(
								"Deleted the InRepairArea product for VIN:"
										+ productId);
			}

			ParkingAddressDao parkingAddressDao=ServiceFactory.getDao(ParkingAddressDao.class);
			List<ParkingAddress> parkingAddressLst = parkingAddressDao.getParkingAddress(productId);
			
			for(ParkingAddress parkingAddress : parkingAddressLst){
				parkingAddress.setVin(null);
				entityList.addEntity(new UpdateEntity(parkingAddress, parkingAddress.toString(),parkingAddressDao));
			}

			if(getFrameLinePropertyBean().isUpdatePurchaseContract())
				updatePurchaseContract(entityList);
			
			getController().getState().getProduct().setMasterEntityList(entityList);
			getController().getState().getProduct().setAfterTrackingMasterEntityList(afterTrackingEntityList);

			return (super.execute(partnumber));

		} catch (Exception e) {
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
					"An Exception occured");
			if(entityList != null)
				entityList.clear();
			if(afterTrackingEntityList != null)
				afterTrackingEntityList.clear();
			
			Logger.getLogger().error(e.getMessage());

			return false;
		}

	}

	private boolean validateAgainstPartMask() {
		boolean maskFlag = StringUtils.isEmpty(getController().getState().getCurrentLotControlRule().getPartMasks());
		return isVerifyPartSerialNumber() && !maskFlag;
	}
	
	private void updatePurchaseContract(EntityList<AbstractEntity> entityList) {
		isFrameFromEntityList = false;
		// find purchase contract and update shipped unit
		List<PurchaseContract> list = ServiceFactory.getDao(PurchaseContractDao.class).
				findByProductSpecCode(getController().getState().getProductSpecCode());
		
		PurchaseContract purchaseContract = list.get(0);
		Frame frame = getFrameFromMasterEntityList();
		frame.setPurchaseContractNumber( purchaseContract.getPurchaseContractNumber());
		
		if(purchaseContract.getShipUnit() < purchaseContract.getOrderUnit())
			purchaseContract.setShipUnit(purchaseContract.getShipUnit() + 1);
		else
			Logger.getLogger().error("ERROR:purchase contract:", purchaseContract.getPurchaseContractNumber(), " was completed.");
		
		//update database
		PurchaseContractDao purchaseContractDao = ServiceFactory.getDao(PurchaseContractDao.class);
		entityList.addEntity(new UpdateEntity(purchaseContract, purchaseContract.toString(), purchaseContractDao));
		if(!isFrameFromEntityList)
			entityList.addEntity(new UpdateEntity(frame, frame.toString(), getFrameDao()));
	}

	private Frame getFrameFromMasterEntityList() {
		Stack<EntityList<AbstractEntity>> masterEntityList = getController().getState().getProduct().getMasterEntityList();
		for(EntityList<AbstractEntity> list : masterEntityList)
			for(AbstractEntity entity: list)
				if(entity.getEntity() instanceof Frame) {
					isFrameFromEntityList = true;
					return (Frame)entity.getEntity();
				}
		
		return getFrameDao().findByKey(getController().getState().getProductId());
	}

	protected void updateShippingStatus(String productId, Timestamp afOffTimestamp, EntityList<AbstractEntity> afterTrackingEntityList) throws Exception {
	    ShippingStatusDao shippingStatusDao = ServiceFactory.getDao(ShippingStatusDao.class);
	    ShippingStatus shippingStatus = shippingStatusDao.findByKey(productId);
	    
	    if (shippingStatus == null) {
	        shippingStatus = new ShippingStatus();
	        shippingStatus.setVin(productId);
	        // Vin cannot be invoiced more than once, invoiced flag set to 'N' on initial record creation and updated to 'Y' when invoiced
	        shippingStatus.setInvoiced("N");
	    }

	    shippingStatus.setStatus(NOT_RETURN_FACTORY);
	    Timestamp actualShipTime = new Timestamp(System.currentTimeMillis());
	    shippingStatus.setActualTimestamp(actualShipTime);

	    // Calculate OTS target time
	    StringBuffer afOffTime = new StringBuffer(new SimpleDateFormat("HH:mm:ss").format(afOffTimestamp));
	    java.sql.Date nextProductionDate = getNextProductionDate(afOffTimestamp);

	    StringBuffer otsTarget = new StringBuffer(nextProductionDate + " " + afOffTime.toString());
	    Timestamp otsTargetStamp = Timestamp.valueOf(otsTarget.toString());

	    // Check if shipping occurred within 24 hours
	    if (actualShipTime.after(otsTargetStamp)) {
	        shippingStatus.setOnTimeShipping(OVER_24_HOURS);
	    } else {
	        shippingStatus.setOnTimeShipping(UNDER_24_HOURS);
	    }

	    afterTrackingEntityList.addEntity(new SaveEntity(shippingStatus, shippingStatus.toString(), shippingStatusDao));
		Logger.getLogger().info("Updated the Shipping Status for VIN:" + productId);
	}

	protected java.sql.Date getNextProductionDate(Timestamp afOffTimestamp) throws Exception {
	    boolean useAfOffTimeStamp = false;
	    java.sql.Date afOffProdDay = null;
	    java.sql.Date nextProductionDate = null;

	    List<Object[]> afOffProdDayList = ServiceFactory.getDao(DailyDepartmentScheduleDao.class)
	            .getProductionDay(afOffTimestamp.toString(), "AF");
	    for (Object[] date : afOffProdDayList) {
	        if (date != null && date[0] != null) {
	            afOffProdDay = (java.sql.Date) date[0];
	        } else {
	            useAfOffTimeStamp = true;
	        }
	    }

	    if (!useAfOffTimeStamp) {
	        List<Object[]> nextProductionDateList = ServiceFactory.getDao(DailyDepartmentScheduleDao.class)
	                .findNextProdDate(afOffProdDay.toString(), "AF", "Y");
	        for (Object[] date : nextProductionDateList) {
	            if (date != null && date[0] != null) {
	                nextProductionDate = (java.sql.Date) date[0];
	            } else {
	                useAfOffTimeStamp = true;
	            }
	        }
	    }

	    if (useAfOffTimeStamp) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(afOffTimestamp);
	        cal.add(Calendar.DATE, 1);
	        nextProductionDate = new java.sql.Date(cal.getTimeInMillis());
	    }

	    return nextProductionDate;
	}
}