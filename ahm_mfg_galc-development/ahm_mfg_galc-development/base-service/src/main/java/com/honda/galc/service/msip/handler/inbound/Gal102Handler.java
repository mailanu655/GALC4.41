package com.honda.galc.service.msip.handler.inbound;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gal102DetGroup;
import com.honda.galc.service.msip.dto.inbound.Gal102DetailRecord;
import com.honda.galc.service.msip.dto.inbound.Gal102Dto;
import com.honda.galc.service.msip.dto.inbound.Gal102HeaderRecord;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
/*
 * 
 *  Message Source: AHM-MF 
 *  Message Destination: 2SD-F-GALC 
 *  Interface ID: D--GMG#PMCGAL#GAL102H 
 *  Run Frequency---------------------------------------------------- 
 *  Received when AH issues purchase order to order sales by the model types. Sent day after PMC PC transmits PIN file. 
 *  Business Purpose------------------------------------------------ 
 *  PCs are necessary for two fundamental reasons:  First, they are the legal commitment to facilitate a purchase between
 *  AHM and PMC.  Second, AHM tracks the receipt of vehicles by purchase contract number.
 *   At the end of the month, AHM knows how many vehicles have or haven't been received against that PC number.
 *   PMC's shipments/invoice to AHM contains the PC numbers. 
 *   
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */

public class Gal102Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gal102Dto> {
	
	public Gal102Handler() {}
	
	public boolean processPurchaseContracts(Gal102Dto gal102dto) {
		try {
			Gal102HeaderRecord pcHeader = null;
			PurchaseContractDao pcDao = ServiceFactory.getDao(PurchaseContractDao.class);
			Gal102DetGroup[] pcList = gal102dto.getGal102DetGroup();
			
			for(Gal102DetGroup detGroup:pcList) {
				pcHeader = detGroup.getGal102HeaderRec();
				Gal102DetailRecord[] listDto = detGroup.getGal102DetailRec();
				for(Gal102DetailRecord dto:listDto){
					String pcNo = dto.getPurchaseContractNumber();
					String pcSalesModelCode = dto.getSalesModelId();
					String pcModelType = dto.getSalesModelTypeCode();
					String pcOptionCode = dto.getSalesModelOptionCode();
					String pcExtColor = dto.getFactoryColorCode();
					String pcIntColor = dto.getSalesInteriorColorCode();
	
					PurchaseContract pContract = pcDao.findByContractNoAndSpecCode(pcNo, pcSalesModelCode, pcModelType,
							pcOptionCode, pcExtColor, pcIntColor);
					if(pContract == null)  {
						pContract = createPurchaseContract(dto);
					}
					updatePurchaseContract(pContract, dto, pcHeader);
					pcDao.save(pContract);
				}
			}
			getLogger().info(" Purchase Contract saved; data processed");

			//update or insert data

			
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	

	public PurchaseContract createPurchaseContract(Gal102DetailRecord pcDtlDto)  {
		
		if(pcDtlDto == null)  return null;
		
		PurchaseContract pc = new PurchaseContract();
		pc.setPurchaseContractNumber(pcDtlDto.getPurchaseContractNumber());
		pc.setSalesModelCode(pcDtlDto.getSalesModelId());
		pc.setSalesModelTypeCode(pcDtlDto.getSalesModelTypeCode());
		pc.setSalesModelOptionCode(pcDtlDto.getSalesModelOptionCode());
		pc.setSalesExtColorCode(pcDtlDto.getFactoryColorCode());
		pc.setSalesIntColorCode(pcDtlDto.getSalesInteriorColorCode());
		return pc;
	}

	public void updatePurchaseContract(PurchaseContract pc, Gal102DetailRecord pcDtlDto, Gal102HeaderRecord headerDto)  {
		
		if(pcDtlDto == null || pc == null)  return;
		Calendar now = GregorianCalendar.getInstance();
		pc.setCurrency(pcDtlDto.getCurrencyCode());
		pc.setCurrencySettlement(pcDtlDto.getCurrencySettlementCode());
		pc.setDeliveryFormCode(pcDtlDto.getDeliveryFormCode());
		pc.setOrderUnit(pcDtlDto.getOrderedUnit());
		pc.setShipper(headerDto.getShipper());
		pc.setSubsCode(pcDtlDto.getSubCode());
		pc.setProductGroupCode(headerDto.getProductGroup());
		pc.setReceiveDate(now.getTime());
	}


	@Override
	public boolean execute(List<Gal102Dto> dtoList) {
		for(Gal102Dto dto:dtoList){
			processPurchaseContracts(dto);
		}
		return false;
	}
}
