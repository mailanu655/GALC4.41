package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ShippingTransactionDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.Adc01050abDto;
import com.honda.galc.service.msip.property.outbound.Adc01050abPropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Adc01050abHandler extends BaseMsipOutboundHandler<Adc01050abPropertyBean> {
	final Integer status = 4;
	final Integer effectiveDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));		
	final Character sendFlag = 'Y';
	final String ADC_50B_PROCESS = "50B";
	ShippingStatusDao shippingStatusDao;
	ShippingTransactionDao shippingTransactionDao;
	ProductResultDao pHistDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Adc01050abDto> fetchDetails() {		
		List<Adc01050abDto> adc01050ABList = new ArrayList<Adc01050abDto>();
		try {
			String cccIssueData = getPropertyBean().getPartName();
			String sendLocation = getPropertyBean().getSendLocation();
			String adc50AProcessCode = getPropertyBean().getAdcProcessCode();
			String tranType = getPropertyBean().getTranType();
			String[] keyNoPartName = getPropertyBean().getKeyNoPartName();

			shippingTransactionDao = ServiceFactory.getDao(ShippingTransactionDao.class);
			shippingStatusDao = ServiceFactory.getDao(ShippingStatusDao.class);
			InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
			pHistDao = ServiceFactory.getDao(ProductResultDao.class );
			List<ShippingTransaction> vins = shippingTransactionDao.get50ATransactionVin(status, effectiveDate,
					sendFlag, cccIssueData);
			for (ShippingTransaction shippingTransaction : vins) {		
				
				shippingTransaction = checkShippingTransaction(shippingTransaction);
				if(shippingTransaction == null){
					continue;
				}
				
				String keyNo = shippingTransaction.getKeyNumber();
				if (keyNoPartName != null && keyNoPartName.length!=0) {
					List<String> keyNoPartNameList = Arrays.asList(keyNoPartName);
					List<InstalledPart> installedPart = installedPartDao
							.findAllByProductIdAndPartNames(shippingTransaction.getId(), keyNoPartNameList);
					if (installedPart != null && !installedPart.isEmpty()) {
						String keyVal = installedPart.get(0).getPartSerialNumber();
						if (!StringUtils.isEmpty(keyVal)) {
							keyNo = StringUtils.leftPad(keyVal, 7, '0');
							shippingTransaction.setKeyNumber(keyNo);
						}
					}
				}
				shippingTransaction.setAdcProcessCode	(	adc50AProcessCode	);
				shippingTransaction.setSendLocation		(	sendLocation	);
				shippingTransaction.setTranType			(	tranType		);
				
				adc01050ABList.add(exportData(shippingTransaction));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to enrich data for Adc01050abDto: ");
		}		
		return adc01050ABList;
	}
	
	private Adc01050abDto exportData(ShippingTransaction shippingTransaction){
		Adc01050abDto adc01050ABDto = new Adc01050abDto();
		BeanUtils.copyProperties( shippingTransaction, adc01050ABDto );
		adc01050ABDto.setVin(ProductNumberDef.justifyJapaneseVIN(shippingTransaction.getVin(),
				getPropertyBean().getVinJustify().booleanValue()));
		adc01050ABDto.setPartInstalled(getPropertyBean().getPartInstalled());
		if (ADC_50B_PROCESS.equals(getPropertyBean().getAdcProcessCode())) {
			// put the FIF codes and fillers into the transactionDTO.
			String fifCode = this.getFIFCodeBySpecCode(shippingTransaction.getVin());
			if (fifCode == null) {
				getLogger().info("Unable to get the FIF CODE to the VIN = " + shippingTransaction.getVin());
			} else {
				adc01050ABDto.setFifCode(fifCode);
			}
		}
		try {
			shippingTransaction.setSendFlag('Y');
			// Retrieve the shipping status for the VIN
			ShippingStatus shippingStatus = shippingStatusDao.findByKey(shippingTransaction.getVin());
			// Change the status value 0 to 1 (already sent to AH)
			shippingStatus.setStatus(ShippingStatusEnum.S50A.getStatus());
			// Update the shipping status
			shippingStatusDao.update(shippingStatus);
		} catch (Exception prx) {
			getLogger().error("Error while updating Shipping Transaction status " + prx.getMessage());
			shippingTransaction.setSendFlag('N');
		}
		//save the shipping transaction (gal148tbx)
		ShippingTransaction newTransaction = new ShippingTransaction();
		//create a new instance to avoid the open jpa issue, when the entity is created
		//from the native query and try to save it, jpa looking for this entity in the cache
		//when it is not found, query the DB to get the entity and make the merge, then the 
		//exception org.apache.openjpa.persistence.EntityNotFoundException raise when jpa doesn't find the entity in the DB.
		BeanUtils.copyProperties( shippingTransaction, newTransaction );
		shippingTransactionDao.save( newTransaction );
		return adc01050ABDto;
	}

	private ShippingTransaction checkShippingTransaction(ShippingTransaction shippingTransaction){
		String afOffDate = shippingTransaction.getAfOffDate();
		
		if ( StringUtils.isBlank(shippingTransaction.getPriceString()))
		{
			getLogger().error( "Error in the VIN: " +  shippingTransaction.getId() + " doesn't have PRICE, it is not possible to send.");
			return null;
		}

		if (StringUtils.isBlank(shippingTransaction.getEngineNumber())) {
			String msg = String.format("Error : Shipping Transaction VIN:%s, has missing EIN and it will not be processed.", shippingTransaction.getId());
			getLogger().error(msg);
			return null;
		}
		if (StringUtils.isEmpty(afOffDate))  {
			Timestamp afOffTs = pHistDao.getMaxActualTs(shippingTransaction.getVin(), getPropertyBean().getAfOffProcessPoint());
			if(afOffTs != null)  {
				afOffDate = new SimpleDateFormat("yyMMdd").format(afOffTs);
				shippingTransaction.setAfOffDate(afOffDate);
			}
		}
		if (StringUtils.isBlank(afOffDate)) {
			String msg = String.format("Error : Shipping Transaction VIN:%s, has missing AF OFF Date and it will not be processed.", shippingTransaction.getId());
			getLogger().error(msg);
			return null;
		}
		if (StringUtils.isBlank(shippingTransaction.getKeyNumber())) {
			String msg = String.format("Error : Shipping Transaction VIN:%s, has missing key number and it will not be processed.", shippingTransaction.getId());
			getLogger().error(msg);
			return null;
		}
		return shippingTransaction;
	}
	/**
	 * 
	 * @param productId
	 * @return
	 */
	private String getFIFCodeBySpecCode(final String productId) {
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		FrameSpecDao frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		SalesOrderFifDao fifDao = ServiceFactory.getDao(SalesOrderFifDao.class);
		Frame currentVin = frameDao.findByKey(productId);
		FrameSpec frameSpec = frameSpecDao.findByKey(currentVin.getProductSpecCode());
		return fifDao.getFIFCodeByProductSpec(frameSpec);
	}
}
